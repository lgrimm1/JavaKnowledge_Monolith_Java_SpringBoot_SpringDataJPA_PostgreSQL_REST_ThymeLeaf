package lgrimm1.JavaKnowledge.Process;

import java.util.*;

/**
 * Processes a knowledge page.
 * @see #processTxt(List, List, String, String, String, String, String, String, String, String)
 * @see #generateFirstTags(String, String)
 * @see #generateLastTags(String, String)
 * @see #changeToHtmlCharsInLine(String, String, String)
 * @see #collectAndReferencingHeaders(List, String)
 */
public class PageProcessor {

	private final Extractors extractors;

	public PageProcessor(Extractors extractors) {
		this.extractors = extractors;
	}

	/**
	 * Processes the original text file lines and generates html code.
	 * @param text				the List of original file lines.
	 * @param titles			the List of titles.
	 * @param fileName			the String of name of processed file.
	 * @param tabInSpaces		the String of spaces replacing a TAB.
	 * @param tabInHtml			the String of HTML TAB character replacing a TAB.
	 * @param superLine			the String of title/header1/header2 marker.
	 * @param subLine			the String of header3 marker.
	 * @param rootHtmlName		the String of root HTML file name.
	 * @param levelSeparator1	the String of header level separator used in sorting headers.
	 * @param languageVersions	the String of Java, Spring etc. version text
	 * @return					the ArrayList&lt;String&gt; of generated html lines.
	 */
	public List<String> processTxt(List<String> text,
								   List<Title> titles,
								   String fileName,
								   String tabInSpaces,
								   String tabInHtml,
								   String superLine,
								   String subLine,
								   String rootHtmlName,
								   String levelSeparator1,
								   String languageVersions) {
		text = text.stream()
				.map(line -> changeToHtmlCharsInLine(line, tabInSpaces, tabInHtml))
				.toList();

		String title = "";
		String header1 = "";
		String header2;
		List<String> html = new ArrayList<>();
		List<String> example;

		int textIndex = 0;
		while (textIndex < text.size()) {
			//title
			if ((textIndex == 0) && (text.get(0).equals(superLine)) && (text.size() > 2) && (text.get(2).equals(superLine))) {
				title = text.get(1);
				html.add(tabInSpaces + languageVersions + "<br>");
				html.add(tabInSpaces + "<h1>" + title + "</h1>");
				titles.add(new Title(title, title, fileName, 1));
				textIndex += 3;
			}
			//header1
			else if ((text.get(textIndex).equals(superLine)) &&
					(text.size() > textIndex + 2) &&
					(text.get(textIndex + 2).equals(superLine))) {
				html.add(tabInSpaces + "<a href=\"#top\">Back to top of page</a><br>");
				html.add(tabInSpaces + "<a href=\""+ rootHtmlName +"\">Back to root page</a><br>");
				html.add(tabInSpaces + "<a name=\"" + text.get(textIndex + 1) + "\"></a>");
				html.add(tabInSpaces + "<h2>" + text.get(textIndex + 1) + "</h2>");
				header1 = (text.get(textIndex + 1).indexOf(".") == 1) ? "0" + text.get(textIndex + 1) : text.get(textIndex + 1);
				titles.add(new Title(
						title + levelSeparator1 + header1,
						text.get(textIndex + 1),
						fileName + "#" + text.get(textIndex + 1),
						2));
				textIndex += 3;
			}
			//header2
			else if ((text.size() > textIndex + 1) && (text.get(textIndex + 1).equals(subLine))) {
				html.add(tabInSpaces + "<a href=\"#top\">Back to top of page</a><br>");
				html.add(tabInSpaces + "<a href=\""+ rootHtmlName +"\">Back to root page</a><br>");
				html.add(tabInSpaces + "<a name=\"" + text.get(textIndex) + "\"></a>");
				html.add(tabInSpaces + "<h3>" + text.get(textIndex) + "</h3>");
				header2 = (text.get(textIndex).indexOf(".") == 1) ? "0" + text.get(textIndex) : text.get(textIndex);
				titles.add(new Title(
						title + levelSeparator1 + header1 + levelSeparator1 + header2,
						text.get(textIndex),
						fileName + "#" + text.get(textIndex),
						3));
				textIndex += 2;
			}
			//example
			else if (text.get(textIndex).contains("EXAMPLE FOR")) {
				html.add(tabInSpaces + "<h4>" + text.get(textIndex) + "</h4>");
				int textIndex2 = textIndex + 1;
				html.add(tabInSpaces + "<textarea>");
				while (textIndex2 < text.size() && !text.get(textIndex2).equals("END OF EXAMPLE")) {
					html.add(tabInSpaces + tabInSpaces + text.get(textIndex2) + "<br>");
					textIndex2++;
				}
				html.add(tabInSpaces + "</textarea>");
				textIndex = textIndex2 + 1;
			}
			//reference
			else if ((text.get(textIndex).startsWith("=>"))) {
				html.add(extractors.extractReference(text.get(textIndex), tabInSpaces));
				textIndex++;
			}
			//table
			else if ((text.get(textIndex).length() > 3) && (text.get(textIndex).startsWith("||"))) {
				int textIndex2 = textIndex + 1;
				while ((textIndex2 < text.size()) && (text.get(textIndex2).length() > 2) && (text.get(textIndex2).startsWith("||"))) {
					textIndex2++;
				}
				html.addAll(extractors.extractTable(text.subList(textIndex, textIndex2), tabInSpaces));
				textIndex = textIndex2;
			}
			//normal text
			else {
				html.add(tabInSpaces + text.get(textIndex) + "<br>");
				textIndex++;
			}
		}

		List<String> page = generateFirstTags(title, tabInSpaces);
		page.addAll(html);
		page.addAll(generateLastTags(rootHtmlName, tabInSpaces));
		page = collectAndReferencingHeaders(page, tabInSpaces);
		return page;
	}

