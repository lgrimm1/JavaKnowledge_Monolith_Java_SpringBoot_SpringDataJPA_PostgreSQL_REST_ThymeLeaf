package lgrimm1.javaknowledge.process;

import lgrimm1.javaknowledge.databasestorage.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.*;
import java.util.*;

import static org.mockito.Mockito.*;

class ProcessRecordsImportTxtFilesTest {

/*
	TitleRepository titleRepository;
	TxtRepository txtRepository;
	HtmlRepository htmlRepository;
*/
	DatabaseStorageService databaseStorageService;
	FileOperations fileOperations;
	Extractors extractors;
	ProcessPage processPage;
	ProcessRecords processRecords;

	@BeforeEach
	void setUp() {
/*
		titleRepository = Mockito.mock(TitleRepository.class);
		txtRepository = Mockito.mock(TxtRepository.class);
		htmlRepository = Mockito.mock(HtmlRepository.class);
*/
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
/*
		String notExistingFileName = "notExistingFileName";
		File notExistingFile = Mockito.mock(File.class);
		when(notExistingFile.getName())
				.thenReturn(notExistingFileName);
		when(notExistingFile.exists())
				.thenReturn(false);
		when(notExistingFile.isFile())
				.thenReturn(false);
		when(notExistingFile.canRead())
				.thenReturn(false);
*/
		File folder = fileMocking("notFileFileName", true, false, false);
/*
		String notFileFileName = "notFileFileName";
		File notFileFile = Mockito.mock(File.class);
		when(notFileFile.getName())
				.thenReturn(notFileFileName);
		when(notFileFile.exists())
				.thenReturn(true);
		when(notFileFile.isFile())
				.thenReturn(false);
		when(notFileFile.canRead())
				.thenReturn(false);
*/
		File unreadableFile = fileMocking("unreadableFileName", true, true, false);
/*
		String unreadableFileName = "unreadableFileName";
		File unreadableFile = Mockito.mock(File.class);
		when(unreadableFile.getName())
				.thenReturn(unreadableFileName);
		when(unreadableFile.exists())
				.thenReturn(true);
		when(unreadableFile.isFile())
				.thenReturn(true);
		when(unreadableFile.canRead())
				.thenReturn(false);
*/

		List<File> files = new ArrayList<>();
		files.add(notExistingFile);
		files.add(folder);
		files.add(unreadableFile);
		Assertions.assertEquals(files, processRecords.importTxtFiles(files));
	}

	@Test
	void importTxtFiles_EmptyFile_FileWithoutTitle() {
		File emptyTxtFile = fileMocking("emptyTxtFileName", true, true, true);
/*
		String emptyTxtFileName = "emptyTxtFileName";
		File emptyTxtFile = Mockito.mock(File.class);
		when(emptyTxtFile.getName())
				.thenReturn(emptyTxtFileName);
		when(emptyTxtFile.exists())
				.thenReturn(true);
		when(emptyTxtFile.isFile())
				.thenReturn(true);
		when(emptyTxtFile.canRead())
				.thenReturn(true);
*/
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
		File txtFile = fileMocking("txtFile1Name", true, true, true);
		List<String> txtFileContent = List.of(
				"Line 11",
				"Line 12",
				"Line 13",
				"Line 14"
		);
		when(fileOperations.readFile(txtFile))
				.thenReturn(txtFileContent);
		when(extractors.extractTitle(txtFileContent))
				.thenReturn("Line 12");
		when(databaseStorageService.findTitleByTitle("Line 12"))
				.thenReturn(Optional.of(new TitleEntity(1L, "Line 12", "line_12", 2L, 3L)));
		when(databaseStorageService.findTxtById(2L))
				.thenReturn(Optional.of(new TxtEntity(2L, "original content")));
		when(databaseStorageService.findHtmlById(3L))
				.thenReturn(Optional.of(new HtmlEntity(3L, List.of("original html content"), List.of("original html references"))));
		String txtColumnContent = "Line 11\nLine 12\nLine 13\nLine 14";
		TxtEntity newTxtEntity = new TxtEntity(2L, txtColumnContent);
		when(databaseStorageService.saveTxt(newTxtEntity))
				.thenReturn(newTxtEntity);
		List<File> files = new ArrayList<>();
		files.add(txtFile);

		Assertions.assertTrue(processRecords.importTxtFiles(files).isEmpty());

		when(databaseStorageService.saveTxt(newTxtEntity))
				.thenThrow(new RuntimeException("txt saved"));
		Exception e = Assertions.assertThrows(Exception.class, () -> processRecords.importTxtFiles(files));
		Assertions.assertEquals("txt saved", e.getMessage());
	}

