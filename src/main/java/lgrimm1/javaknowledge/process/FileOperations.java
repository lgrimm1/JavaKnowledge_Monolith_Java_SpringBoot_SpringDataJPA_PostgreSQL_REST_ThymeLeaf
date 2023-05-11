package lgrimm1.javaknowledge.process;

import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @see #writeFile(File, List)
 * @see #readFile(File)
 * @see #createNonExistentDirectory(File)
 * @see #getExtension(File)
 * @see #deleteAllFilesInFolder(File, String)
 */
@Component
public class FileOperations {

	/**
	 * Overwrites possible existing file.
	 */
	public boolean writeFile(File htmlFile, List<String> content) {
		if (htmlFile == null || content == null || content.isEmpty()) {
			return false;
		}
		try {
			Files.write(htmlFile.toPath(), content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		}
		catch (Exception e) {
		    return false;
		}
		return true;
	}

	/**
	 * In case of any problem, returns an empty List.
	 */
	public List<String> readFile(File textFile) {
		if (textFile == null) {
			return new ArrayList<>();
		}
		List<String> content;
		try {
			content = Files.readAllLines(textFile.toPath());
		}
		catch (Exception e) {
			return new ArrayList<>();
		}
		return content;
	}

	/**
	 * Returns true only when the folder already exists or successfully created.
	 */
	public boolean createNonExistentDirectory(File folder) {
		if (folder == null) {
			return false;
		}
		if (!folder.exists()) {
			return folder.mkdir();
		}
		return folder.isDirectory();
	}

	public String getExtension(File file) {
		String name = file.getName();
		int p = name.lastIndexOf(".");
		return (p == -1) ? "" : name.substring(p);
	}

	/**
	 * Returns the number of successfully deleted files.
	 */
	public long deleteAllFilesInFolder(File folder, String extensionWithSeparator) {
		if (folder == null || !folder.exists() || !folder.isDirectory() || extensionWithSeparator == null) {
			return 0;
		}
		File[] files = folder.listFiles();
		if (files == null || files.length == 0) {
			return 0;
		}
		return Arrays.stream(files)
				.filter(File::isFile)
				.filter(file -> getExtension(file).equalsIgnoreCase(extensionWithSeparator))
				.filter(File::delete)
				.count();
	}
}
