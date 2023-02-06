package lgrimm1.JavaKnowledge.Process;

import java.io.*;
import java.time.*;
import java.util.*;

/**
 * Processes folder of source TXT files.
 * @see #folderProcessor()
 * @see #processTitles(List)
 * @see #stringListToString(List, String)
 * @see #getResourcesPath()
 */
public class FolderProcessor {

	private final PageProcessor pageProcessor;
	private final FileOperations fileOperations;

	public FolderProcessor(PageProcessor pageProcessor, FileOperations fileOperations) {
		this.pageProcessor = pageProcessor;
		this.fileOperations = fileOperations;
	}

	/**
	 * Returns the list of names of pages of which processing failed.
	 */
	public List<String> folderProcessor() {
		LocalTime startLocalTime = LocalTime.now();
		String txtFolderName = getResourcesPath() + fileOperations.getOSFileSeparator() + "txt";
		String htmlFolderName = getResourcesPath() + fileOperations.getOSFileSeparator() + "static";
		File txtFolder = new File(txtFolderName);
		File htmlFolder = new File(htmlFolderName);
		fileOperations.checkFolderExistence(txtFolder, "/resources/txt");
		fileOperations.checkFolderExistence(htmlFolder, "/resources/static");
		if (!fileOperations.deleteAllFilesInFolder(htmlFolder)) {
			throw new IllegalStateException("Unable to delete all HTML files.");
		}

		List<Title> titles = new ArrayList<>();
		int total, success = 0;
		List<File> txtFiles = fileOperations.getListOfFiles(txtFolder, ".txt");
		total = txtFiles.size();
		List<String> namesOfNotProcessedFiles = new ArrayList<>();

		for (File txtFile : txtFiles) {
			String fileName = txtFile.getName();
			List<String> text = fileOperations.readTextFile(txtFile);
			if (text.isEmpty()) {
				namesOfNotProcessedFiles.add(fileName);
			}
			else {
				String temp = stringListToString(pageProcessor.processTxt(
						text,
						titles,
						fileName + "",
						Formulas.TAB_IN_SPACES,
						Formulas.TAB_IN_HTML,
						Formulas.SUPERLINE,
						Formulas.SUBLINE,
						Formulas.ROOT_HTML_NAME,
						Formulas.LEVEL_1_SEPARATOR,
						Formulas.VERSIONS), "\n");
				if (fileOperations.writeHtmlFile(
						new File(htmlFolderName + fileOperations.getOSFileSeparator() + fileName),
						temp)) {
					success++;
				}
				else {
					namesOfNotProcessedFiles.add(txtFile.getName());
				}
			}
		}

		if (!fileOperations.writeHtmlFile(
				new File(htmlFolderName + fileOperations.getOSFileSeparator() + Formulas.ROOT_HTML_NAME),
				stringListToString(processTitles(titles), "\n"))) {
			namesOfNotProcessedFiles.add(Formulas.ROOT_HTML_NAME);
		}

		System.out.println("Processed: " + success + " out of " + total + " in " + Duration.between(startLocalTime, LocalTime.now()).toSeconds() + " secs.");
		return namesOfNotProcessedFiles;
	}