	@Test
	void importTxtFiles_ExistingTitleAndExistingTxtAndNotExistingHtml() {
		File txtFile = fileMocking("txtFileName", true, true, true);
		List<String> txtFileContent = List.of(
				"Line 11",
				"Line 12",
				"Line 13",
				"Line 14"
		);
		when(fileOperations.readFile(txtFile))
				.thenReturn(txtFileContent);
		when(extractors.extractTitle(txtFileContent))
				.thenReturn("Line 12");
		when(databaseStorageService.findTitleByTitle("Line 12"))
				.thenReturn(Optional.of(new TitleEntity(1L, "Line 12", "line_12", 2L, 3L)));
		when(databaseStorageService.findTxtById(2L))
				.thenReturn(Optional.of(new TxtEntity(2L, "original content")));
		when(databaseStorageService.findHtmlById(3L))
				.thenReturn(Optional.empty());
		String txtColumnContent = "Line 11\nLine 12\nLine 13\nLine 14";
		TxtEntity newTxtEntity = new TxtEntity(2L, txtColumnContent);
		when(databaseStorageService.saveTxt(newTxtEntity))
				.thenReturn(newTxtEntity);
		when(databaseStorageService.saveHtml(new HtmlEntity(new ArrayList<>(), new ArrayList<>())))
				.thenReturn(new HtmlEntity(4L, new ArrayList<>(), new ArrayList<>()));
		List<File> files = new ArrayList<>();
		files.add(txtFile);

		Assertions.assertTrue(processRecords.importTxtFiles(files).isEmpty());

		when(databaseStorageService.saveTxt(newTxtEntity))
				.thenThrow(new RuntimeException("txt saved"));
		Exception e = Assertions.assertThrows(Exception.class, () -> processRecords.importTxtFiles(files));
		Assertions.assertEquals("txt saved", e.getMessage());

		when(databaseStorageService.saveTxt(newTxtEntity))
				.thenReturn(newTxtEntity);
		when(databaseStorageService.saveTitle(new TitleEntity(1L, "Line 12", "line_12", 2L, 4L)))
				.thenThrow(new RuntimeException("title saved"));
		e = Assertions.assertThrows(Exception.class, () -> processRecords.importTxtFiles(files));
		Assertions.assertEquals("title saved", e.getMessage());
	}

