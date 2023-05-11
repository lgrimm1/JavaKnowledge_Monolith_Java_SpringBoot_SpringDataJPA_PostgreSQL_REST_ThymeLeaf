package lgrimm1.javaknowledge.process;

import lgrimm1.javaknowledge.databasestorage.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * @see #writeFile(File, List)
 * @see #readFile(File)
 * @see #createNonExistentDirectory(File)
 * @see #getExtension(File)
 * @see #deleteAllFilesInFolder(File, String)
 * @see #generateFilename(String, TitleRepository)
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

/*
	public String getOSFileSeparator() {
		return File.separator;
	}
*/

/*
	public String getOSPathSeparator() {
		return File.pathSeparator;
	}
*/

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

	/**
	 * Replaces TAB, dot, column, semicolon, :, quote, double-quote and (repeated) SPACE characters
	 * to simple underline character,
	 * furthermore transforms the name to lowercase form, in order to form a proper filename.
	 * Finds exact matching in TitleRepository and creates numbered version if necessary.
	 */
	public String generateFilename(String title, TitleRepository titleRepository) {
		if (title == null || title.isBlank()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Arrays.stream(title
				.toLowerCase()
				.replace("\t", " ")
				.replace(".", " ")
				.replace(":", " ")
				.replace(",", " ")
				.replace(";", " ")
				.replace("'", " ")
				.replace("\"", " ")
				.replace("_", " ")
				.split(" "))
				.filter(word -> !word.isBlank())
				.toList()
				.forEach(word -> sb.append(word).append("_"));
		String fileName = sb.deleteCharAt(sb.length() - 1).toString();
		if (titleRepository.findByFilename(fileName).isEmpty()) {
			return fileName;
		}
		int number = 1;
		while (titleRepository.findByFilename(fileName + "_" + number).isPresent()) {
			number++;
		}
		return fileName + "_" + number;
	}
}
