package lgrimm1.javaknowledge.services;

import lgrimm1.javaknowledge.databasestorage.*;
import lgrimm1.javaknowledge.datamodels.*;
import lgrimm1.javaknowledge.process.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;

class BrowsingServiceTest {

	DatabaseStorageService databaseStorageService;
	Formulas formulas;
	ProcessRecords processRecords;
	BrowsingService browsingService;

	@BeforeEach
	void setUp() {
		databaseStorageService = Mockito.mock(DatabaseStorageService.class);
		formulas = Mockito.mock(Formulas.class);
		processRecords = Mockito.mock(ProcessRecords.class);
		browsingService = new BrowsingService(
				databaseStorageService,
				processRecords,
				formulas);
		when(formulas.getTitleRoot())
				.thenReturn("RootTitle");
		when(formulas.getTitleManagement())
				.thenReturn("ManagementTitle");
		when(formulas.getTitleSource())
				.thenReturn("SourceTitle");
	}

	@Test
	void getRoot() {
		Payload expectedPayload = new Payload(
				formulas.getTitleRoot(),
				null,
				null,
				null,
				null,
				"",
				null,
				null,
				null
		);

		Assertions.assertEquals(expectedPayload, browsingService.getRoot());
	}

	@Test
	void searchPages_WrongPayload() {
		Exception e = Assertions.assertThrows(Exception.class, () -> browsingService.searchPages(null));
		Assertions.assertEquals("THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.",
				e.getMessage());

		Payload receivedPayload = new Payload(
				"templateTitle",
				null,
				null,
				null,
				"",
				null,
				null,
				null,
				List.of("Title 1")
		);
		e = Assertions.assertThrows(Exception.class, () -> browsingService.searchPages(receivedPayload));
		Assertions.assertEquals("THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.",
				e.getMessage());
	}

	@Test
	void searchPages_RightPayload() {
		String searchText = "  Word2 Word1  ";
		String correctedSearchText = "Word2 Word1";
		String trimmedSearchText = "Word2 Word1";
		Payload receivedPayload = new Payload(
				formulas.getTitleRoot(),
				null,
				null,
				null,
				null,
				searchText,
				null,
				null,
				null
		);

//		Set<String> titlesSet = Set.of("Title 2", "Title 1");
		List<String> titles = List.of("Title 1", "Title 2");
		when(databaseStorageService.findTitlesBySearchText(correctedSearchText))
				.thenReturn(titles);
		Payload expectedPayload = new Payload(
				formulas.getTitleList(),
				null,
				null,
				null,
				null,
				trimmedSearchText,
				null,
				null,
				titles
		);
	Assertions.assertEquals(expectedPayload, browsingService.searchPages(receivedPayload));
	}

	@Test
	void getPage_NullPayload() {
		Exception e = Assertions.assertThrows(Exception.class, () -> browsingService.getPage(null));
		Assertions.assertEquals("THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.",
				e.getMessage());
	}

	@Test
	void getPage_NullTitles() {
		Payload receivedPayload = new Payload(
				"templateTitle",
				null,
				null,
				null,
				null,
				"search text",
				null,
				null,
				null
		);
		Exception e = Assertions.assertThrows(Exception.class, () -> browsingService.getPage(receivedPayload));
		Assertions.assertEquals("THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.",
				e.getMessage());
	}

	@Test
	void getPage_EmptyTitles() {
		Payload receivedPayload = new Payload(
				"templateTitle",
				null,
				null,
				null,
				null,
				"search text",
				null,
				null,
				new ArrayList<>()
		);
		Exception e = Assertions.assertThrows(Exception.class, () -> browsingService.getPage(receivedPayload));
		Assertions.assertEquals("THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.",
				e.getMessage());
	}

	@Test
	void getPage_WrongPayload_MoreThanOneTitles() {
		Payload receivedPayload = new Payload(
				"templateTitle",
				null,
				null,
				null,
				null,
				"search text",
				null,
				null,
				List.of("Title 1", "Title 2")
		);
		Exception e = Assertions.assertThrows(Exception.class, () -> browsingService.getPage(receivedPayload));
		Assertions.assertEquals("THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.",
				e.getMessage());
	}

	@Test
	void getPage_WrongPayload_FirstTitlesIsNull() {
		List<String> titles = new ArrayList<>();
		titles.add(null);
		Payload receivedPayload = new Payload(
				"templateTitle",
				null,
				null,
				null,
				null,
				"search text",
				null,
				null,
				titles
		);
		Exception e = Assertions.assertThrows(Exception.class, () -> browsingService.getPage(receivedPayload));
		Assertions.assertEquals("THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.",
				e.getMessage());
	}

	@Test
	void getPage_WrongPayload_FirstTitlesIsBlank() {
		Payload receivedPayload = new Payload(
				"templateTitle",
				null,
				null,
				null,
				null,
				"search text",
				null,
				null,
				List.of("  ")
		);
		Exception e = Assertions.assertThrows(Exception.class, () -> browsingService.getPage(receivedPayload));
		Assertions.assertEquals("THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.",
				e.getMessage());
	}

	@Test
	void getPage_WrongPayload_NotExistingTitle() {
		String title = "Title 3";
		Payload receivedPayload = new Payload(
				"templateTitle",
				null,
				null,
				null,
				null,
				"search text",
				null,
				null,
				List.of(title)
		);
		when(databaseStorageService.findTitleByTitle(title))
				.thenReturn(Optional.empty());
		Exception e = Assertions.assertThrows(Exception.class, () -> browsingService.getPage(receivedPayload));
		Assertions.assertEquals("THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.",
				e.getMessage());
	}

	@Test
	void getPage_WrongPayload_NotExistingHtml() {
		String title = "Title 3";
		Payload receivedPayload = new Payload(
				"templateTitle",
				null,
				null,
				null,
				null,
				"search text",
				null,
				null,
				List.of(title)
		);
		when(databaseStorageService.findTitleByTitle(title))
				.thenReturn(Optional.of(new TitleEntity(1L, "Title 3", 1L, 1L)));
		when(databaseStorageService.findHtmlById(1L))
				.thenReturn(Optional.empty());
		Exception e = Assertions.assertThrows(Exception.class, () -> browsingService.getPage(receivedPayload));
		Assertions.assertEquals("THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.",
				e.getMessage());
	}

	@Test
	void getPage_RightPayload() {
		String title = "Title 3";
		List<String> contentList = List.of("content");
		String contentString = "content\n";
		List<String> titleReferences = List.of("Title 2");
		Payload receivedPayload = new Payload(
				"templateTitle",
				null,
				null,
				null,
				null,
				"search text",
				null,
				null,
				List.of(title)
		);
		when(databaseStorageService.findTitleByTitle(title))
				.thenReturn(Optional.of(new TitleEntity(1L, "Title 3", 1L, 1L)));
		when(databaseStorageService.findHtmlById(1L))
				.thenReturn(Optional.of(new HtmlEntity(1L, contentList, titleReferences)));
		when(processRecords.listToString(contentList))
				.thenReturn(contentString);
		Payload expectedPayload = new Payload(
				formulas.getTitlePage(),
				null,
				null,
				null,
				null,
				null,
				contentString,
				null,
				titleReferences
		);
		Assertions.assertEquals(expectedPayload, browsingService.getPage(receivedPayload));
	}
}