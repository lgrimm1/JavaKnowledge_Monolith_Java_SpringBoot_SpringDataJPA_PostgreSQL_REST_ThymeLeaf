package lgrimm1.javaknowledge.common;

import lgrimm1.javaknowledge.html.*;
import lgrimm1.javaknowledge.process.*;
import lgrimm1.javaknowledge.title.*;
import lgrimm1.javaknowledge.txt.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

import static org.mockito.Mockito.*;

class ManagementServiceImportTxtTest {

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
	String filename2, filename3, filename4;
	Path path2, path3, path4;
	Stream<Path> paths;
	File file2, file3, file4;
	List<File> uploadedFiles, notImportedFiles;
	long[] uploadResults;
	List<String> titlesBeforeUploading, titlesAfterUploading;

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
		titlesBeforeUploading = List.of("Title 1");
		titlesAfterUploading = List.of("Title 1", "Title 3");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titlesBeforeUploading);
		filename2 = "file2.txt";
		filename3 = "file3.txt";
		filename4 = "file4.txt";
		path2 = Path.of(filename2);
		path3 = Path.of(filename3);
		path4 = Path.of(filename4);
		paths = Stream.of(path2, path3, path4);
		file2 = path2.toFile();
		file3 = path3.toFile();
		file4 = path4.toFile();
		uploadedFiles = List.of(file2, file3, file4);
		notImportedFiles = List.of(file2);
		uploadResults = new long[]{3, 3};
	}

	@Test
	void importTxt_NullPayload() {
		Exception e = Assertions.assertThrows(Exception.class,
				() -> managementService.importTxt(null, paths, uploadResults));
		Assertions.assertEquals("PLEASE UPLOAD MINIMUM ONE PROPER FILE AND CONFIRM SOURCE OVERWRITING.",
				e.getMessage());
	}

	@Test
	void importTxt_NullConfirm() {
		Payload receivedPayload = new Payload(
				"templateTitle",
				null,
				null,
				null,
				"",
				null,
				null,
				"",
				titlesBeforeUploading
		);
		Exception e = Assertions.assertThrows(Exception.class,
				() -> managementService.importTxt(receivedPayload, paths, uploadResults));
		Assertions.assertEquals("PLEASE UPLOAD MINIMUM ONE PROPER FILE AND CONFIRM SOURCE OVERWRITING.",
				e.getMessage());
	}

	@Test
	void importTxt_NotConfirmed() {
		Payload receivedPayload = new Payload(
				"templateTitle",
				false,
				null,
				null,
				"",
				null,
				null,
				"",
				titlesBeforeUploading
		);
		Exception e = Assertions.assertThrows(Exception.class,
				() -> managementService.importTxt(receivedPayload, paths, uploadResults));
		Assertions.assertEquals("PLEASE UPLOAD MINIMUM ONE PROPER FILE AND CONFIRM SOURCE OVERWRITING.",
				e.getMessage());
	}

	@Test
	void importTxt_NullUploadResults() {
		Payload receivedPayload = new Payload(
				"templateTitle",
				true,
				null,
				null,
				"",
				null,
				null,
				"",
				titlesBeforeUploading
		);
		Exception e = Assertions.assertThrows(Exception.class,
				() -> managementService.importTxt(receivedPayload, paths, null));
		Assertions.assertEquals("PLEASE UPLOAD MINIMUM ONE PROPER FILE AND CONFIRM SOURCE OVERWRITING.",
				e.getMessage());
	}

	@Test
	void importTxt_WrongUploadResults() {
		Payload receivedPayload = new Payload(
				"templateTitle",
				true,
				null,
				null,
				"",
				null,
				null,
				"",
				titlesBeforeUploading
		);
		uploadResults = new long[]{1L, 2L, 3L};
		Exception e = Assertions.assertThrows(Exception.class,
				() -> managementService.importTxt(receivedPayload, paths, uploadResults));
		Assertions.assertEquals("PLEASE UPLOAD MINIMUM ONE PROPER FILE AND CONFIRM SOURCE OVERWRITING.",
				e.getMessage());
	}

	@Test
	void importTxt_Confirmed_NoUploadedFiles() {
		Payload receivedPayload = new Payload(
				"templateTitle",
				true,
				null,
				null,
				"",
				null,
				null,
				"",
				titlesBeforeUploading
		);
		uploadResults = new long[]{1L, 0};
		Exception e = Assertions.assertThrows(Exception.class,
				() -> managementService.importTxt(receivedPayload, paths, uploadResults));
		Assertions.assertEquals("PLEASE UPLOAD MINIMUM ONE PROPER FILE AND CONFIRM SOURCE OVERWRITING.",
				e.getMessage());
	}

	@Test
	void importTxt_Confirmed_ExistingUploadedFiles() {
		Payload receivedPayload = new Payload(
				"templateTitle",
				true,
				null,
				null,
				"",
				null,
				null,
				"",
				titlesBeforeUploading
		);

		when(processRecords.importTxtFiles(
				uploadedFiles,
				titleRepository,
				txtRepository,
				htmlRepository,
				fileOperations,
				formulas,
				extractors))
				.thenReturn(notImportedFiles);

		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titlesAfterUploading);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"FILE IMPORT RESULTS: 2 IMPORTED, 1 NOT IMPORTED, 3 TOTAL.",
				null,
				null,
				"",
				titlesAfterUploading
		);
		Assertions.assertEquals(expectedPayload, managementService.importTxt(receivedPayload, paths, uploadResults));
	}
}