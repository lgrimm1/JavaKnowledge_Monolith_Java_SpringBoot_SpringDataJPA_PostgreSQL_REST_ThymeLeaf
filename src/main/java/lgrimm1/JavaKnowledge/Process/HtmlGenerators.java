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
		int textIndex = 0;
		while (textIndex < text.size()) {
			//header1
			if ((text.get(textIndex).equals(formulas.getConstant(Formulas.ConstantName.SUPERLINE))) &&
					(text.size() > textIndex + 2) &&
					(text.get(textIndex + 2).equals(formulas.getConstant(Formulas.ConstantName.SUPERLINE)))) {
				html.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"<a href=\"#top\">Back to top of page</a><br>");
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
				html.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + text.get(textIndex) + "</br>");
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
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<title>" + title + "</title>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<meta charset=\"UTF-8\">",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<link rel=\"stylesheet\" href=\"styles.css\">",
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
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"<br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"<a href=\"#top\">Back to top of page</a><br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"<script>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"function example_to_clipboard(id) {",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"document.getElementById(id).select();",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"document.execCommand('copy');",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"}",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"function element_to_full_size(element) {",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"element.style.height = \"\";",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"element.style.height = element.scrollHeight + \"px\";",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"}",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"</script>",
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
