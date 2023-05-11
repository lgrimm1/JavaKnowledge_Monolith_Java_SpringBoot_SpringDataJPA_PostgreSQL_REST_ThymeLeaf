package lgrimm1.javaknowledge.services;

import lgrimm1.javaknowledge.databasestorage.*;
import lgrimm1.javaknowledge.datamodels.*;
import lgrimm1.javaknowledge.process.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;

class ManagementServiceEditSourcePageTest {
/*
	TitleRepository titleRepository;
	TxtRepository txtRepository;
	HtmlRepository htmlRepository;
*/
	DatabaseStorageService databaseStorageService;
	Formulas formulas;
	ProcessRecords processRecords;
	FileOperations fileOperations;
	Extractors extractors;
	ProcessPage processPage;
	HtmlGenerators htmlGenerators;
	ManagementService managementService;

	@BeforeEach
	void setUp() {
/*
		titleRepository = Mockito.mock(TitleRepository.class);
		txtRepository = Mockito.mock(TxtRepository.class);
		htmlRepository = Mockito.mock(HtmlRepository.class);
*/
		databaseStorageService = Mockito.mock(DatabaseStorageService.class);
		formulas = Mockito.mock(Formulas.class);
		processRecords = Mockito.mock(ProcessRecords.class);
		fileOperations = Mockito.mock(FileOperations.class);
		extractors = Mockito.mock(Extractors.class);
		processPage = Mockito.mock(ProcessPage.class);
		htmlGenerators = Mockito.mock(HtmlGenerators.class);
		managementService = new ManagementService(
/*
				titleRepository,
				txtRepository,
				htmlRepository,
*/
				databaseStorageService, processRecords,
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
	void editSourcePage_NullPayload() {
		Exception e = Assertions.assertThrows(Exception.class, () -> managementService.editSourcePage(null));
		Assertions.assertEquals("PLEASE SELECT EXACTLY ONE TITLE FOR EDITING.", e.getMessage());
	}

	@Test
	void editSourcePage_NullTitles() {
		Payload receivedPayload = new Payload(
				"templateTitle",
				false,
				null,
				null,
				"",
				null,
				null,
				"",
				null
		);
		Exception e = Assertions.assertThrows(Exception.class, () -> managementService.editSourcePage(receivedPayload));
		Assertions.assertEquals("PLEASE SELECT EXACTLY ONE TITLE FOR EDITING.", e.getMessage());
	}

	@Test
	void editSourcePage_MoreThanOneTitles() {
		List<String> receivedTitles = List.of("Title 1", "Title 2");
		Payload receivedPayload = new Payload(
				"templateTitle",
				false,
				null,
				null,
				"",
				null,
				null,
				"",
				receivedTitles
		);
		Exception e = Assertions.assertThrows(Exception.class, () -> managementService.editSourcePage(receivedPayload));
		Assertions.assertEquals("PLEASE SELECT EXACTLY ONE TITLE FOR EDITING.", e.getMessage());
	}

	@Test
	void editSourcePage_FirstTitleIsNull() {
		List<String> receivedTitles = new ArrayList<>();
		receivedTitles.add(null);
		Payload receivedPayload = new Payload(
				"templateTitle",
				false,
				null,
				null,
				"",
				null,
				null,
				"",
				receivedTitles
		);
		Exception e = Assertions.assertThrows(Exception.class, () -> managementService.editSourcePage(receivedPayload));
		Assertions.assertEquals("PLEASE SELECT EXACTLY ONE TITLE FOR EDITING.", e.getMessage());
	}

	@Test
	void editSourcePage_FirstTitleBlank() {
		List<String> receivedTitles = new ArrayList<>();
		receivedTitles.add("  ");
		Payload receivedPayload = new Payload(
				"templateTitle",
				false,
				null,
				null,
				"",
				null,
				null,
				"",
				receivedTitles
		);
		Exception e = Assertions.assertThrows(Exception.class, () -> managementService.editSourcePage(receivedPayload));
		Assertions.assertEquals("PLEASE SELECT EXACTLY ONE TITLE FOR EDITING.", e.getMessage());
	}

	@Test
	void editSourcePage_NoSuchTitle() {
		List<String> receivedTitles = new ArrayList<>();
		receivedTitles.add("Title 3");
		Payload receivedPayload = new Payload(
				"templateTitle",
				false,
				null,
				null,
				"",
				null,
				null,
				"",
				receivedTitles
		);
		List<String> requestTitles = List.of("Title 3");
		when(databaseStorageService.findTitleByTitle(requestTitles.get(0)))
				.thenReturn(Optional.empty());
		Exception e = Assertions.assertThrows(Exception.class, () -> managementService.editSourcePage(receivedPayload));
		Assertions.assertEquals("PLEASE SELECT EXACTLY ONE TITLE FOR EDITING.", e.getMessage());
	}

	@Test
	void editSourcePage_NoSuchTxt() {
		List<String> receivedTitles = new ArrayList<>();
		receivedTitles.add("Title 3");
		Payload receivedPayload = new Payload(
				"templateTitle",
				false,
				null,
				null,
				"",
				null,
				null,
				"",
				receivedTitles
		);
		when(databaseStorageService.findTitleByTitle(receivedTitles.get(0)))
				.thenReturn(Optional.of(new TitleEntity(3L, "Title 3", "title_3", 2L, 4L)));
		when(databaseStorageService.findTxtById(2L))
				.thenReturn(Optional.empty());
		Exception e = Assertions.assertThrows(Exception.class, () -> managementService.editSourcePage(receivedPayload));
		Assertions.assertEquals("PLEASE SELECT EXACTLY ONE TITLE FOR EDITING.", e.getMessage());
	}

	@Test
	void editSourcePage_ExistingTitleAndTxt() {
		List<String> receivedTitles = new ArrayList<>();
		receivedTitles.add("Title 3");
		Payload receivedPayload = new Payload(
				"templateTitle",
				false,
				null,
				null,
				"",
				null,
				null,
				"",
				receivedTitles
		);

		String expectedTitle = receivedTitles.get(0);
		String expectedFilename = "title_3";
		List<String> expectedTitles = List.of(expectedTitle);
		when(databaseStorageService.findTitleByTitle(expectedTitles.get(0)))
				.thenReturn(Optional.of(new TitleEntity(3L, expectedTitle, expectedFilename, 2L, 4L)));

		String content = "Line 1\nLine 2\n";
		when(databaseStorageService.findTxtById(2L))
				.thenReturn(Optional.of(new TxtEntity(2L, content)));

		Payload expectedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				content,
				true,
				"",
				null,
				null,
				expectedTitle,
				null
		);
		Assertions.assertEquals(expectedPayload, managementService.editSourcePage(receivedPayload));
	}
}