package lgrimm1.JavaKnowledge.Process;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;

/**
 * Handles file-level operations.<p>
 * @see #writeHtmlFile(File, List)
 * @see #readTextFile(File)
 * @see #checkFolderExistence(File, String)
 * @see #getFileName(File)
 * @see #getExtension(File)
 * @see #getOSFileSeparator()
 * @see #deleteAllFilesInFolder(File, String)
// * @see #getListOfFiles(File, String)
 * @see #generateFilename(String)
 * @see #getResourcesPath()
 * @see #getStaticPath()
 */
public class FileOperations {

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
		try (FileReader reader = new FileReader(textFile)) {
			content = new char[(int) textFile.length()];
			readSize = reader.read(content);
		}
		catch (Exception e) {
			return new ArrayList<>();
		}
		if (readSize < 1) {
			return new ArrayList<>();
		}
//		contentTemp = Arrays.copyOfRange(contentTemp, 0, readSize);
/*
		return Arrays.stream(new String(Arrays.copyOfRange(contentTemp, 0, readSize))
				.split("\n")).toList();
*/
		return Arrays.stream(new String(content).split("\n"))
				.toList();
	}

	/**
	 * In case of non-existence, the exception message will be: "There is no accessible <meanNameOfDirectory> directory.".
	 */
	public void checkFolderExistence(File folder, String meanNameOfDirectory) {
		if (!folder.exists() || folder.isFile()) {
			throw new IllegalStateException("There is no " + meanNameOfDirectory + " directory.");
		}
	}

	public String getFileName(File file) {
		if (file.isFile()) {
			String name = file.getName();
			int p = name.lastIndexOf(".");
			return (p == -1) ? name : name.substring(0, p);
		}
		else {
			return "";
		}
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

	public String generateFilename(String title) {
		if (title == null || title.isBlank()) {
			return "";
		}
		return title
				.replaceAll("\t", " ")
				.replaceAll(".", " ")
				.replaceAll(":", " ")
				.replaceAll(",", " ")
				.replaceAll("  ", " ")
				.replaceAll(" ", "_");
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
