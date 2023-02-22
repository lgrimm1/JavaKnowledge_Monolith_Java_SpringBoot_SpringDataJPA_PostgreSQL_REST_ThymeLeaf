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

	public List<String> generateMainContent(List<String> text,
											Formulas formulas,
											Extractors extractors,
											TitleRepository titleRepository) {
		List<String> html = new ArrayList<>();
		int textIndex = 0;
		while (textIndex < text.size()) {
			//header1
			if ((text.get(textIndex).equals(formulas.getConstant(Formulas.ConstantName.SUPERLINE))) &&
					(text.size() > textIndex + 2) &&
					(text.get(textIndex + 2).equals(formulas.getConstant(Formulas.ConstantName.SUPERLINE)))) {
				html.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"<a href=\"#top\">Back to top of page</a><br>");
				html.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"<a href=\""+ formulas.getConstant(Formulas.ConstantName.ROOT_HTML_NAME) +
						"\">Back to root page</a><br>");
				html.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"<a name=\"" + text.get(textIndex + 1) + "\"></a>");
				html.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"<h2>" + text.get(textIndex + 1) + "</h2>");
				textIndex += 3;
			}

			//header2
			else if ((text.size() > textIndex + 1) &&
					(text.get(textIndex + 1).equals(formulas.getConstant(Formulas.ConstantName.SUBLINE)))) {
				html.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"<a href=\"#top\">Back to top of page</a><br>");
				html.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"<a href=\""+ formulas.getConstant(Formulas.ConstantName.ROOT_HTML_NAME) +
						"\">Back to root page</a><br>");
				html.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"<a name=\"" + text.get(textIndex) + "\"></a>");
				html.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"<h3>" + text.get(textIndex) + "</h3>");
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
				html.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"<h4>" + text.get(textIndex) + "</h4>");
				int textIndex2 = textIndex + 1;
				html.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<textarea>");
				while (textIndex2 < text.size() && !text.get(textIndex2).equals("END OF EXAMPLE")) {
					html.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
							formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES)
							+ text.get(textIndex2) + "<br>");
					textIndex2++;
				}
				html.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "</textarea>");
				textIndex = textIndex2 + 1;
			}

			//reference
			else if ((text.get(textIndex).startsWith("=>"))) {
				html.add(extractors.extractReference(text.get(textIndex), formulas, titleRepository));
				textIndex++;
			}

			//development remarks
			//TODO skip the single TODO lines

			//normal text
			else {
				html.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + text.get(textIndex) + "</br>");
				textIndex++;
			}
		}
		return html;
	}

	public List<String> generateFirstTags(String title, Formulas formulas) {
		return new ArrayList<>(List.of(
				"<!DOCTYPE html>",
				"<html lang=\"en\">",
				"<head>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<meta charset=\"UTF-8\">",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<title>" + title + "</title>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<style>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "table, th, td {",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "border: 1px solid black;",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "border-collapse: collapse;",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "}",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "h1, h2, h3 {color:red;}",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "h4 {color:blue;}",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "</style>",
				"</head>",
				"<body>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<a name=\"top\"></a>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.VERSIONS) + "<br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<h1>" + title + "</h1>"
		));
	}

	public List<String> generateLastTags(Formulas formulas) {
		return List.of(
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"<a href=\"#top\">Back to top of page</a><br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"<a href=\""+ formulas.getConstant(Formulas.ConstantName.ROOT_HTML_NAME) +
						"\">Back to root page</a><br>",
				"</body>",
				"</html>"
		);
	}

	public String changeToHtmlCharsInLine(String line, Formulas formulas) {
		return line
				.replaceAll("<", "&lt;")
				.replaceAll("\t", formulas.getConstant(Formulas.ConstantName.TAB_IN_HTML))
				.replaceAll(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES),
						formulas.getConstant(Formulas.ConstantName.TAB_IN_HTML));
	}

	public List<String> collectAndReferenceHeaders(List<String> html, Formulas formulas) {
		List<String> headers = html.stream()
				.filter(line -> line.contains("<h2>") || line.contains("<h3>"))
				.map(line -> line.substring(line.indexOf(">") + 1, line.lastIndexOf("<")))
				.toList();
		List<String> newHtml = new ArrayList<>(html);
		for (String header : headers) {
			int headerIndex = newHtml.indexOf(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
					header + "<br>");
			if (headerIndex > -1) {
				newHtml.set(headerIndex, formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"<a href=\"#" + header + "\">" + formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						header + "</a><br>");
			}
		}
		return newHtml;
	}
}
