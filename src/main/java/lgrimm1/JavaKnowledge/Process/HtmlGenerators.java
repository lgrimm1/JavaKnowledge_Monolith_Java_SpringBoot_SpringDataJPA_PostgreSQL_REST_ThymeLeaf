package lgrimm1.JavaKnowledge.Process;

import lgrimm1.JavaKnowledge.Title.*;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Generates elements of HTML pages.
 * @see #generateMainContent(List, Formulas, Extractors, TitleRepository)
 * @see #generateFirstTags(String, Formulas)
 * @see #generateLastTags(Formulas)
 * @see #changeToHtmlCharsInLine(String, Formulas)
 * @see #collectAndReferenceHeaders(List, Formulas)
 */
@Component
public class HtmlGenerators {

	/**
	 * Generates main html page content, also list of titles as page references.
	 * Both Lists of String are wrapped into a record.
	 */
	public MainHtmlContentPayload generateMainContent(List<String> text,
													  Formulas formulas,
													  Extractors extractors,
													  TitleRepository titleRepository) {
		List<String> html = new ArrayList<>();
		List<String> titles = new ArrayList<>();
		String line;
		int textIndex = 0;
		while (textIndex < text.size()) {
			//header1
			if ((text.get(textIndex).equals(formulas.getSuperLine())) &&
					(text.size() > textIndex + 2) &&
					(text.get(textIndex + 2).equals(formulas.getSuperLine()))) {
				html.add(formulas.getTabInSpaces() + "<a href=\"#top\">Back to top of page</a><br>");
				html.add(formulas.getTabInSpaces() + "<a name=\"" + text.get(textIndex + 1) + "\"></a>");
				html.add(formulas.getTabInSpaces() + "<h2>" + text.get(textIndex + 1) + "</h2>");
				textIndex += 3;
			}

			//header2
			else if ((text.size() > textIndex + 1) &&
					(text.get(textIndex + 1).equals(formulas.getSubLine()))) {
				html.add(formulas.getTabInSpaces() + "<a href=\"#top\">Back to top of page</a><br>");
				html.add(formulas.getTabInSpaces() + "<a name=\"" + text.get(textIndex) + "\"></a>");
				html.add(formulas.getTabInSpaces() + "<h3>" + text.get(textIndex) + "</h3>");
				textIndex += 2;
			}

			//table
			else if ((text.get(textIndex).length() > 3) && (text.get(textIndex).startsWith("||"))) {
				int textIndex2 = textIndex + 1;
				while ((textIndex2 < text.size()) &&
						(text.get(textIndex2).length() > 2) &&
						(text.get(textIndex2).startsWith("||"))) {
					textIndex2++;
				}
				html.addAll(extractors.extractTable(text.subList(textIndex, textIndex2), formulas));
				textIndex = textIndex2;
			}

			//example
			else if (text.get(textIndex).contains("EXAMPLE FOR")) {
				int textIndex2 = textIndex + 1;
				while (textIndex2 < text.size() && !text.get(textIndex2).equals("END OF EXAMPLE")) {
					textIndex2++;
				}
				html.addAll(extractors.extractExample(text.subList(textIndex, textIndex2), formulas));
				textIndex = textIndex2 + 1;
			}

			//reference
			else if ((text.get(textIndex).startsWith("=>"))) {
				html.add(extractors.extractReference(text.get(textIndex), formulas, titleRepository));
				titles.add(text.get(textIndex).substring(2));
				textIndex++;
			}

			//external reference, skipped
			else if (text.get(textIndex).startsWith("MORE HERE:")) {
				textIndex++;
			}

			//normal text
			else {
				line = changeToHtmlCharsInLine(text.get(textIndex), formulas).trim();
				if (line.isEmpty()) {
					html.add(formulas.getTabInSpaces() + "<br>");
				}
				else {
					html.add(formulas.getTabInSpaces() + "<p>" + line + "</p>");
				}
				textIndex++;
			}
		}
		return new MainHtmlContentPayload(html, titles);
	}

	public List<String> generateFirstTags(String title, Formulas formulas) {
		return new ArrayList<>(List.of(
				"<!DOCTYPE html>",
				"<html lang=\"en\">",
				"<head>",
				formulas.getTabInSpaces() + "<title>" + title + "</title>",
				formulas.getTabInSpaces() + "<meta charset=\"UTF-8\">",
				formulas.getTabInSpaces() + "<link rel=\"icon\" type=\"image/x-icon\" th:href=\"@{/images/favicon.ico}\" href=\"/images/favicon.ico\">",
				formulas.getTabInSpaces() + "<link rel=\"stylesheet\" th:href=\"@{/styles/desktop.css}\" href=\"/styles/desktop.css\">",
				"</head>",
				"<body>",
				formulas.getTabInSpaces() + "<a name=\"top\"></a>",
				formulas.getTabInSpaces() + formulas.getVersions() + "<br>",
				formulas.getTabInSpaces() + "<h1>" + title + "</h1>"
		));
	}

	public List<String> generateLastTags(Formulas formulas) {
		return List.of(
				formulas.getTabInSpaces() + "<br>",
				formulas.getTabInSpaces() + "<a href=\"#top\">Back to top of page</a><br>",
				formulas.getTabInSpaces() + "<script>",
				formulas.generateTabInSpaces(2) + "function content_to_clipboard(element) {",
				formulas.generateTabInSpaces(3) + "element.select();",
				formulas.generateTabInSpaces(3) + "document.execCommand('copy');",
				formulas.generateTabInSpaces(2) + "}",
				formulas.generateTabInSpaces(2) + "function element_to_full_size(element) {",
				formulas.generateTabInSpaces(3) + "element.style.height = \"\";",
				formulas.generateTabInSpaces(3) + "element.style.height = element.scrollHeight + \"px\";",
				formulas.generateTabInSpaces(2) + "}",
				formulas.getTabInSpaces() + "</script>",
				"</body>",
				"</html>"
		);
	}

	public String changeToHtmlCharsInLine(String line, Formulas formulas) {
		return line
				.replaceAll("<", "&lt;")
				.replaceAll("\t", formulas.getTabInHtml())
				.replaceAll(formulas.getTabInSpaces(), formulas.getTabInHtml());
	}

	public List<String> collectAndReferenceHeaders(List<String> html, Formulas formulas) {
		List<String> headers = html.stream()
				.filter(line -> line.contains("<h2>") || line.contains("<h3>"))
				.map(line -> line.substring(line.indexOf(">") + 1, line.lastIndexOf("<")))
				.toList();
		List<String> newHtml = new ArrayList<>(html);
		for (String header : headers) {
			int headerIndex = newHtml.indexOf(formulas.getTabInSpaces() + "<p>" + header + "</p>");
			if (headerIndex > -1) {
				newHtml.set(headerIndex, formulas.getTabInSpaces() +
						"<a href=\"#" + header + "\">" + header + "</a><br>");
			}
		}
		return newHtml;
	}
}
