package lgrimm.javaknowledge.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * @see #extractTitle(List)
 * @see #extractTable(List)
 * @see #extractCells(String)
 * @see #extractExample(List, int)
 * @see #extractReference(String)
 * @see #extractBulletedList(List)
 * @see #changeToHtmlCharsInLine(String)
 */
@Component
public class Extractors {

	private final Formulas formulas;

	@Autowired
	public Extractors(Formulas formulas) {
		this.formulas = formulas;
	}

	/**
	 * Returns empty string if title is not valid.
	 */
	public String extractTitle(List<String> txt) {
		if (txt != null &&
				txt.size() > 2 &&
				txt.get(0).contains(formulas.getSuperLine()) &&
				txt.get(2).contains(formulas.getSuperLine()) &&
				!txt.get(1).isBlank()) {
			return txt.get(1).trim().toUpperCase();
		}
		return "";
	}

	/**
	 * Extracts table components.
	 */
	public List<String> extractTable(List<String> tableText) {
		List<String> tableInHtml = new ArrayList<>();
		tableInHtml.add(formulas.getTabInSpaces() + "<table class=\"table\">");
		for (int i = 0; i < tableText.size(); i++) {
			tableInHtml.add(formulas.generateTabInSpaces(2) + "<tr>");
			List<String> cells = extractCells(tableText.get(i).replace(formulas.getTableStart(), ""));
			for (String s : cells) {
				if (i == 0) {
					tableInHtml.add(formulas.generateTabInSpaces(3) +
							"<th class=\"table_th\"><i>" + s + "</i></th>");
				}
				else {
					tableInHtml.add(formulas.generateTabInSpaces(3) +
							"<td class=\"table_td\">" + s + "</td>");
				}
			}
			tableInHtml.add(formulas.generateTabInSpaces(2) + "</tr>");
		}
		tableInHtml.add(formulas.getTabInSpaces() + "</table>");
		return tableInHtml;
	}

	/**
	 * Extracts cells from a String, based upon the | separator character.
	 */
	private List<String> extractCells(String s) {
		ArrayList<String> cells = new ArrayList<>();
		int pos = s.indexOf("|");
		while (pos > -1) {
			cells.add(s.substring(0, pos));
			s = s.substring(pos + 1);
			pos = s.indexOf("|");
		}
		if (s.length() > 0) {
			cells.add(s);
		}
		return cells;
	}

	/**
	 * By definition, the content is an example if starts with EXAMPLE FOR text, optionally ends with END OF EXAMPLE text.
	 * In case no ending text until the end of the content, every line after the starting one will be rendered into the example.
	 */
	public List<String> extractExample(List<String> exampleText, int exampleCounter) {
		List<String> exampleInHtml = new ArrayList<>();
		exampleInHtml.add(formulas.getTabInSpaces() + "<h4>" + exampleText.get(0) + "</h4>");
		exampleInHtml.add(formulas.getTabInSpaces() + "<table class=\"formatter_table\">");
		exampleInHtml.add(formulas.generateTabInSpaces(2) + "<tr>");
		exampleInHtml.add(formulas.generateTabInSpaces(3) + "<td style=\"width: 85%\">");
		exampleInHtml.add("<textarea id=\"example_" + exampleCounter + "\" onclick=\"element_to_full_size(this)\" readonly>");
		for (int index = 1, size = exampleText.size(); index < size; index++) {
			exampleInHtml.add(exampleText.get(index));
		}

		exampleInHtml.add("</textarea>");
		exampleInHtml.add(formulas.generateTabInSpaces(3) + "</td>");
		exampleInHtml.add(formulas.generateTabInSpaces(3) + "<td style=\"width: 15%\">");
		exampleInHtml.add(formulas.generateTabInSpaces(4) +
				"<input type=\"button\" value=\"COPY\" class=\"button_full\" onclick=\"content_to_clipboard('example_" +
				exampleCounter + "')\" />");
		exampleInHtml.add(formulas.generateTabInSpaces(3) + "</td>");
		exampleInHtml.add(formulas.generateTabInSpaces(2) + "</tr>");
		exampleInHtml.add(formulas.getTabInSpaces() + "</table>");

		return exampleInHtml;
	}

	/**
	 * Extracts reference components from a line.
	 * The first header separator separates file reference from name reference inside the file, using it is optional.
	 */
	public String extractReference(String line) {
		line = formulas.getTabInSpaces() + "<p><i>See: " + line.substring(formulas.getReference().length()) + "</i></p>";
		return line.contains(formulas.getHeaderSeparator()) ?
				line.replace(formulas.getHeaderSeparator(), " / ") :
				line;
	}

	public List<String> extractBulletedList(List<String> bulletedListText) {
		List<String> bulletedListInHtml = new ArrayList<>();
		String effectiveLine;
		bulletedListInHtml.add(formulas.getTabInSpaces() + "<ol>");
		for (String textLine : bulletedListText) {
			if (textLine.startsWith(formulas.getBulletWithSpaces())) {
				effectiveLine = changeToHtmlCharsInLine(textLine.substring(
						formulas.getBulletWithSpaces().length()));
				bulletedListInHtml.add(formulas.getTabInSpaces() +
						formulas.getTabInSpaces() +
						"<li>" + effectiveLine +
						"</li>");
			}
			else {
				effectiveLine = changeToHtmlCharsInLine(textLine.substring(
						formulas.getBulletWithTab().length()));
				bulletedListInHtml.add(formulas.getTabInSpaces() +
						formulas.getTabInSpaces() +
						"<li>" + effectiveLine +
						"</li>");
			}
		}
		bulletedListInHtml.add(formulas.getTabInSpaces() + "</ol>");
		return bulletedListInHtml;
	}

	private String changeToHtmlCharsInLine(String line) {
		return line
				.replaceAll("<", "&lt;")
				.replaceAll("\t", formulas.getTabInHtml())
				.replaceAll(formulas.getTabInSpaces(), formulas.getTabInHtml());
	}
}
