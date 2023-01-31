package lgrimm1.JavaKnowledge.Process;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.*;
import java.util.*;

import static org.mockito.Mockito.when;

class FileProcessorTest {

	FileProcessor fileProcessor = new FileProcessor();

	@Test
	void checkFolderExistence() {
		File folder = Mockito.mock(File.class);

		when(folder.exists())
				.thenReturn(true);
		when(folder.isFile())
				.thenReturn(false);
		Assertions.assertDoesNotThrow(() -> fileProcessor.checkFolderExistence(folder, "folder"));

		when(folder.exists())
				.thenReturn(false);
		when(folder.isFile())
				.thenReturn(true);
		Exception ex = Assertions.assertThrows(Exception.class, () -> fileProcessor.checkFolderExistence(folder, "folder"));
		Assertions.assertEquals("There is no folder directory.", ex.getMessage());

		when(folder.exists())
				.thenReturn(true);
		when(folder.isFile())
				.thenReturn(true);
		ex = Assertions.assertThrows(Exception.class, () -> fileProcessor.checkFolderExistence(folder, "folder"));
		Assertions.assertEquals("There is no folder directory.", ex.getMessage());
	}

	@Test
	void readTextFile() {
		File file = Mockito.mock(File.class);
		when(file.length())
				.thenReturn(0L);
		Assertions.assertTrue(fileProcessor.readTextFile(file).isEmpty());

		when(file.length())
				.thenReturn(12L);
		Assertions.assertTrue(fileProcessor.readTextFile(file).isEmpty());

		//TODO FileReader test with Mockito
	}

	@Test
	void writeHtmlFile() {
		//TODO FileWriter test with Mockito
	}

	@Test
	void getFileName() {
		File file1 = Mockito.mock(File.class);
		when(file1.isFile())
				.thenReturn(true);
		when(file1.getName())
				.thenReturn("File 1");
		File file2 = Mockito.mock(File.class);
		when(file2.isFile())
				.thenReturn(true);
		when(file2.getName())
				.thenReturn("File 2.xyz");
		File folder = Mockito.mock(File.class);
		when(folder.isFile())
				.thenReturn(false);
		when(folder.getName())
				.thenReturn("Folder");

		Assertions.assertEquals("File 1", fileProcessor.getFileName(file1));
		Assertions.assertEquals("File 2", fileProcessor.getFileName(file2));
		Assertions.assertTrue(fileProcessor.getFileName(folder).isEmpty());
	}

	@Test
	void getFileExtension() {
		File file1 = Mockito.mock(File.class);
		when(file1.getName())
				.thenReturn("fileName.xyz");
		File file2 = Mockito.mock(File.class);
		when(file2.getName())
				.thenReturn("fileName");
		File folder = Mockito.mock(File.class);
		when(folder.getName())
				.thenReturn("folderName");

		Assertions.assertEquals(".xyz", fileProcessor.getExtension(file1));
		Assertions.assertTrue(fileProcessor.getExtension(file2).isEmpty());
		Assertions.assertTrue(fileProcessor.getExtension(folder).isEmpty());
	}

	@Test
	void getOSFileSeparator() {
		Assertions.assertEquals(1, fileProcessor.getOSFileSeparator().length());
	}

	@Test
	void deleteAllFilesInFolder() {
		File file1 = Mockito.mock(File.class);
		when(file1.isFile())
				.thenReturn(true);
		when(file1.getName())
				.thenReturn("file 1");
		when(file1.delete())
				.thenReturn(true);

		File file2 = Mockito.mock(File.class);
		when(file2.isFile())
				.thenReturn(true);
		when(file2.getName())
				.thenReturn("file 2.xyz");
		when(file2.delete())
				.thenReturn(true);

		File file3 = Mockito.mock(File.class);
		when(file3.isFile())
				.thenReturn(true);
		when(file3.getName())
				.thenReturn("file 3.txt");
		when(file3.delete())
				.thenReturn(true);

		File file4 = Mockito.mock(File.class);
		when(file4.isFile())
				.thenReturn(true);
		when(file4.getName())
				.thenReturn("file 4.txt");
		when(file4.delete())
				.thenReturn(true);

		File sourceFolder = Mockito.mock(File.class);

		when(sourceFolder.listFiles())
				.thenReturn(null);
		Assertions.assertTrue(fileProcessor.deleteAllFilesInFolder(sourceFolder));

		when(sourceFolder.listFiles())
				.thenReturn(new File[]{file1, file2, file3, file4});
		Assertions.assertTrue(fileProcessor.deleteAllFilesInFolder(sourceFolder));

		when(file3.delete())
				.thenReturn(false);
		Assertions.assertFalse(fileProcessor.deleteAllFilesInFolder(sourceFolder));
	}

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
		Assertions.assertTrue(fileProcessor.getListOfFiles(sourceFolder, ".txt").isEmpty());

		when(sourceFolder.listFiles())
				.thenReturn(new File[]{folder1, folder2, file1, file2, file3, file4});
		Assertions.assertIterableEquals(List.of(file3, file4), fileProcessor.getListOfFiles(sourceFolder, ".txt"));
		Assertions.assertIterableEquals(List.of(file1, file2, file3, file4), fileProcessor.getListOfFiles(sourceFolder, ""));
	}

}
