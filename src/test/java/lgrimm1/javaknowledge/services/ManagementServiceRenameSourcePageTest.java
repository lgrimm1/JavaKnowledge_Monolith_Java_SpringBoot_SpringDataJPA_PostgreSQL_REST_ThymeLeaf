package lgrimm1.javaknowledge.services;

import lgrimm1.javaknowledge.databasestorage.*;
import lgrimm1.javaknowledge.datamodels.*;
import lgrimm1.javaknowledge.process.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;

class ManagementServiceRenameSourcePageTest {
DatabaseStorageService databaseStorageService;
	Formulas formulas;
	ProcessRecords processRecords;
	ManagementService managementService;

	@BeforeEach
	void setUp() {
		formulas = Mockito.mock(Formulas.class);
		processRecords = Mockito.mock(ProcessRecords.class);
		databaseStorageService = Mockito.mock(DatabaseStorageService.class);
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
	void renameSourcePage_NullPayload() {
		Exception e = Assertions.assertThrows(Exception.class, () -> managementService.renameSourcePage(null));
		Assertions.assertEquals("PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.",
				e.getMessage());
	}

	@Test
	void renameSourcePage_NullTitles() {
		Exception e = Assertions.assertThrows(Exception.class, () -> managementService.renameSourcePage(null));
		Assertions.assertEquals("PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.",
				e.getMessage());
	}

	@Test
	void renameSourcePage_MoreThanOneTitles() {
		List<String> receivedTitles = List.of("Title 1", "Title 2");
		Payload receivedPayload = new Payload(
				"templateTitle",
				false,
				null,
				null,
				"",
				null,
				null,
				"Title 3",
				receivedTitles
		);
		Exception e = Assertions.assertThrows(Exception.class,
				() -> managementService.renameSourcePage(receivedPayload));
		Assertions.assertEquals("PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.",
				e.getMessage());
	}

	@Test
	void renameSourcePage_FirstTitleIsNull() {
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
				"Title 3",
				receivedTitles
		);
		Exception e = Assertions.assertThrows(Exception.class,
				() -> managementService.renameSourcePage(receivedPayload));
		Assertions.assertEquals("PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.",
				e.getMessage());
	}

	@Test
	void renameSourcePage_FirstTitleBlank() {
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
				"Title 3",
				receivedTitles
		);
		Exception e = Assertions.assertThrows(Exception.class,
				() -> managementService.renameSourcePage(receivedPayload));
		Assertions.assertEquals("PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.",
				e.getMessage());
	}

	@Test
	void renameSourcePage_NoSuchTitle() {
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
				"Title 4",
				receivedTitles
		);
		when(databaseStorageService.findTitleByTitle("Title 3"))
				.thenReturn(Optional.empty());
		Exception e = Assertions.assertThrows(Exception.class,
				() -> managementService.renameSourcePage(receivedPayload));
		Assertions.assertEquals("PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.",
				e.getMessage());
	}

	@Test
	void renameSourcePage_NullNewTitle() {
		List<String> receivedTitles = new ArrayList<>();
		receivedTitles.add("Title 1");
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"",
				null,
				null,
				null,
				receivedTitles
		);
		Exception e = Assertions.assertThrows(Exception.class,
				() -> managementService.renameSourcePage(receivedPayload));
		Assertions.assertEquals("PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.",
				e.getMessage());
	}

	@Test
	void renameSourcePage_BlankNewTitle() {
		List<String> receivedTitles = new ArrayList<>();
		receivedTitles.add("Title 1");
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"",
				null,
				null,
				"  ",
				receivedTitles
		);
		Exception e = Assertions.assertThrows(Exception.class,
				() -> managementService.renameSourcePage(receivedPayload));
		Assertions.assertEquals("PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.",
				e.getMessage());
	}

	@Test
	void renameSourcePage_AlreadyExistingNewTitle() {
		List<String> receivedTitles = new ArrayList<>();
		receivedTitles.add("Title 1");
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"",
				null,
				null,
				"Title 2",
				receivedTitles
		);
		when(databaseStorageService.findTitleByTitle(receivedTitles.get(0)))
				.thenReturn(Optional.of(new TitleEntity(3L, "Title 1", 2L, 4L)));
		when(databaseStorageService.findTitleByTitle("Title 2"))
				.thenReturn(Optional.of(new TitleEntity(3L, "Title 2", 2L, 4L)));
		Exception e = Assertions.assertThrows(Exception.class,
				() -> managementService.renameSourcePage(receivedPayload));
		Assertions.assertEquals("THE GIVEN NEW TITLE ALREADY EXISTS, PLEASE DEFINE AN OTHER ONE.",
				e.getMessage());
	}

	@Test
	void renameSourcePage_AllValid() {
		List<String> receivedTitles = new ArrayList<>();
		receivedTitles.add("Title 2");
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"",
				null,
				null,
				"Title 3",
				receivedTitles
		);

		when(databaseStorageService.findTitleByTitle(receivedTitles.get(0)))
				.thenReturn(Optional.of(new TitleEntity(3L, "Title 2", 2L, 4L)));

		when(databaseStorageService.findTitleByTitle("Title 3"))
				.thenReturn(Optional.empty());

		when(databaseStorageService.saveTitle(new TitleEntity(3L, "Title 3", 2L, 4L)))
				.thenReturn(new TitleEntity(3L, "Title 3", 2L, 4L));

		List<String> titles = List.of("Title 1", "Title 3");
		when(databaseStorageService.getAllTitles())
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PAGE HAS BEEN RENAMED.",
				null,
				null,
				"",
				titles
		);
		Assertions.assertEquals(expectedPayload, managementService.renameSourcePage(receivedPayload));
	}
}