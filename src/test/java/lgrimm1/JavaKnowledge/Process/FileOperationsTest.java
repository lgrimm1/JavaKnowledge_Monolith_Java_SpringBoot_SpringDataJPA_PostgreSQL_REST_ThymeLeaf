package lgrimm1.JavaKnowledge.Process;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.*;
import java.util.*;

import static org.mockito.Mockito.when;

class FileOperationsTest {

	FileOperations fileOperations = new FileOperations();
	String testResourcesPath = fileOperations.getResourcesPath() + fileOperations.getOSFileSeparator() + "test_resources";

	@Test
	void writeHtmlFile() {
		Assertions.assertFalse(fileOperations.writeHtmlFile(null, List.of("Line 1")));
		Assertions.assertFalse(fileOperations.writeHtmlFile(new File("abc.xyz"), null));
		Assertions.assertFalse(fileOperations.writeHtmlFile(new File("abc.xyz"), new ArrayList<>()));

		File fileWriterTestFile = new File(testResourcesPath + fileOperations.getOSFileSeparator() + "fileWriterTestFile");
		Assertions.assertTrue(fileOperations.writeHtmlFile(fileWriterTestFile, List.of("Line 1", "Line 2")));
		Assertions.assertTrue(fileOperations.writeHtmlFile(fileWriterTestFile, List.of("Line 1", "Line 2")));
		fileWriterTestFile.delete();
	}

	@Test
	void readTextFile() {
		File notExistingFile = Mockito.mock(File.class);
		when(notExistingFile.exists())
				.thenReturn(false);
		when(notExistingFile.isFile())
				.thenReturn(false);
		when(notExistingFile.isDirectory())
				.thenReturn(false);

		File notFile = Mockito.mock(File.class);
		when(notExistingFile.exists())
				.thenReturn(true);
		when(notFile.isFile())
				.thenReturn(false);
		when(notFile.isDirectory())
				.thenReturn(true);

		File notReadableFile = Mockito.mock(File.class);
		when(notExistingFile.exists())
				.thenReturn(true);
		when(notReadableFile.isFile())
				.thenReturn(true);
		when(notReadableFile.isDirectory())
				.thenReturn(false);
		when(notReadableFile.canRead())
				.thenReturn(false);

		File readableEmptyFile = Mockito.mock(File.class);
		when(readableEmptyFile.exists())
				.thenReturn(true);
		when(readableEmptyFile.isFile())
				.thenReturn(true);
		when(readableEmptyFile.isDirectory())
				.thenReturn(false);
		when(readableEmptyFile.canRead())
				.thenReturn(true);
		when(readableEmptyFile.length())
				.thenReturn(0L);

		Assertions.assertTrue(fileOperations.readTextFile(notExistingFile).isEmpty());
		Assertions.assertTrue(fileOperations.readTextFile(notFile).isEmpty());
		Assertions.assertTrue(fileOperations.readTextFile(notReadableFile).isEmpty());
		Assertions.assertTrue(fileOperations.readTextFile(readableEmptyFile).isEmpty());

		File fileReaderTestFile = new File(testResourcesPath +
				fileOperations.getOSFileSeparator() +
				"FileReaderTestFile.txt");

		Assertions.assertEquals(
				List.of(
						"Line 1",
						"Line 2"
				), fileOperations.readTextFile(fileReaderTestFile));
	}

	@Test
	void checkFolderExistence() {
		File folderNotExistingNotCreatable = Mockito.mock(File.class);
		when(folderNotExistingNotCreatable.exists())
				.thenReturn(false);
		when(folderNotExistingNotCreatable.mkdir())
				.thenReturn(false);

		File folderNotExistingCreatable = Mockito.mock(File.class);
		when(folderNotExistingCreatable.exists())
				.thenReturn(false);
		when(folderNotExistingCreatable.mkdir())
				.thenReturn(true);

		File folderExistingNotDirectory = Mockito.mock(File.class);
		when(folderExistingNotDirectory.exists())
				.thenReturn(true);
		when(folderExistingNotDirectory.isDirectory())
				.thenReturn(false);

		File folderExistingDirectory = Mockito.mock(File.class);
		when(folderExistingDirectory.exists())
				.thenReturn(true);
		when(folderExistingDirectory.isDirectory())
				.thenReturn(true);

		Assertions.assertFalse(fileOperations.createNonExistentDirectory(null));
		Assertions.assertFalse(fileOperations.createNonExistentDirectory(folderNotExistingNotCreatable));
		Assertions.assertTrue(fileOperations.createNonExistentDirectory(folderNotExistingCreatable));
		Assertions.assertFalse(fileOperations.createNonExistentDirectory(folderExistingNotDirectory));
		Assertions.assertTrue(fileOperations.createNonExistentDirectory(folderExistingDirectory));
	}

