package lgrimm1.JavaKnowledge.Process;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;

/**
 * Handles file-level operations.<p>
 * @see #writeHtmlFile(File, List)
 * @see #readTextFile(File)
 * @see #createNonExistentDirectory(File)
 * @see #getExtension(File)
 * @see #getOSFileSeparator()
 * @see #deleteAllFilesInFolder(File, String)
// * @see #getListOfFiles(File, String)
 * @see #generateFilename(String)
 * @see #getResourcesPath()
 * @see #getStaticPath()
 */
public class FileOperations {

	/**
	 * Overwrites possible existing file.
	 */
	public boolean writeHtmlFile(File htmlFile, List<String> content) {
		if (htmlFile == null || content == null || content.isEmpty()) {
			return false;
		}
		StringBuilder sb = new StringBuilder();
		content
				.forEach(line -> sb.append(line).append("\n"));
		try (FileWriter writer = new FileWriter(htmlFile, false)) {
			writer.write(sb.toString());
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * In case of any problem, returns an empty List.
	 */
	public List<String> readTextFile(File textFile) {
		char[] content;
		int readSize;
		try (FileReader fileReader = new FileReader(textFile)) {
			content = new char[(int) textFile.length()];
			readSize = fileReader.read(content);
		}
		catch (Exception e) {
			return new ArrayList<>();
		}
		if (readSize < 1) {
			return new ArrayList<>();
		}
		return Arrays.stream(
						new String(content)
								.replace("\r", "")
								.split("\n")
				)
				.toList();
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

	public String getOSFileSeparator() {
		return File.separator;
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

	/**
	 * In case extensionWithSeparator is empty, will get all files.
	 */
/*
	public List<File> getListOfFiles(File folder, String extensionWithSeparator) {
		try {
			File[] filesAndFolders = folder.listFiles();
			if (filesAndFolders != null) {
				if (extensionWithSeparator.isEmpty()) {
					return Arrays.stream(filesAndFolders)
							.filter(File::isFile)
							.sorted()
							.collect(Collectors.toCollection(ArrayList::new));
				}
				else {
					return Arrays.stream(filesAndFolders)
							.filter(File::isFile)
							.filter(file -> (getExtension(file).equalsIgnoreCase(extensionWithSeparator)))
							.sorted()
							.collect(Collectors.toCollection(ArrayList::new));
				}

			}
			else {
				return new ArrayList<>();
			}
		}
		catch (Exception e) {
			return new ArrayList<>();
		}
	}
*/

	/**
	 * Replaces TAB, dot, column, semicolumn, :, quote, double-quote and (repeated) SPACE characters to simple underline character, furthermore transforms the name to lowercase form, in order to form a proper filename.
	 */
	public String generateFilename(String title) {
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
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

	public String getResourcesPath() {
		return System.getProperty("user.dir") + getOSFileSeparator() +
				"src" + getOSFileSeparator() +
				"main" + getOSFileSeparator() +
				"resources";
	}

	public String getStaticPath() {
		return getResourcesPath() + getOSFileSeparator() + "static";
	}

}
