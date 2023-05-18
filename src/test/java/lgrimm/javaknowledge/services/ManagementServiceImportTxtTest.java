package lgrimm.javaknowledge.services;

import lgrimm.javaknowledge.databasestorage.DatabaseStorageService;
import lgrimm.javaknowledge.datamodels.Payload;
import lgrimm.javaknowledge.process.Formulas;
import lgrimm.javaknowledge.process.ProcessRecords;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.mockito.Mockito.when;

class ManagementServiceImportTxtTest {

	DatabaseStorageService databaseStorageService;
	Formulas formulas;
	ProcessRecords processRecords;
	ManagementService managementService;
	String filename2, filename3, filename4;
	Path path2, path3, path4;
	List<Path> paths;
	File file2, file3, file4;
	List<File> uploadedFiles, notImportedFiles;
	long[] uploadResults;
	List<String> titlesBeforeUploading, titlesAfterUploading;

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
		titlesBeforeUploading = List.of("Title 1");
		titlesAfterUploading = List.of("Title 1", "Title 3");
		when(databaseStorageService.getAllTitles())
				.thenReturn(titlesBeforeUploading);
		filename2 = "file2.txt";
		filename3 = "file3.txt";
		filename4 = "file4.txt";
		path2 = Path.of(filename2);
		path3 = Path.of(filename3);
		path4 = Path.of(filename4);
		paths = List.of(path2, path3, path4);
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
				uploadedFiles))
				.thenReturn(notImportedFiles);
		when(databaseStorageService.getAllTitles())
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