	public List<String> generateFirstTags(String title, String tabInSpaces) {
		return new ArrayList<>(List.of(
								"<!DOCTYPE html>",
								"<html lang=\"en\">",
								"<head>",
								tabInSpaces + "<meta charset=\"UTF-8\">",
								tabInSpaces + "<title>" + title + "</title>",
								tabInSpaces + "<style>",
								tabInSpaces + "table, th, td {",
								tabInSpaces + tabInSpaces + "border: 1px solid black;",
								tabInSpaces + tabInSpaces + "border-collapse: collapse;",
								tabInSpaces + "}",
								tabInSpaces + "h1, h2, h3 {color:red;}",
								tabInSpaces + "h4 {color:blue;}",
								tabInSpaces + "</style>",
								"</head>",
								"<body>",
								tabInSpaces + "<a name=\"top\"></a>"
						));
	}
	
	public List<String> generateLastTags(String rootHtmlName, String tabInSpaces) {
		return List.of(
				tabInSpaces + "<br>",
				tabInSpaces + "<a href=\"#top\">Back to top of page</a><br>",
				tabInSpaces + "<a href=\""+ rootHtmlName +"\">Back to root page</a><br>",
				"</body>",
				"</html>"
		);
	}

	public String changeToHtmlCharsInLine(String line, String tabInSpaces, String tabInHtml) {
		return line
				.replaceAll("<", "&lt;")
				//.replaceAll(">", "&gt;")
				.replaceAll("\t", tabInHtml)
				.replaceAll(tabInSpaces, tabInHtml);
	}

	public List<String> collectAndReferencingHeaders(List<String> html, String tabInSpaces) {
		List<String> headers = html.stream()
				.filter(line -> line.contains("<h2>") || line.contains("<h3>"))
				.map(line -> line.substring(line.indexOf(">") + 1, line.lastIndexOf("<")))
				.toList();
/*
		ArrayList<String> headers = new ArrayList<>();
		for (String line : html) {
			if ((line.contains("<h2>")) || (line.contains("<h3>"))) {
				headers.add(line.substring(line.indexOf(">") + 1, line.lastIndexOf("<")));
			}
		}
*/
		List<String> newHtml = new ArrayList<>(html);
		for (String header : headers) {
			int headerIndex = newHtml.indexOf(tabInSpaces + header + "<br>");
			if (headerIndex > -1) {
				newHtml.set(headerIndex, tabInSpaces + "<a href=\"#" + header + "\">" + tabInSpaces + header + "</a><br>");
			}
		}
		return newHtml;
	}

}
