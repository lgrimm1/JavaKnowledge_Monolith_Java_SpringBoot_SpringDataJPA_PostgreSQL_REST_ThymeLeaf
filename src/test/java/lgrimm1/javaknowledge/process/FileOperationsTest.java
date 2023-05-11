package lgrimm1.javaknowledge.process;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

class FileOperationsTest {

	FileOperations fileOperations = new FileOperations();

	@Test
	void writeFile_WrongData() {
		Assertions.assertFalse(fileOperations.writeFile(null, List.of("Line 1")));
		Assertions.assertFalse(fileOperations.writeFile(new File("abc.xyz"), null));
		Assertions.assertFalse(fileOperations.writeFile(new File("abc.xyz"), new ArrayList<>()));
	}

	@Test
	void readFile_WrongData() {
		Assertions.assertTrue(fileOperations.readFile(null).isEmpty());
	}

	@Test
	void writeFile_NotAccessibleFile() {
		File file = Mockito.mock(File.class);
		when(file.exists())
				.thenReturn(false);
		when(file.isDirectory())
				.thenReturn(false);
		when(file.isFile())
				.thenReturn(false);
		when(file.length())
				.thenReturn(0L);
		when(file.canRead())
				.thenReturn(false);

		Assertions.assertFalse(fileOperations.writeFile(file, new ArrayList<>()));
	}

	@Test
	void readFile_NonExistentFile() {
		File file = Mockito.mock(File.class);
		when(file.exists())
				.thenReturn(false);
		when(file.isDirectory())
				.thenReturn(false);
		when(file.isFile())
				.thenReturn(false);
		when(file.length())
				.thenReturn(0L);
		when(file.canRead())
				.thenReturn(false);

		Assertions.assertTrue(fileOperations.readFile(file).isEmpty());
	}

	@Test
	void writeFile_Then_ReadFile(@TempDir Path tempPath) {
		Path file = tempPath.resolve("test_file");
		List<String> data = List.of(
				"Line 1",
				"Line 2"
		);

		Assertions.assertTrue(fileOperations.writeFile(file.toFile(), data));
		Assertions.assertTrue(file.toFile().exists() && file.toFile().isFile());
		Assertions.assertEquals(data, fileOperations.readFile(file.toFile()));

		Assertions.assertTrue(fileOperations.writeFile(file.toFile(), data));
		Assertions.assertTrue(file.toFile().exists() && file.toFile().isFile());
		Assertions.assertEquals(data, fileOperations.readFile(file.toFile()));
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

		Assertions.assertEquals(0,
				fileOperations.deleteAllFilesInFolder(null, null));
		Assertions.assertEquals(0,
				fileOperations.deleteAllFilesInFolder(sourceFolderNotExists, ".html"));
		Assertions.assertEquals(0,
				fileOperations.deleteAllFilesInFolder(sourceFolderNotDirectory1, ".html"));
		Assertions.assertEquals(0,
				fileOperations.deleteAllFilesInFolder(null, ".html"));
		Assertions.assertEquals(0,
				fileOperations.deleteAllFilesInFolder(sourceFolderNotDirectory2, ".html"));
		Assertions.assertEquals(0,
				fileOperations.deleteAllFilesInFolder(sourceFolderEmptyDirectory, ".html"));
		Assertions.assertEquals(2,
				fileOperations.deleteAllFilesInFolder(sourceFolderNotEmptyDirectory, ".html"));
		Assertions.assertEquals(1,
				fileOperations.deleteAllFilesInFolder(sourceFolderNotEmptyDirectory, ""));
		Assertions.assertEquals(2,
				fileOperations.deleteAllFilesInFolder(sourceFolderNotEmptyDirectory, ".HTML"));
		Assertions.assertEquals(1,
				fileOperations.deleteAllFilesInFolder(sourceFolderNotEmptyDirectory, ".xyz"));
	}
}
