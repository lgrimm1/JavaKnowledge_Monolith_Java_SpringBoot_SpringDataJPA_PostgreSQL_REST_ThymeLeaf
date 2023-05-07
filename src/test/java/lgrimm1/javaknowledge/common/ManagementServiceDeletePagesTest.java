package lgrimm1.javaknowledge.common;

import lgrimm1.javaknowledge.html.*;
import lgrimm1.javaknowledge.process.*;
import lgrimm1.javaknowledge.title.*;
import lgrimm1.javaknowledge.txt.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;

class ManagementServiceDeletePagesTest {

	TitleRepository titleRepository;
	TxtRepository txtRepository;
	HtmlRepository htmlRepository;
	Formulas formulas;
	ProcessRecords processRecords;
	FileOperations fileOperations;
	Extractors extractors;
	ProcessPage processPage;
	HtmlGenerators htmlGenerators;
	ManagementService managementService;

	@BeforeEach
	void setUp() {
		titleRepository = Mockito.mock(TitleRepository.class);
		txtRepository = Mockito.mock(TxtRepository.class);
		htmlRepository = Mockito.mock(HtmlRepository.class);
		formulas = Mockito.mock(Formulas.class);
		processRecords = Mockito.mock(ProcessRecords.class);
		fileOperations = Mockito.mock(FileOperations.class);
		extractors = Mockito.mock(Extractors.class);
		processPage = Mockito.mock(ProcessPage.class);
		htmlGenerators = Mockito.mock(HtmlGenerators.class);
		managementService = new ManagementService(
				titleRepository,
				txtRepository,
				htmlRepository,
				processRecords,
				processPage,
				fileOperations,
				htmlGenerators,
				extractors,
				formulas
		);
		when(formulas.getTitleManagement())
				.thenReturn("MANAGEMENTTITLE");
		when(formulas.getTitleSource())
				.thenReturn("SOURCETITLE");
	}

	@Test
	void deletePages_NullPayload() {
		Exception e = Assertions.assertThrows(Exception.class, () -> managementService.deletePages(null));
		Assertions.assertEquals("PLEASE SELECT TITLES YOU WISH TO DELETE AND CONFIRM DELETION.", e.getMessage());
	}

	@Test
	void deletePages_NullTitles() {
		Payload receivedPayload = new Payload(
				"templateTitle",
				true,
				null,
				null,
				"",
				null,
				null,
				"",
				null
		);
		Exception e = Assertions.assertThrows(Exception.class, () -> managementService.deletePages(receivedPayload));
		Assertions.assertEquals("PLEASE SELECT TITLES YOU WISH TO DELETE AND CONFIRM DELETION.", e.getMessage());
	}

	@Test
	void deletePages_NoTitles() {
		List<String> titles = new ArrayList<>();
		Payload receivedPayload = new Payload(
				"templateTitle",
				true,
				null,
				null,
				"",
				null,
				null,
				"",
				titles
		);
		Exception e = Assertions.assertThrows(Exception.class, () -> managementService.deletePages(receivedPayload));
		Assertions.assertEquals("PLEASE SELECT TITLES YOU WISH TO DELETE AND CONFIRM DELETION.", e.getMessage());
	}

	@Test
	void deletePages_NullConfirm() {
		List<String> titles = List.of("Title 1");
		Payload receivedPayload = new Payload(
				"templateTitle",
				null,
				null,
				null,
				"",
				null,
				null,
				"",
				titles
		);
		Exception e = Assertions.assertThrows(Exception.class, () -> managementService.deletePages(receivedPayload));
		Assertions.assertEquals("PLEASE SELECT TITLES YOU WISH TO DELETE AND CONFIRM DELETION.", e.getMessage());
	}

	@Test
	void deletePages_NotConfirmed() {
		List<String> titles = List.of("Title 1");
		Payload receivedPayload = new Payload(
				"templateTitle",
				false,
				null,
				null,
				"",
				null,
				null,
				"",
				titles
		);
		Exception e = Assertions.assertThrows(Exception.class, () -> managementService.deletePages(receivedPayload));
		Assertions.assertEquals("PLEASE SELECT TITLES YOU WISH TO DELETE AND CONFIRM DELETION.", e.getMessage());
	}

	@Test
	void deletePages_WithNoValidTitles() {
		List<String> requestTitles = new ArrayList<>();
		requestTitles.add(null);
		requestTitles.add("  ");
		Payload receivedPayload = new Payload(
				"templateTitle",
				true,
				null,
				null,
				"",
				null,
				null,
				"",
				requestTitles
		);
		Exception e = Assertions.assertThrows(Exception.class, () -> managementService.deletePages(receivedPayload));
		Assertions.assertEquals("PLEASE SELECT TITLES YOU WISH TO DELETE AND CONFIRM DELETION.", e.getMessage());
	}

	@Test
	void deletePages_WithValidTitles() {
		List<String> cleanedRequestTitles = List.of("Title 3", "Title 4");
		when(processRecords.deleteByTitles(cleanedRequestTitles, titleRepository, txtRepository, htmlRepository))
				.thenReturn(1L);

		List<String> requestTitles = new ArrayList<>();
		requestTitles.add(null);
		requestTitles.add("  ");
		requestTitles.addAll(cleanedRequestTitles);

		Payload receivedPayload = new Payload(
				"templateTitle",
				true,
				null,
				null,
				"",
				null,
				null,
				"",
				requestTitles
		);

		List<String> restOfTitles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(restOfTitles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"1 OF 2 TITLES WERE DELETED.",
				null,
				null,
				"",
				restOfTitles
		);
		Assertions.assertEquals(expectedPayload, managementService.deletePages(receivedPayload));
	}
}