	/**
	 * Generates a root html code from all html files and their titles.
	 * @param titles	the String&lt;Title&gt; of html filenames and titles.
	 * @return			the List&lt;String&gt; of html code.
	 */
	private List<String> processTitles(List<Title> titles) {
		Comparator<Title> comparator = new Comparator<>() {
			@Override
			public int compare(Title te1, Title te2) {
				return te1.textToCompare().compareToIgnoreCase(te2.textToCompare());
			}
		};
		titles.sort(comparator);
		ArrayList<String> html = new ArrayList<>();
		html.add("<!DOCTYPE html>");
		html.add("<html lang=\"en\">");
		html.add("<head>");
		html.add(Formulas.TAB_IN_SPACES + "<meta charset=\"UTF-8\">");
		html.add(Formulas.TAB_IN_SPACES + "<title>JAVA KNOWLEDGE</title>");
		html.add(Formulas.TAB_IN_SPACES + "<style>");
		html.add(Formulas.TAB_IN_SPACES + "table, th, td {");
		html.add(Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + "border: 1px solid black;");
		html.add(Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + "border-collapse: collapse;");
		html.add(Formulas.TAB_IN_SPACES + "}");
		html.add(Formulas.TAB_IN_SPACES + "h1, h2, h3 {color:red;}");
		html.add(Formulas.TAB_IN_SPACES + "* {");
		html.add(Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + "box-sizing: border-box;");
		html.add(Formulas.TAB_IN_SPACES + "}");
		html.add(Formulas.TAB_IN_SPACES + ".column {");
		html.add(Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + "float: left;");
		html.add(Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + "width: 50%;");
		html.add(Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + "padding: 10px;");
//		html.add(Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + "height: 300px;");
		html.add(Formulas.TAB_IN_SPACES + "}");
		html.add(Formulas.TAB_IN_SPACES + ".row:after {");
		html.add(Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + "content: \"\";");
		html.add(Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + "display: table;");
		html.add(Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + "clear: both;");
		html.add(Formulas.TAB_IN_SPACES + "}");
		html.add(Formulas.TAB_IN_SPACES + "</style>");
		html.add("</head>");
		html.add("<body>");
		html.add(Formulas.TAB_IN_SPACES + "<h1>JAVA KNOWLEDGE</h1>");
		html.add(Formulas.VERSIONS);
		int titlesSize = titles.size();
		int half = titlesSize / 2 + 1;
		String titleText, fileName;
		html.add(Formulas.TAB_IN_SPACES + "<div class=\"row\">");
		html.add(Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + "<div class=\"column\" style=\"background-color:#fff;\">");
		for (int titlesIndex = 0; titlesIndex < half; titlesIndex++) {
			titleText = titles.get(titlesIndex).titleText();
			fileName = titles.get(titlesIndex).fileName();
			switch (titles.get(titlesIndex).level()) {
				case 1 -> html.add(Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + "<p><a href=\"" + fileName + "\"><b>" + titleText + "</b></a></p>");
				case 2 -> html.add(Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + "<p>" + Formulas.TAB_IN_HTML + "<a href=\"" + fileName + "\">" + titleText + "</a></p>");
				case 3 -> html.add(Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + "<p>" + Formulas.TAB_IN_HTML + Formulas.TAB_IN_HTML + "<a href=\"" + fileName + "\"><i>" + titleText + "</i></a></p>");
			}
		}
		html.add(Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + "</div>");
		html.add(Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + "<div class=\"column\" style=\"background-color:#fff;\">");
		for (int titlesIndex = half; titlesIndex < titlesSize; titlesIndex++) {
			titleText = titles.get(titlesIndex).titleText();
			fileName = titles.get(titlesIndex).fileName();
			switch (titles.get(titlesIndex).level()) {
				case 1 -> html.add(Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + "<p><a href=\"" + fileName + "\"><b>" + titleText + "</b></a></p>");
				case 2 -> html.add(Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + "<p>" + Formulas.TAB_IN_HTML + "<a href=\"" + fileName + "\">" + titleText + "</a></p>");
				case 3 -> html.add(Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + "<p>" + Formulas.TAB_IN_HTML + Formulas.TAB_IN_HTML + "<a href=\"" + fileName + "\"><i>" + titleText + "</i></a></p>");
			}
		}
		html.add(Formulas.TAB_IN_SPACES + Formulas.TAB_IN_SPACES + "</div>");
		html.add(Formulas.TAB_IN_SPACES + "</div>");
		html.add("</body>");
		html.add("</html>");
		return html;
	}

	public String stringListToString(List<String> stringList, String separator) {
		int length = stringList.size();
		int totalLength = 0;
		for (String s : stringList) {
			totalLength += s.length();
		}
		StringBuilder sb = new StringBuilder(totalLength + (length - 1) * separator.length());
		for (int i = 0; i < length; ++i) {
			if (i == 0) {
				sb.append(stringList.get(i));
			}
			else {
				sb.append(separator).append(stringList.get(i));
			}
		}
		return sb.toString();
	}

	public String getResourcesPath() {
		return System.getProperty("user.dir") + fileOperations.getOSFileSeparator() +
				"src" + fileOperations.getOSFileSeparator() +
				"main" + fileOperations.getOSFileSeparator() +
				"resources";
	}

}
