package lgrimm1.JavaKnowledge.Process;

import java.io.*;
import java.time.*;
import java.util.*;

/**
 * Processes folder of source TXT files.
 * @see #folderProcessor()
 * @see #processTitles(List)
 * @see #stringListToString(List, String)
 * @see #generateRepeatedPattern(String, int)
 * @see #getResourcesPath()
 */
public class FolderProcessor {

	private final String iniFile = "JavaKnowledge.ini";
	private final String superLine = generateRepeatedPattern("=", 81);
	private final String subLine = generateRepeatedPattern("-", 81);
	private final String tabInSpaces = generateRepeatedPattern(" ", 4);
	private final String tabInHtml = generateRepeatedPattern("&nbsp;", 4);
	private final String rootHtmlName = "!JavaKnowledge";
	private final String levelSeparator1 = ";";
	private final String languageVersions = "Up to Java 17, Spring 3";

	private final PageProcessor pageProcessor;
	private final FileProcessor fileProcessor;

	public FolderProcessor(PageProcessor pageProcessor, FileProcessor fileProcessor) {
		this.pageProcessor = pageProcessor;
		this.fileProcessor = fileProcessor;
	}

	/**
	 * Returns the list of names of pages of which processing failed.
	 */
	public List<String> folderProcessor() {
		LocalTime startLocalTime = LocalTime.now();
		String txtFolderName = getResourcesPath() + fileProcessor.getOSFileSeparator() + "txt";
		String htmlFolderName = getResourcesPath() + fileProcessor.getOSFileSeparator() + "static";
		File txtFolder = new File(txtFolderName);
		File htmlFolder = new File(htmlFolderName);
		fileProcessor.checkFolderExistence(txtFolder, "/resources/txt");
		fileProcessor.checkFolderExistence(htmlFolder, "/resources/static");
		if (!fileProcessor.deleteAllFilesInFolder(htmlFolder)) {
			throw new IllegalStateException("Unable to delete all HTML files.");
		}

		List<Title> titles = new ArrayList<>();
		int total, success = 0;
		List<File> txtFiles = fileProcessor.getListOfFiles(txtFolder, ".txt");
		total = txtFiles.size();
		List<String> namesOfNotProcessedFiles = new ArrayList<>();

		for (File txtFile : txtFiles) {
			String fileName = txtFile.getName();
			List<String> text = fileProcessor.readTextFile(txtFile);
			if (text.isEmpty()) {
				namesOfNotProcessedFiles.add(fileName);
			}
			else {
				String temp = stringListToString(pageProcessor.processTxt(
						text,
						titles,
						fileName + "",
						tabInSpaces,
						tabInHtml,
						superLine,
						subLine,
						rootHtmlName,
						levelSeparator1,
						languageVersions), "\n");
				if (fileProcessor.writeHtmlFile(
						new File(htmlFolderName + fileProcessor.getOSFileSeparator() + fileName),
						temp)) {
					success++;
				}
				else {
					namesOfNotProcessedFiles.add(txtFile.getName());
				}
			}
		}

		if (!fileProcessor.writeHtmlFile(
				new File(htmlFolderName + fileProcessor.getOSFileSeparator() + rootHtmlName),
				stringListToString(processTitles(titles), "\n"))) {
			namesOfNotProcessedFiles.add(rootHtmlName);
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
		html.add(tabInSpaces + "<meta charset=\"UTF-8\">");
		html.add(tabInSpaces + "<title>JAVA KNOWLEDGE</title>");
		html.add(tabInSpaces + "<style>");
		html.add(tabInSpaces + "table, th, td {");
		html.add(tabInSpaces + tabInSpaces + "border: 1px solid black;");
		html.add(tabInSpaces + tabInSpaces + "border-collapse: collapse;");
		html.add(tabInSpaces + "}");
		html.add(tabInSpaces + "h1, h2, h3 {color:red;}");
		html.add(tabInSpaces + "* {");
		html.add(tabInSpaces + tabInSpaces + "box-sizing: border-box;");
		html.add(tabInSpaces + "}");
		html.add(tabInSpaces + ".column {");
		html.add(tabInSpaces + tabInSpaces + "float: left;");
		html.add(tabInSpaces + tabInSpaces + "width: 50%;");
		html.add(tabInSpaces + tabInSpaces + "padding: 10px;");
//		html.add(tabInSpaces + tabInSpaces + "height: 300px;");
		html.add(tabInSpaces + "}");
		html.add(tabInSpaces + ".row:after {");
		html.add(tabInSpaces + tabInSpaces + "content: \"\";");
		html.add(tabInSpaces + tabInSpaces + "display: table;");
		html.add(tabInSpaces + tabInSpaces + "clear: both;");
		html.add(tabInSpaces + "}");
		html.add(tabInSpaces + "</style>");
		html.add("</head>");
		html.add("<body>");
		html.add(tabInSpaces + "<h1>JAVA KNOWLEDGE</h1>");
		html.add(languageVersions);
		int titlesSize = titles.size();
		int half = titlesSize / 2 + 1;
		String titleText, fileName;
		html.add(tabInSpaces + "<div class=\"row\">");
		html.add(tabInSpaces + tabInSpaces + "<div class=\"column\" style=\"background-color:#fff;\">");
		for (int titlesIndex = 0; titlesIndex < half; titlesIndex++) {
			titleText = titles.get(titlesIndex).titleText();
			fileName = titles.get(titlesIndex).fileName();
			switch (titles.get(titlesIndex).level()) {
				case 1 -> html.add(tabInSpaces + tabInSpaces + tabInSpaces + "<p><a href=\"" + fileName + "\"><b>" + titleText + "</b></a></p>");
				case 2 -> html.add(tabInSpaces + tabInSpaces + tabInSpaces + "<p>" + tabInHtml + "<a href=\"" + fileName + "\">" + titleText + "</a></p>");
				case 3 -> html.add(tabInSpaces + tabInSpaces + tabInSpaces + "<p>" + tabInHtml + tabInHtml + "<a href=\"" + fileName + "\"><i>" + titleText + "</i></a></p>");
			}
		}
		html.add(tabInSpaces + tabInSpaces + "</div>");
		html.add(tabInSpaces + tabInSpaces + "<div class=\"column\" style=\"background-color:#fff;\">");
		for (int titlesIndex = half; titlesIndex < titlesSize; titlesIndex++) {
			titleText = titles.get(titlesIndex).titleText();
			fileName = titles.get(titlesIndex).fileName();
			switch (titles.get(titlesIndex).level()) {
				case 1 -> html.add(tabInSpaces + tabInSpaces + tabInSpaces + "<p><a href=\"" + fileName + "\"><b>" + titleText + "</b></a></p>");
				case 2 -> html.add(tabInSpaces + tabInSpaces + tabInSpaces + "<p>" + tabInHtml + "<a href=\"" + fileName + "\">" + titleText + "</a></p>");
				case 3 -> html.add(tabInSpaces + tabInSpaces + tabInSpaces + "<p>" + tabInHtml + tabInHtml + "<a href=\"" + fileName + "\"><i>" + titleText + "</i></a></p>");
			}
		}
		html.add(tabInSpaces + tabInSpaces + "</div>");
		html.add(tabInSpaces + "</div>");
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

	public String generateRepeatedPattern(String pattern, int numberOfRepeating) {
		return new String(new char[numberOfRepeating]).replace("\0", pattern);
	}

	public String getResourcesPath() {
		return System.getProperty("user.dir") + fileProcessor.getOSFileSeparator() +
				"src" + fileProcessor.getOSFileSeparator() +
				"main" + fileProcessor.getOSFileSeparator() +
				"resources";
	}

}
