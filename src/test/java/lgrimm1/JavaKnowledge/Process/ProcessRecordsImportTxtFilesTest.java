package lgrimm1.JavaKnowledge.Process;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.*;
import java.util.*;

import static org.mockito.Mockito.*;

class ProcessRecordsImportTxtFilesTest {

	ProcessRecords processRecords = new ProcessRecords();
	TitleRepository titleRepository;
	TxtRepository txtRepository;
	HtmlRepository htmlRepository;
	FileOperations fileOperations;
	Formulas formulas;
	Extractors extractors;
	ProcessPage processPage;

	@BeforeEach
	void setUp() {
		titleRepository = Mockito.mock(TitleRepository.class);
		txtRepository = Mockito.mock(TxtRepository.class);
		htmlRepository = Mockito.mock(HtmlRepository.class);
		fileOperations = Mockito.mock(FileOperations.class);
		formulas = Mockito.mock(Formulas.class);
		extractors = Mockito.mock(Extractors.class);
		processPage = Mockito.mock(ProcessPage.class);
	}

	@Test
	void importTxtFiles() {
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

		when(fileOperations.readFile(txtFile1))
				.thenReturn(List.of(
						"Line 11",
						"Line 12",
						"Line 13",
						"Line 14"
				));
		when(fileOperations.readFile(txtFile2))
				.thenReturn(List.of(
						"Line 21",
						"Line 22",
						"Line 23",
						"Line 24"
				));
		when(fileOperations.readFile(txtFile3))
				.thenReturn(List.of(
						"Line 31",
						"Line 32",
						"Line 33",
						"Line 34"
				));
		when(fileOperations.readFile(emptyTxtFile))
				.thenReturn(new ArrayList<>());

		when(extractors.extractTitle(List.of(
				"Line 11",
				"Line 12",
				"Line 13",
				"Line 14"
		), formulas))
				.thenReturn("Line 12");
		when(extractors.extractTitle(List.of(
				"Line 21",
				"Line 22",
				"Line 23",
				"Line 24"
		), formulas))
				.thenReturn("Line 22");
		when(extractors.extractTitle(List.of(
				"Line 31",
				"Line 32",
				"Line 33",
				"Line 34"
		), formulas))
				.thenReturn("");

		when(titleRepository.findByTitle("Line 12"))
				.thenReturn(Optional.of(new TitleEntity(1L, "Line 12", "line_12", 1L, 1L)));
		when(titleRepository.findByTitle("Line 22"))
				.thenReturn(Optional.of(new TitleEntity(2L, "Line 22", "line_22", 2L, 2L)));

		when(fileOperations.generateFilename("Line 12", titleRepository))
				.thenReturn("line_12");
		when(fileOperations.generateFilename("Line 22", titleRepository))
				.thenReturn("line_22");

		when(txtRepository.save(new TxtEntity("Line 14\n")))
				.thenReturn(new TxtEntity(2L, "Line 14\n"));
		when(txtRepository.save(new TxtEntity("Line 24\n")))
				.thenReturn(new TxtEntity(2L, "Line 24\n"));

		when(htmlRepository.save(new HtmlEntity(new ArrayList<>(), new ArrayList<>())))
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
				allFiles,
				titleRepository,
				txtRepository,
				htmlRepository,
				fileOperations,
				formulas,
				extractors));
	}
}