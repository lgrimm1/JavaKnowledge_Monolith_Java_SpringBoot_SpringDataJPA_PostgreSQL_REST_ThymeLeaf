package lgrimm1.javaknowledge.process;

import lgrimm1.javaknowledge.databasestorage.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.*;
import java.util.*;

import static org.mockito.Mockito.*;

class ProcessRecordsImportTxtFilesTest {

	DatabaseStorageService databaseStorageService;
	FileOperations fileOperations;
	Extractors extractors;
	ProcessPage processPage;
	ProcessRecords processRecords;

	@BeforeEach
	void setUp() {
		databaseStorageService = Mockito.mock(DatabaseStorageService.class);
		fileOperations = Mockito.mock(FileOperations.class);
		extractors = Mockito.mock(Extractors.class);
		processPage = Mockito.mock(ProcessPage.class);
		processRecords = new ProcessRecords(databaseStorageService, fileOperations, extractors, processPage);
	}

	private File fileMocking(String filename, boolean exists, boolean isFile, boolean canRead) {
		File file = Mockito.mock(File.class);
		when(file.getName())
				.thenReturn(filename);
		when(file.exists())
				.thenReturn(exists);
		when(file.isFile())
				.thenReturn(isFile);
		when(file.canRead())
				.thenReturn(canRead);
		return file;
	}

	@Test
	void importTxtFiles_NotExistingFile_Folder_UnreadableFile() {
		File notExistingFile = fileMocking("notExistingFileName", false, false, false);
		File folder = fileMocking("notFileFileName", true, false, false);
		File unreadableFile = fileMocking("unreadableFileName", true, true, false);
		List<File> files = new ArrayList<>();
		files.add(notExistingFile);
		files.add(folder);
		files.add(unreadableFile);
		Assertions.assertEquals(files, processRecords.importTxtFiles(files));
	}

	@Test
	void importTxtFiles_EmptyFile_FileWithoutTitle() {
		File emptyTxtFile = fileMocking("emptyTxtFileName", true, true, true);
		when(fileOperations.readFile(emptyTxtFile))
				.thenReturn(new ArrayList<>());

		File txtFileWithoutTitle = fileMocking("txtFileName", true, true, true);
		List<String> txtFileWithoutTitleContent = List.of(
				"Line 51",
				"Line 52",
				"Line 53",
				"Line 54"
		);
		when(fileOperations.readFile(txtFileWithoutTitle))
				.thenReturn(txtFileWithoutTitleContent);
		when(extractors.extractTitle(txtFileWithoutTitleContent))
				.thenReturn("");

		List<File> files = new ArrayList<>();
		files.add(emptyTxtFile);
		files.add(txtFileWithoutTitle);
		Assertions.assertEquals(files, processRecords.importTxtFiles(files));
	}

	@Test
	void importTxtFiles_ExistingTitleAndExistingTxtAndExistingHtml() {
		File txtFile = fileMocking("txtFileName", true, true, true);
		List<File> files = new ArrayList<>();
		files.add(txtFile);
		String title = "title";
		List<String> txtFileContent = List.of(
				"Line 1",
				title,
				"Line 3",
				"Line 4"
		);
		String txtColumnContent = "Line 4\n";
		TxtEntity originalTxtEntity = new TxtEntity(2L, "original txt content");
		TxtEntity givenTxtEntity = new TxtEntity(2L, txtColumnContent);
		TxtEntity savedTxtEntity = new TxtEntity(2L, txtColumnContent);
		HtmlEntity originalHtmlEntity = new HtmlEntity(
				3L,
				"original html content",
				"original references");
		TitleEntity originalTitleEntity = new TitleEntity(1L, title, 2L, 3L);

		when(fileOperations.readFile(txtFile))
				.thenReturn(txtFileContent);
		when(extractors.extractTitle(txtFileContent))
				.thenReturn(title);
		when(databaseStorageService.findTitleByTitle(title))
				.thenReturn(Optional.of(originalTitleEntity));
		when(databaseStorageService.findTxtById(2L))
				.thenReturn(Optional.of(originalTxtEntity));
		when(databaseStorageService.findHtmlById(3L))
				.thenReturn(Optional.of(originalHtmlEntity));
		when(databaseStorageService.saveTxt(givenTxtEntity))
				.thenReturn(savedTxtEntity);

		Assertions.assertTrue(processRecords.importTxtFiles(files).isEmpty());

		when(databaseStorageService.saveTxt(givenTxtEntity))
				.thenThrow(new RuntimeException("txt saved"));
		Exception e = Assertions.assertThrows(Exception.class, () -> processRecords.importTxtFiles(files));
		Assertions.assertEquals("txt saved", e.getMessage());
	}