	@Test
	void getExtension() {
		File fileXyz = Mockito.mock(File.class);
		when(fileXyz.getName())
				.thenReturn("filename1.xyz");
		File fileNoExtension = Mockito.mock(File.class);
		when(fileNoExtension.getName())
				.thenReturn("filename2");

		Assertions.assertEquals(".xyz", fileOperations.getExtension(fileXyz));
		Assertions.assertTrue(fileOperations.getExtension(fileNoExtension).isEmpty());
	}

	@Test
	void getOSFileSeparator() {
		Assertions.assertEquals(1, fileOperations.getOSFileSeparator().length());
	}

	@Test
	void deleteAllFilesInFolder() {
		File fileNoExtension = Mockito.mock(File.class);
		when(fileNoExtension.isFile())
				.thenReturn(true);
		when(fileNoExtension.getName())
				.thenReturn("file 1");
		when(fileNoExtension.delete())
				.thenReturn(true);

		File fileXyz = Mockito.mock(File.class);
		when(fileXyz.isFile())
				.thenReturn(true);
		when(fileXyz.getName())
				.thenReturn("file 2.xyz");
		when(fileXyz.delete())
				.thenReturn(true);

		File fileHtml1 = Mockito.mock(File.class);
		when(fileHtml1.isFile())
				.thenReturn(true);
		when(fileHtml1.getName())
				.thenReturn("file 3.html");
		when(fileHtml1.delete())
				.thenReturn(true);

		File fileHtml2 = Mockito.mock(File.class);
		when(fileHtml2.isFile())
				.thenReturn(true);
		when(fileHtml2.getName())
				.thenReturn("file 4.html");
		when(fileHtml2.delete())
				.thenReturn(true);

		File fileHtmlNotDeletable = Mockito.mock(File.class);
		when(fileHtmlNotDeletable.isFile())
				.thenReturn(true);
		when(fileHtmlNotDeletable.getName())
				.thenReturn("file 4.html");
		when(fileHtmlNotDeletable.delete())
				.thenReturn(false);

		File sourceFolderNotExists = Mockito.mock(File.class);
		when(sourceFolderNotExists.exists())
				.thenReturn(false);

		File sourceFolderNotDirectory1 = Mockito.mock(File.class);
		when(sourceFolderNotDirectory1.exists())
				.thenReturn(true);
		when(sourceFolderNotDirectory1.isDirectory())
				.thenReturn(false);

		File sourceFolderNotDirectory2 = Mockito.mock(File.class);
		when(sourceFolderNotDirectory2.exists())
				.thenReturn(true);
		when(sourceFolderNotDirectory2.isDirectory())
				.thenReturn(true);
		when(sourceFolderNotDirectory2.listFiles())
				.thenReturn(null);

		File sourceFolderEmptyDirectory = Mockito.mock(File.class);
		when(sourceFolderEmptyDirectory.exists())
				.thenReturn(true);
		when(sourceFolderEmptyDirectory.isDirectory())
				.thenReturn(true);
		when(sourceFolderEmptyDirectory.listFiles())
				.thenReturn(new File[0]);
		
		File sourceFolderNotEmptyDirectory = Mockito.mock(File.class);
		when(sourceFolderNotEmptyDirectory.exists())
				.thenReturn(true);
		when(sourceFolderNotEmptyDirectory.isDirectory())
				.thenReturn(true);
		when(sourceFolderNotEmptyDirectory.listFiles())
				.thenReturn(new File[]{fileNoExtension, fileXyz, fileHtml1, fileHtml2, fileHtmlNotDeletable});

		Assertions.assertEquals(0, fileOperations.deleteAllFilesInFolder(null, null));
		Assertions.assertEquals(0, fileOperations.deleteAllFilesInFolder(sourceFolderNotExists, ".html"));
		Assertions.assertEquals(0, fileOperations.deleteAllFilesInFolder(sourceFolderNotDirectory1, ".html"));
		Assertions.assertEquals(0, fileOperations.deleteAllFilesInFolder(null, ".html"));
		Assertions.assertEquals(0, fileOperations.deleteAllFilesInFolder(sourceFolderNotDirectory2, ".html"));
		Assertions.assertEquals(0, fileOperations.deleteAllFilesInFolder(sourceFolderEmptyDirectory, ".html"));
		Assertions.assertEquals(2, fileOperations.deleteAllFilesInFolder(sourceFolderNotEmptyDirectory, ".html"));
		Assertions.assertEquals(1, fileOperations.deleteAllFilesInFolder(sourceFolderNotEmptyDirectory, ""));
		Assertions.assertEquals(2, fileOperations.deleteAllFilesInFolder(sourceFolderNotEmptyDirectory, ".HTML"));
		Assertions.assertEquals(1, fileOperations.deleteAllFilesInFolder(sourceFolderNotEmptyDirectory, ".xyz"));
	}

/*
	@Test
	void getListOfFiles() {
		File folder1 = Mockito.mock(File.class);
		when(folder1.isFile())
				.thenReturn(false);
		when(folder1.getName())
				.thenReturn("folder 1");
		File folder2 = Mockito.mock(File.class);
		when(folder2.isFile())
				.thenReturn(false);
		when(folder2.getName())
				.thenReturn("folder 2");
		File file1 = Mockito.mock(File.class);
		when(file1.isFile())
				.thenReturn(true);
		when(file1.getName())
				.thenReturn("file 1");
		File file2 = Mockito.mock(File.class);
		when(file2.isFile())
				.thenReturn(true);
		when(file2.getName())
				.thenReturn("file 2.xyz");
		File file3 = Mockito.mock(File.class);
		when(file3.isFile())
				.thenReturn(true);
		when(file3.getName())
				.thenReturn("file 3.txt");
		File file4 = Mockito.mock(File.class);
		when(file4.isFile())
				.thenReturn(true);
		when(file4.getName())
				.thenReturn("file 4.txt");
		File file5 = Mockito.mock(File.class);

		File sourceFolder = Mockito.mock(File.class);

		when(sourceFolder.listFiles())
				.thenReturn(null);
		Assertions.assertTrue(fileOperations.getListOfFiles(sourceFolder, ".txt").isEmpty());

		when(sourceFolder.listFiles())
				.thenReturn(new File[]{folder1, folder2, file1, file2, file3, file4});
		Assertions.assertIterableEquals(List.of(file3, file4), fileOperations.getListOfFiles(sourceFolder, ".txt"));
		Assertions.assertIterableEquals(List.of(file1, file2, file3, file4), fileOperations.getListOfFiles(sourceFolder, ""));
	}
*/

	@Test
	void generateFilename() {
		Assertions.assertTrue(fileOperations.generateFilename(null).isEmpty());
		Assertions.assertTrue(fileOperations.generateFilename("  ").isEmpty());
		Assertions.assertEquals("abc_def_ghi_jkl_mno_pqr_stu", fileOperations.generateFilename("Abc\tdef.gHi:jkl,mno;     pqr stu"));
	}

	@Test
	void getResourcesPath() {
		String resourcePath = fileOperations.getResourcesPath();

		Assertions.assertTrue(resourcePath.contains("src") &&
				resourcePath.contains("main") &&
				resourcePath.contains("resources"));
	}

	@Test
	void getStaticPath() {
		String resourcePath = fileOperations.getStaticPath();

		Assertions.assertTrue(resourcePath.contains("src") &&
				resourcePath.contains("main") &&
				resourcePath.contains("resources") &&
				resourcePath.contains("static"));
	}
}
