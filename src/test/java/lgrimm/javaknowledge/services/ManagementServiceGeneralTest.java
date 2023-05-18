package lgrimm.javaknowledge.services;

import lgrimm.javaknowledge.databasestorage.*;
import lgrimm.javaknowledge.datamodels.*;
import lgrimm.javaknowledge.process.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;

class ManagementServiceGeneralTest {

DatabaseStorageService databaseStorageService;
	Formulas formulas;
	ProcessRecords processRecords;
	ManagementService managementService;

	@BeforeEach
	void setUp() {
		databaseStorageService = Mockito.mock(DatabaseStorageService.class);
		formulas = Mockito.mock(Formulas.class);
		processRecords = Mockito.mock(ProcessRecords.class);
		managementService = new ManagementService(
				databaseStorageService,
				processRecords,
				formulas
		);
		when(formulas.getTitleManagement())
				.thenReturn("ManagementTitle");
		when(formulas.getTitleSource())
				.thenReturn("SourceTitle");
	}

	@Test
	void managePages() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(databaseStorageService.getAllTitles())
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"",
				null,
				null,
				"",
				titles
		);
		Assertions.assertEquals(expectedPayload, managementService.managePages());
	}

	@Test
	void createSourcePage() {
		Payload expectedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				"",
				false,
				"",
				null,
				null,
				"",
				null
		);
		Assertions.assertEquals(expectedPayload, managementService.createSourcePage());
	}

	@Test
	void generateHtml_NullPayload() {
		Exception e = Assertions.assertThrows(Exception.class, () -> managementService.generateHtml(null));
		Assertions.assertEquals("PLEASE CONFIRM GENERATING PAGES.", e.getMessage());
	}

	@Test
	void generateHtml_NullConfirm() {
		List<String> receivedTitles = List.of("Title 1");
		Payload receivedPayload = new Payload(
				"templateTitle",
				null,
				null,
				null,
				"",
				null,
				null,
				"",
				receivedTitles
		);
		Exception e = Assertions.assertThrows(Exception.class, () -> managementService.generateHtml(receivedPayload));
		Assertions.assertEquals("PLEASE CONFIRM GENERATING PAGES.", e.getMessage());
	}

	@Test
	void generateHtml_NotConfirmed() {
		List<String> receivedTitles = List.of("Title 1");
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"",
				null,
				null,
				"",
				receivedTitles
		);
		Exception e = Assertions.assertThrows(Exception.class, () -> managementService.generateHtml(receivedPayload));
		Assertions.assertEquals("PLEASE CONFIRM GENERATING PAGES.", e.getMessage());
	}

	@Test
	void generateHtml_Confirmed() {
		List<String> receivedTitles = List.of("Title 1");
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				true,
				null,
				null,
				"",
				null,
				null,
				"",
				receivedTitles
		);

		List<String> titles = List.of("Title 1", "Title 2");
		when(databaseStorageService.getAllTitles())
				.thenReturn(titles);

		when(processRecords.generate())
				.thenReturn(new long[]{12, 24});

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"12 PAGES IN 24 SECONDS HAS BEEN PROCESSED.",
				null,
				null,
				"",
				titles
		);
		Assertions.assertEquals(expectedPayload, managementService.generateHtml(receivedPayload));
	}
}