package lgrimm1.JavaKnowledge.Process;

import lgrimm1.JavaKnowledge.Title.*;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Extractors of special data.
 * @see #extractTitle(List, Formulas)
 * @see #extractTable(List, Formulas)
 * @see #extractCells(String)
 * @see #extractExample(List, Formulas)
 * @see #extractReference(String, Formulas, TitleRepository)
 * @see #extractBulletedList(List, Formulas)
 */
@Component
public class Extractors {

	/**
	 * Returns empty string if title is not valid.
	 */
	public String extractTitle(List<String> txt, Formulas formulas) {
		if (txt != null &&
				txt.size() > 2 &&
				txt.get(0).equals(formulas.getSuperLine()) &&
				txt.get(2).equals(formulas.getSuperLine()) &&
				!txt.get(1).isBlank()) {
			return txt.get(1).trim().toUpperCase();
		}
		return "";
	}

	/**
	 * Extracts table components.
	 */
	public List<String> extractTable(List<String> tableText, Formulas formulas) {
		List<String> tableInHtml = new ArrayList<>();
		tableInHtml.add(formulas.getTabInSpaces() + "<table class=\"table\">");
		for (int i = 0; i < tableText.size(); i++) {
			tableInHtml.add(formulas.generateTabInSpaces(2) + "<tr>");
			List<String> cells = extractCells(tableText.get(i).replace(formulas.getTableStart(), ""));
			for (String s : cells) {
				if (i == 0) {
					tableInHtml.add(formulas.generateTabInSpaces(3) +
							"<th class=\"table_th\">" + s + "</th>");
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
	public List<String> extractExample(List<String> exampleText, Formulas formulas) {
		List<String> exampleInHtml = new ArrayList<>();
		exampleInHtml.add(formulas.getTabInSpaces() + "<h4>" + exampleText.get(0) + "</h4>");
		exampleInHtml.add(formulas.getTabInSpaces() + "<table class=\"formatter_table\">");
		exampleInHtml.add(formulas.generateTabInSpaces(2) + "<tr>");
		exampleInHtml.add(formulas.generateTabInSpaces(3) + "<td style=\"width: 85%\">");
		exampleInHtml.add(formulas.generateTabInSpaces(4) +
				"<textarea onclick=\"element_to_full_size(this)\" readonly>");

		for (int index = 1, size = exampleText.size(); index < size; index++) {
			exampleInHtml.add(exampleText.get(index));
		}

		exampleInHtml.add(formulas.generateTabInSpaces(4) + "</textarea>");
		exampleInHtml.add(formulas.generateTabInSpaces(3) + "</td>");
		exampleInHtml.add(formulas.generateTabInSpaces(3) + "<td style=\"width: 15%\">");
		exampleInHtml.add(formulas.generateTabInSpaces(4) +
				"<input type=\"button\" value=\"COPY\" class=\"button_full\" onclick=\"content_to_clipboard(this)\" />");
		exampleInHtml.add(formulas.generateTabInSpaces(3) + "</td>");
		exampleInHtml.add(formulas.generateTabInSpaces(2) + "</tr>");
		exampleInHtml.add(formulas.getTabInSpaces() + "</table>");

		return exampleInHtml;
	}

	/**
	 * Extracts reference components from a line.
	 * The first header separator separates file reference from name reference inside the file, using it is optional.
	 * In case there is no matching title in TitleRepository, instead of link a text will be returned.
	 */
	public String extractReference(String line, Formulas formulas, TitleRepository titleRepository) {
		line = line.substring(formulas.getReference().length());
		String title, link, linkText;
		int posHeader = line.indexOf(formulas.getHeaderSeparator());
		if (posHeader == -1) { //only page, no header
			title = line;
			Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTitle(title);
			link = optionalTitleEntity
					.map(titleEntity -> titleEntity.getFilename() + ".html")
					.orElse("");
			linkText = "See: " + title;
		}
		else { //page with header
			title = line.substring(0, posHeader);
			String header = line.substring(posHeader + formulas.getHeaderSeparator().length());
			Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTitle(title);
			link = optionalTitleEntity
					.map(titleEntity -> titleEntity.getFilename() + ".html#" + header)
					.orElse("");
			linkText = "See: " + title + " / " + header;
		}
		return link.isEmpty() ?
				formulas.getTabInSpaces() + linkText + "</br>" :
				formulas.getTabInSpaces() + "<a href=\"" + link + "\">" + linkText + "</a></br>";
	}

	public List<String> extractBulletedList(List<String> bulletedListText, Formulas formulas) {
		List<String> bulletedListInHtml = new ArrayList<>();
		bulletedListInHtml.add(formulas.getTabInSpaces() + "<ol>");
		for (String textLine : bulletedListText) {
			if (textLine.startsWith(formulas.getBulletWithHtmlSpaces())) {
				bulletedListInHtml.add(formulas.getTabInSpaces() + formulas.getTabInSpaces() + "<li>" + textLine.substring(formulas.getBulletWithHtmlSpaces().length()) + "</li>");
			}
			else {
				bulletedListInHtml.add(formulas.getTabInSpaces() + formulas.getTabInSpaces() + "<li>" + textLine.substring(formulas.getBulletWithTab().length()) + "</li>");
			}
		}
		bulletedListInHtml.add(formulas.getTabInSpaces() + "</ol>");
		return bulletedListInHtml;
	}
}