	@Test
	void importTxtFiles_ExistingTitleAndExistingTxtAndNotExistingHtml() {
		File txtFile = fileMocking("txtFileName", true, true, true);
		List<File> files = new ArrayList<>();
		files.add(txtFile);
		String title = "title";
		List<String> txtFileContent = List.of(
				"Line 1",
				title,
				"Line 3",
				"Line 4"
		);
		String txtColumnContent = "Line 4\n";
		TxtEntity originalTxtEntity = new TxtEntity(2L, "original txt content");
		TxtEntity givenTxtEntity = new TxtEntity(2L, txtColumnContent);
		TxtEntity savedTxtEntity = new TxtEntity(2L, txtColumnContent);
		HtmlEntity givenHtmlEntity = new HtmlEntity("", "");
		HtmlEntity savedHtmlEntity = new HtmlEntity(5L, "", "");
		TitleEntity originalTitleEntity = new TitleEntity(1L, title, 2L, 3L);
		TitleEntity givenTitleEntity = new TitleEntity(1L, title, 2L, 5L);

		when(fileOperations.readFile(txtFile))
				.thenReturn(txtFileContent);
		when(extractors.extractTitle(txtFileContent))
				.thenReturn(title);
		when(databaseStorageService.findTitleByTitle(title))
				.thenReturn(Optional.of(originalTitleEntity));
		when(databaseStorageService.findTxtById(2L))
				.thenReturn(Optional.of(originalTxtEntity));
		when(databaseStorageService.findHtmlById(3L))
				.thenReturn(Optional.empty());
		when(databaseStorageService.saveTxt(givenTxtEntity))
				.thenReturn(savedTxtEntity);
		when(databaseStorageService.saveHtml(givenHtmlEntity))
				.thenReturn(savedHtmlEntity);

		Assertions.assertTrue(processRecords.importTxtFiles(files).isEmpty());

		when(databaseStorageService.saveTitle(givenTitleEntity))
				.thenThrow(new RuntimeException("title saved"));
		Exception e = Assertions.assertThrows(Exception.class, () -> processRecords.importTxtFiles(files));
		Assertions.assertEquals("title saved", e.getMessage());
	}

	@Test
	void importTxtFiles_ExistingTitleAndNotExistingTxtAndExistingHtml() {
		File txtFile = fileMocking("txtFileName", true, true, true);
		List<File> files = new ArrayList<>();
		files.add(txtFile);
		String title = "title";
		List<String> txtFileContent = List.of(
				"Line 1",
				title,
				"Line 3",
				"Line 4"
		);
		String txtColumnContent = "Line 4\n";
		TxtEntity givenTxtEntity = new TxtEntity(txtColumnContent);
		TxtEntity savedTxtEntity = new TxtEntity(4L, txtColumnContent);
		HtmlEntity originalHtmlEntity = new HtmlEntity(
				3L,
				"original html content",
				"original references");
		TitleEntity originalTitleEntity = new TitleEntity(1L, title, 2L, 3L);
		TitleEntity givenTitleEntity = new TitleEntity(1L, title, 4L, 3L);

		when(fileOperations.readFile(txtFile))
				.thenReturn(txtFileContent);
		when(extractors.extractTitle(txtFileContent))
				.thenReturn(title);
		when(databaseStorageService.findTitleByTitle(title))
				.thenReturn(Optional.of(originalTitleEntity));
		when(databaseStorageService.findTxtById(2L))
				.thenReturn(Optional.empty());
		when(databaseStorageService.findHtmlById(3L))
				.thenReturn(Optional.of(originalHtmlEntity));
		when(databaseStorageService.saveTxt(givenTxtEntity))
				.thenReturn(savedTxtEntity);

		Assertions.assertTrue(processRecords.importTxtFiles(files).isEmpty());

		when(databaseStorageService.saveTitle(givenTitleEntity))
				.thenThrow(new RuntimeException("title saved"));
		Exception e = Assertions.assertThrows(Exception.class, () -> processRecords.importTxtFiles(files));
		Assertions.assertEquals("title saved", e.getMessage());
	}

	@Test
	void importTxtFiles_NotExistingTitleAndNotExistingTxtAndNotExistingHtml() {
		File txtFile = fileMocking("txtFileName", true, true, true);
		List<File> files = new ArrayList<>();
		files.add(txtFile);
		String title = "title";
		List<String> txtFileContent = List.of(
				"Line 1",
				title,
				"Line 3",
				"Line 4"
		);
		String txtColumnContent = "Line 4\n";
		TxtEntity givenTxtEntity = new TxtEntity(txtColumnContent);
		TxtEntity savedTxtEntity = new TxtEntity(2L, txtColumnContent);
		HtmlEntity givenHtmlEntity = new HtmlEntity("", "");
		HtmlEntity savedHtmlEntity = new HtmlEntity(3L, "", "");
		TitleEntity givenTitleEntity = new TitleEntity(title, 2L, 3L);

		when(fileOperations.readFile(txtFile))
				.thenReturn(txtFileContent);
		when(extractors.extractTitle(txtFileContent))
				.thenReturn(title);
		when(databaseStorageService.findTitleByTitle(title))
				.thenReturn(Optional.empty());
		when(databaseStorageService.saveTxt(givenTxtEntity))
				.thenReturn(savedTxtEntity);
		when(databaseStorageService.saveHtml(givenHtmlEntity))
				.thenReturn(savedHtmlEntity);

		Assertions.assertTrue(processRecords.importTxtFiles(files).isEmpty());

		when(databaseStorageService.saveTitle(givenTitleEntity))
				.thenThrow(new RuntimeException("title saved"));
		Exception e = Assertions.assertThrows(Exception.class, () -> processRecords.importTxtFiles(files));
		Assertions.assertEquals("title saved", e.getMessage());
	}
}