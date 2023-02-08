package lgrimm1.JavaKnowledge.Process;

import java.util.*;

/**
 * Translates a TXT content to an HTML content, forming a knowledge page.
 * @see #processTxt(List, String, String, String, String, String, String, String, Extractors)
 * @see #generateFirstTags(String, String)
 * @see #generateLastTags(String, String)
 * @see #changeToHtmlCharsInLine(String, String, String)
 * @see #collectAndReferenceHeaders(List, String)
 */
public class ProcessPage {

	/**
	 * Processes the original text file lines and generates html code.
	 */
	public List<String> processTxt(List<String> text,
								   String title,
								   String tabInSpaces,
								   String tabInHtml,
								   String superLine,
								   String subLine,
								   String rootHtmlName,
								   String versions,
								   Extractors extractors) {
		text = text.stream()
				.map(line -> changeToHtmlCharsInLine(line, tabInSpaces, tabInHtml))
				.toList();

		List<String> html = new ArrayList<>();

		int textIndex = 0;
		while (textIndex < text.size()) {
			//title
			html.add(tabInSpaces + versions + "<br>");
			html.add(tabInSpaces + "<h1>" + title + "</h1>");
			//header1
			if ((text.get(textIndex).equals(superLine)) &&
					(text.size() > textIndex + 2) &&
					(text.get(textIndex + 2).equals(superLine))) {
				html.add(tabInSpaces + "<a href=\"#top\">Back to top of page</a><br>");
				html.add(tabInSpaces + "<a href=\""+ rootHtmlName +"\">Back to root page</a><br>");
				html.add(tabInSpaces + "<a name=\"" + text.get(textIndex + 1) + "\"></a>");
				html.add(tabInSpaces + "<h2>" + text.get(textIndex + 1) + "</h2>");
				textIndex += 3;
			}
			//header2
			else if ((text.size() > textIndex + 1) && (text.get(textIndex + 1).equals(subLine))) {
				html.add(tabInSpaces + "<a href=\"#top\">Back to top of page</a><br>");
				html.add(tabInSpaces + "<a href=\""+ rootHtmlName +"\">Back to root page</a><br>");
				html.add(tabInSpaces + "<a name=\"" + text.get(textIndex) + "\"></a>");
				html.add(tabInSpaces + "<h3>" + text.get(textIndex) + "</h3>");
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
		page = collectAndReferenceHeaders(page, tabInSpaces);
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

	public List<String> collectAndReferenceHeaders(List<String> html, String tabInSpaces) {
		List<String> headers = html.stream()
				.filter(line -> line.contains("<h2>") || line.contains("<h3>"))
				.map(line -> line.substring(line.indexOf(">") + 1, line.lastIndexOf("<")))
				.toList();
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