	@Test
	void importTxtFiles_ExistingTitleAndNotExistingTxtAndExistingHtml() {
		File txtFile = fileMocking("txtFileName", true, true, true);
		List<String> txtFileContent = List.of(
				"Line 11",
				"Line 12",
				"Line 13",
				"Line 14"
		);
		when(fileOperations.readFile(txtFile))
				.thenReturn(txtFileContent);
		when(extractors.extractTitle(txtFileContent))
				.thenReturn("Line 12");
		when(databaseStorageService.findTitleByTitle("Line 12"))
				.thenReturn(Optional.of(new TitleEntity(1L, "Line 12", "line_12", 2L, 3L)));
		when(databaseStorageService.findTxtById(2L))
				.thenReturn(Optional.empty());
		when(databaseStorageService.findHtmlById(3L))
				.thenReturn(Optional.of(new HtmlEntity(3L, List.of("original html content"), List.of("original references"))));
		String txtColumnContent = "Line 11\nLine 12\nLine 13\nLine 14";
		TxtEntity newTxtEntity = new TxtEntity(4L, txtColumnContent);
		when(databaseStorageService.saveTxt(new TxtEntity(txtColumnContent)))
				.thenReturn(newTxtEntity);
		List<File> files = new ArrayList<>();
		files.add(txtFile);

		Assertions.assertTrue(processRecords.importTxtFiles(files).isEmpty());

		when(databaseStorageService.saveTxt(newTxtEntity))
				.thenThrow(new RuntimeException("txt saved"));
		Exception e = Assertions.assertThrows(Exception.class, () -> processRecords.importTxtFiles(files));
		Assertions.assertEquals("txt saved", e.getMessage());

		when(databaseStorageService.saveTxt(new TxtEntity(txtColumnContent)))
				.thenReturn(newTxtEntity);
		when(databaseStorageService.saveTitle(new TitleEntity(1L, "Line 12", "line_12", 4L, 3L)))
				.thenThrow(new RuntimeException("title saved"));
		e = Assertions.assertThrows(Exception.class, () -> processRecords.importTxtFiles(files));
		Assertions.assertEquals("title saved", e.getMessage());
	}

