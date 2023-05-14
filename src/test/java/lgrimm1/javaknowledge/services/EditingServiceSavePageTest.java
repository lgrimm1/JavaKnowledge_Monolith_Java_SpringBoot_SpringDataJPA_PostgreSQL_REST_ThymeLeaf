package lgrimm1.javaknowledge.services;

import lgrimm1.javaknowledge.databasestorage.*;
import lgrimm1.javaknowledge.datamodels.*;
import lgrimm1.javaknowledge.process.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;

class EditingServiceSavePageTest {

	DatabaseStorageService databaseStorageService;
	Formulas formulas;
	ProcessRecords processRecords;
	EditingService editingService;

	@BeforeEach
	void setUp() {
		databaseStorageService = Mockito.mock(DatabaseStorageService.class);
		formulas = Mockito.mock(Formulas.class);
		processRecords = Mockito.mock(ProcessRecords.class);
		editingService = new EditingService(
				databaseStorageService,
				processRecords,
				formulas);
		when(formulas.getTitleSource())
				.thenReturn("SourceTitle");
		when(formulas.getTitleManagement())
				.thenReturn("ManagementTitle");
	}

	@Test
	void savePage_NullPayload() {
		Exception e = Assertions.assertThrows(Exception.class, () -> editingService.savePage(null));
		Assertions.assertEquals("THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.",
				e.getMessage());
	}

	@Test
	void savePage_NullTitle() {
		String content = "Line 1\nLine 2\n";
		boolean editExistingPage = true;
		String message = "";
		Payload receivedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				content,
				editExistingPage,
				message,
				null,
				null,
				null,
				null
		);

		Payload expectedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				content,
				editExistingPage,
				"PLEASE DEFINE A TITLE.",
				null,
				null,
				"",
				null
		);
		Assertions.assertEquals(expectedPayload, editingService.savePage(receivedPayload));
	}

	@Test
	void savePage_BlankTitle() {
		String content = "Line 1\nLine 2\n";
		boolean editExistingPage = true;
		String message = "";
		String title = "  ";
		Payload receivedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				content,
				editExistingPage,
				message,
				null,
				null,
				title,
				null
		);

		Payload expectedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				content,
				editExistingPage,
				"PLEASE DEFINE A TITLE.",
				null,
				null,
				"",
				null
		);
		Assertions.assertEquals(expectedPayload, editingService.savePage(receivedPayload));
	}

	@Test
	void savePage_Edit_NullContent_NonExistent() {
		boolean editExistingPage = true;
		String message = "";
		String title = "Title 1";
		Payload receivedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				null,
				editExistingPage,
				message,
				null,
				null,
				title,
				null
		);

		when(databaseStorageService.findTitleByTitle(title))
				.thenReturn(Optional.empty());

		Payload expectedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				"",
				editExistingPage,
				"THERE IS NO EXISTING PAGE WITH THIS TITLE.",
				null,
				null,
				title,
				null
		);
		Assertions.assertEquals(expectedPayload, editingService.savePage(receivedPayload));
	}

	@Test
	void savePage_Edit_NonExistent() {
		String content = "Line 1\nLine 2\n";
		boolean editExistingPage = true;
		String message = "";
		String title = "Title 1";
		Payload receivedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				content,
				editExistingPage,
				message,
				null,
				null,
				title,
				null
		);

		when(databaseStorageService.findTitleByTitle(title))
				.thenReturn(Optional.empty());

		Payload expectedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				content,
				editExistingPage,
				"THERE IS NO EXISTING PAGE WITH THIS TITLE.",
				null,
				null,
				title,
				null
		);
		Assertions.assertEquals(expectedPayload, editingService.savePage(receivedPayload));
	}

	@Test
	void savePage_Edit_Existent() {
		String content = "Line 1\nLine 2\n";
		boolean editExistingPage = true;
		String message = "";
		String title = "Title 1";
		Payload receivedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				content,
				editExistingPage,
				message,
				null,
				null,
				title,
				null
		);

		when(databaseStorageService.findTitleByTitle(title))
				.thenReturn(Optional.of(new TitleEntity(2L, "Title 1", 3L, 4L)));
		when(databaseStorageService.findTxtById(3L))
				.thenReturn(Optional.of(new TxtEntity(3L, "original content")));
		when(databaseStorageService.saveTxt(new TxtEntity(3L, content)))
				.thenReturn(new TxtEntity(3L, content));

		Payload expectedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				content,
				editExistingPage,
				"SOURCE PAGE HAS BEEN OVERWRITTEN.",
				null,
				null,
				title,
				null
		);
		Assertions.assertEquals(expectedPayload, editingService.savePage(receivedPayload));
	}

	@Test
	void savePage_New_Existent() {
		String content = "Line 1\nLine 2\n";
		boolean editExistingPage = false;
		String message = "";
		String title = "Title 1";
		Payload receivedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				content,
				editExistingPage,
				message,
				null,
				null,
				title,
				null
		);

		when(databaseStorageService.findTitleByTitle(title))
				.thenReturn(Optional.of(new TitleEntity(2L, "Title 1", 3L, 4L)));

		Payload expectedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				content,
				editExistingPage,
				"THERE IS AN EXISTING PAGE WITH THIS TITLE.",
				null,
				null,
				title,
				null
		);
		Assertions.assertEquals(expectedPayload, editingService.savePage(receivedPayload));
	}

	@Test
	void savePage_New_NonExistent() {
		String content = "Line 1\nLine 2\n";
		boolean editExistingPage = false;
		String message = "";
		String title = "Title 1";
		Payload receivedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				content,
				editExistingPage,
				message,
				null,
				null,
				title,
				null
		);

		when(databaseStorageService.findTitleByTitle(title))
				.thenReturn(Optional.empty());
		when(databaseStorageService.saveHtml(new HtmlEntity("", "")))
				.thenReturn(new HtmlEntity(3L, "", ""));
		when(databaseStorageService.saveTxt(new TxtEntity(content)))
				.thenReturn(new TxtEntity(4L, content));

		List<String> titles = List.of("Title 1", "Title 2");
		when(databaseStorageService.getAllTitles())
				.thenReturn(titles);
		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"SOURCE PAGE HAS BEEN SAVED.",
				null,
				null,
				"",
				titles
		);
		Assertions.assertEquals(expectedPayload, editingService.savePage(receivedPayload));
	}

	@Test
	void savePage_NullEditExistingPage_Existent() {
		String content = "Line 1\nLine 2\n";
		String message = "";
		String title = "Title 1";
		Payload receivedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				content,
				null,
				message,
				null,
				null,
				title,
				null
		);

		when(databaseStorageService.findTitleByTitle(title))
				.thenReturn(Optional.of(new TitleEntity(2L, title, 3L, 4L)));

		Payload expectedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				content,
				false,
				"THERE IS AN EXISTING PAGE WITH THIS TITLE.",
				null,
				null,
				title,
				null
		);
		Assertions.assertEquals(expectedPayload, editingService.savePage(receivedPayload));
	}
}