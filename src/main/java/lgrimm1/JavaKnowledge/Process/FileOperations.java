package lgrimm1.JavaKnowledge.Process;

import java.io.*;
import java.util.*;
import java.util.stream.*;

/**
 * Handles file-level operations.<p>
 * @see #writeHtmlFile(File, String)
 * @see #readTextFile(File)
 * @see #checkFolderExistence(File, String)
 * @see #getFileName(File)
 * @see #getExtension(File)
 * @see #getOSFileSeparator()
 * @see #deleteAllFilesInFolder(File)
 * @see #getListOfFiles(File, String)
 * @see #generateFilename(String)
 */
public class FileOperations {

	public boolean writeHtmlFile(File htmlFile, String content) {
		try (FileWriter writer = new FileWriter(htmlFile, false)) {
			writer.write(content);
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
		int fileSize;
		char[] contentTemp;
		int readSize;
		try (FileReader reader = new FileReader(textFile)) {
			fileSize = (int) textFile.length();
			contentTemp = new char[fileSize];
			readSize = reader.read(contentTemp);
		}
		catch (Exception e) {
			return new ArrayList<>();
		}
		if (readSize < 1) {
			return new ArrayList<>();
		}
		contentTemp = Arrays.copyOfRange(contentTemp, 0, readSize);
		String contentTemp2 = new String(contentTemp);
		try (Scanner scs = new Scanner(contentTemp2)) {
			List<String> lines = new ArrayList<>();
			while (scs.hasNextLine()) {
				lines.add(scs.nextLine());
			}
			return lines;
		}
		catch (Exception e) {
			return new ArrayList<>();
		}
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

	public boolean deleteAllFilesInFolder(File folder) {
		List<File> listOfFiles = getListOfFiles(folder, "");
		boolean ok = true;
		int i = 0;
		while (ok && (i < listOfFiles.size())) {
			try {
				if (listOfFiles.get(i).delete()) {
					i++;
				}
				else {
					ok = false;
				}
			}
			catch (Exception e) {
				ok = false;
			}
		}
		return ok;
	}

	/**
	 * In case extensionWithSeparator is empty, will get all files.
	 */
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
}