	@Test
	void importTxtFiles_NotExistingTitleAndNotExistingTxtAndNotExistingHtml() {
		File txtFile = fileMocking("txtFileName", true, true, true);
		List<String> txtFileContent = List.of(
				"Line 11",
				"Line 12",
				"Line 13",
				"Line 14"
		);
		when(fileOperations.readFile(txtFile))
				.thenReturn(txtFileContent);
		when(extractors.extractTitle(txtFileContent))
				.thenReturn("Line 12");
		when(databaseStorageService.findTitleByTitle("Line 12"))
				.thenReturn(Optional.empty());
		String txtColumnContent = "Line 11\nLine 12\nLine 13\nLine 14";
		TxtEntity newTxtEntity = new TxtEntity(2L, txtColumnContent);
		when(databaseStorageService.saveTxt(new TxtEntity(txtColumnContent)))
				.thenReturn(newTxtEntity);
		when(databaseStorageService.saveHtml(new HtmlEntity(new ArrayList<>(), new ArrayList<>())))
				.thenReturn(new HtmlEntity(3L, new ArrayList<>(), new ArrayList<>()));
		List<File> files = new ArrayList<>();
		files.add(txtFile);

		Assertions.assertTrue(processRecords.importTxtFiles(files).isEmpty());

		when(databaseStorageService.saveTitle(new TitleEntity("Line 12", "line_12", 2L, 3L)))
				.thenThrow(new RuntimeException("title saved"));
		Exception e = Assertions.assertThrows(Exception.class, () -> processRecords.importTxtFiles(files));
		Assertions.assertEquals("title saved", e.getMessage());
	}

/*
	@Test
	void importTxtFiles() {
		File txtFile1 = fileMocking("txtFile1Name", true, true, true);
*/
/*
		String txtFile1Name = "txtFile1Name";
		File txtFile1 = Mockito.mock(File.class);
		when(txtFile1.getName())
				.thenReturn(txtFile1Name);
		when(txtFile1.exists())
				.thenReturn(true);
		when(txtFile1.isFile())
				.thenReturn(true);
		when(txtFile1.canRead())
				.thenReturn(true);
*/
//		File txtFile2 = fileMocking("txtFile2Name", true, true, true);
/*
		String txtFile2Name = "txtFile2Name";
		File txtFile2 = Mockito.mock(File.class);
		when(txtFile1.getName())
				.thenReturn(txtFile2Name);
		when(txtFile2.exists())
				.thenReturn(true);
		when(txtFile2.isFile())
				.thenReturn(true);
		when(txtFile2.canRead())
				.thenReturn(true);
*/
//		File txtFile3 = fileMocking("txtFile3Name", true, true, true);
/*
		String txtFile3Name = "txtFile3Name";
		File txtFile3 = Mockito.mock(File.class);
		when(txtFile1.getName())
				.thenReturn(txtFile3Name);
		when(txtFile3.exists())
				.thenReturn(true);
		when(txtFile3.isFile())
				.thenReturn(true);
		when(txtFile3.canRead())
				.thenReturn(true);
*/
//		File txtFile4 = fileMocking("txtFile4Name", true, true, true);

/*
		List<String> txtFileContent1 = List.of(
				"Line 11",
				"Line 12",
				"Line 13",
				"Line 14"
		);
		List<String> txtFileContent2 = List.of(
				"Line 21",
				"Line 22",
				"Line 23",
				"Line 24"
		);
		List<String> txtFileContent3 = List.of(
				"Line 31",
				"Line 32",
				"Line 33",
				"Line 34"
		);
		List<String> txtFileContent4 = List.of(
				"Line 41",
				"Line 42",
				"Line 43",
				"Line 44"
		);
		when(fileOperations.readFile(txtFile1))
				.thenReturn(txtFileContent1);
		when(fileOperations.readFile(txtFile2))
				.thenReturn(txtFileContent2);
		when(fileOperations.readFile(txtFile3))
				.thenReturn(txtFileContent3);
		when(fileOperations.readFile(txtFile4))
				.thenReturn(txtFileContent4);

		when(extractors.extractTitle(txtFileContent1))
				.thenReturn("Line 12");
		when(extractors.extractTitle(txtFileContent2))
				.thenReturn("Line 22");
		when(extractors.extractTitle(txtFileContent3))
				.thenReturn("Line 32");
		when(extractors.extractTitle(txtFileContent4))
				.thenReturn("Line 42");

		when(databaseStorageService.findTitleByTitle("Line 12"))
				.thenReturn(Optional.of(new TitleEntity(1L, "Line 12", "line_12", 1L, 1L)));
		when(databaseStorageService.findTitleByTitle("Line 22"))
				.thenReturn(Optional.of(new TitleEntity(2L, "Line 22", "line_22", 2L, 2L)));
		when(databaseStorageService.findTitleByTitle("Line 32"))
				.thenReturn(Optional.of(new TitleEntity(3L, "Line 32", "line_32", 3L, 3L)));
		when(databaseStorageService.findTitleByTitle("Line 42"))
				.thenReturn(Optional.empty());
*/

/*
		when(fileOperations.generateFilename("Line 12", titleRepository))
				.thenReturn("line_12");
		when(fileOperations.generateFilename("Line 22", titleRepository))
				.thenReturn("line_22");
*/
/*

		when(databaseStorageService.saveTxt(new TxtEntity("Line 14\n")))
				.thenReturn(new TxtEntity(2L, "Line 14\n"));
		when(databaseStorageService.saveTxt(new TxtEntity("Line 24\n")))
				.thenReturn(new TxtEntity(2L, "Line 24\n"));

		when(databaseStorageService.saveHtml(new HtmlEntity(new ArrayList<>(), new ArrayList<>())))
				.thenReturn(new HtmlEntity(1L, new ArrayList<>(), new ArrayList<>()));

		List<File> notImportedFiles = new ArrayList<>();
		notImportedFiles.add(notExistingFile);
		notImportedFiles.add(notFileFile);
		notImportedFiles.add(unreadableFile);
		notImportedFiles.add(emptyTxtFile);
		notImportedFiles.add(txtFile3);

		List<File> allFiles = new ArrayList<>();
		allFiles.add(txtFile1);
		allFiles.add(txtFile2);
		allFiles.addAll(notImportedFiles);

		Assertions.assertEquals(notImportedFiles, processRecords.importTxtFiles(
				allFiles));
*/
/*
		Assertions.assertEquals(notImportedFiles, processRecords.importTxtFiles(
				allFiles,
				titleRepository,
				txtRepository,
				htmlRepository,
				fileOperations,
				formulas,
				extractors));
*/
//	}
}