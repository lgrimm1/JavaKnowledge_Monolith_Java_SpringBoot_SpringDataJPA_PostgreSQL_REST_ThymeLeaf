package lgrimm.javaknowledge.process;

import lgrimm.javaknowledge.datamodels.HtmlContentAndReferences;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * @see #generateMainContent(List)
 * @see #generateFirstTags(String)
 * @see #generateLastTags()
 * @see #changeToHtmlCharsInLine(String)
 * @see #collectAndReferenceHeaders(List)
 */
@Component
public class HtmlGenerators {

	private final Extractors extractors;
	private final Formulas formulas;

	public HtmlGenerators(Extractors extractors, Formulas formulas) {
		this.extractors = extractors;
		this.formulas = formulas;
	}

	/**
	 * Generates main html page content, also list of titles as page references.
	 * Both Lists of String are wrapped into a record.
	 */
	public HtmlContentAndReferences generateMainContent(List<String> text) {
		List<String> html = new ArrayList<>();
		List<String> titles = new ArrayList<>();
		int exampleCounter = 0;
		String line;
		int textIndex = 0;
		while (textIndex < text.size()) {
			//header1
			if ((text.get(textIndex).equals(formulas.getSuperLine())) &&
					(text.size() > textIndex + 2) &&
					(text.get(textIndex + 2).equals(formulas.getSuperLine()))) {
				html.add(formulas.getTabInSpaces() + "<a href=\"#top\"><i>Back to top of page</i></a><br>");
				html.add(formulas.getTabInSpaces() + "<a id=\"" + text.get(textIndex + 1) + "\"></a>");
				html.add(formulas.getTabInSpaces() + "<h2>" + text.get(textIndex + 1) + "</h2>");
				textIndex += 3;
			}

			//header2
			else if ((text.size() > textIndex + 1) &&
					(text.get(textIndex + 1).equals(formulas.getSubLine()))) {
				html.add(formulas.getTabInSpaces() + "<a href=\"#top\"><i>Back to top of page</i></a><br>");
				html.add(formulas.getTabInSpaces() + "<a id=\"" + text.get(textIndex) + "\"></a>");
				html.add(formulas.getTabInSpaces() + "<h3>" + text.get(textIndex) + "</h3>");
				textIndex += 2;
			}

			//table
			else if ((text.get(textIndex).length() > 3) && (text.get(textIndex).startsWith(formulas.getTableStart()))) {
				int textIndex2 = textIndex + 1;
				while ((textIndex2 < text.size()) &&
						(text.get(textIndex2).length() > 2) &&
						(text.get(textIndex2).startsWith(formulas.getTableStart()))) {
					textIndex2++;
				}
				html.addAll(extractors.extractTable(text.subList(textIndex, textIndex2)));
				textIndex = textIndex2;
			}

			//example
			else if (text.get(textIndex).contains(formulas.getExampleStart())) {
				int textIndex2 = textIndex + 1;
				while (textIndex2 < text.size() && !text.get(textIndex2).equals(formulas.getExampleEnd())) {
					textIndex2++;
				}
				exampleCounter++;
				html.addAll(extractors.extractExample(text.subList(textIndex, textIndex2), exampleCounter));
				textIndex = textIndex2 + 1;
			}

			//reference
			else if ((text.get(textIndex).startsWith(formulas.getReference()))) {
				html.add(extractors.extractReference(text.get(textIndex)));
				titles.add(text.get(textIndex).substring(formulas.getReference().length()));
				textIndex++;
			}

			//external reference, skipped
			else if (text.get(textIndex).startsWith(formulas.getMore())) {
				textIndex++;
			}

			//bulleted list
			else if (text.get(textIndex).startsWith(formulas.getBulletWithSpaces()) ||
					text.get(textIndex).startsWith(formulas.getBulletWithTab())) {
				int textIndex2 = textIndex + 1;
				while (textIndex2 < text.size() && (
						text.get(textIndex2).startsWith(formulas.getBulletWithSpaces()) ||
								text.get(textIndex2).startsWith(formulas.getBulletWithTab()))) {
					textIndex2++;
				}
				html.addAll(extractors.extractBulletedList(text.subList(textIndex, textIndex2)));
				textIndex = textIndex2;
			}

			//normal text
			else {
				line = changeToHtmlCharsInLine(text.get(textIndex)).trim();
				if (line.isEmpty()) {
					html.add(formulas.getTabInSpaces() + "<br>");
				}
				else {
					html.add(formulas.getTabInSpaces() + "<p>" + line + "</p>");
				}
				textIndex++;
			}
		}
		return new HtmlContentAndReferences(html, titles);
	}

	public List<String> generateFirstTags(String title) {
		return new ArrayList<>(List.of(
				formulas.getTabInSpaces() + "<i>" + formulas.getVersions() + "</i><br>",
				formulas.getTabInSpaces() + "<h1>" + title + "</h1>"
		));
	}

	public List<String> generateLastTags() {
		return List.of(
				formulas.getTabInSpaces() + "<br>",
				formulas.getTabInSpaces() + "<a href=\"#top\"><i>Back to top of page</i></a><br>"
		);
	}

	private String changeToHtmlCharsInLine(String line) {
		return line
				.replaceAll("<", "&lt;")
				.replaceAll("\t", formulas.getTabInHtml())
				.replaceAll(formulas.getTabInSpaces(), formulas.getTabInHtml());
	}

	public List<String> collectAndReferenceHeaders(List<String> html) {
		List<String> headers = html.stream()
				.filter(line -> line.contains("<h2>") || line.contains("<h3>"))
				.map(line -> line.substring(line.indexOf(">") + 1, line.lastIndexOf("<")))
				.toList();
		List<String> newHtml = new ArrayList<>(html);
		for (String header : headers) {
			int headerIndex = newHtml.indexOf(formulas.getTabInSpaces() + "<p>" + header + "</p>");
			if (headerIndex > -1) {
				newHtml.set(headerIndex, formulas.getTabInSpaces() +
						"<a href=\"#" + header + "\"><i>" + header + "</i></a><br>");
			}
		}
		return newHtml;
	}
}
