package lgrimm1.JavaKnowledge.Process;

import java.util.*;

/**
 * Extractors of special data.
 * @see #extractReference(String, String)
 * @see #extractTable(List, String)
 * @see #extractCells(String)
 */
public class Extractors {

	/**
	 * <b>Extracts reference components from a line.</b><p>
	 * The line starts with =>.<p>
	 * The first ; separates file reference from name reference inside the file, using it is optional.<p>
	 * @param line	the String of line.
	 * @return		the String[2] of reference: [0] is HTML-file or HTML-file#name, [1] is link text.
	 */
	public String extractReference(String line, String tabInSpaces) {
		line = line.substring(2);
		String link, linkText;
		int posHeader = line.indexOf(";");
		if (posHeader == -1) { //only page, no header
			link = line + "." + "html";
			linkText = "See: " + line;
		}
		else { //page with header
			link = line.substring(0, posHeader) + "." + "html"+ "#" + line.substring(posHeader + 1);
			linkText = "See: " + line.substring(0, posHeader) + " / " + line.substring(posHeader + 1);
		}
		return tabInSpaces + "<a href=\"" + link + "\">" + linkText.replaceAll("_", " ") + "</a></br>";
	}

	/**
	 * <b>Extracts table components and add it to html.</b>
	 * @param tableText		the List&lt;String&gt; of table rows.
	 * @param tabInSpaces 	the String of spaces replacing a TAB.
	 * @return				the List&lt;String&gt; of html table
	 */
	public List<String> extractTable(List<String> tableText,
									String tabInSpaces) {
		List<String> tableInHtml = new ArrayList<>();
		tableInHtml.add(tabInSpaces + "<table>");
		for (int i = 0; i < tableText.size(); i++) {
			tableInHtml.add(tabInSpaces + tabInSpaces + "<tr>");
			ArrayList<String> cells = extractCells(tableText.get(i).replace("||", ""));
			for (String s : cells) {
				if (i == 0) {
					tableInHtml.add(tabInSpaces + tabInSpaces + tabInSpaces + "<th>" + s + "</th>");
				}
				else {
					tableInHtml.add(tabInSpaces + tabInSpaces + tabInSpaces + "<td>" + s + "</td>");
				}
			}
			tableInHtml.add(tabInSpaces + tabInSpaces + "</tr>");
		}
		tableInHtml.add(tabInSpaces + "</table>");
		return tableInHtml;
	}

	/**
	 * <b>Extracts cells from a String, based upon the | separator character.</b>
	 * @param s	the String of line.
	 * @return	the ArrayList&lt;String&gt; of cells.
	 */
	private ArrayList<String> extractCells(String s) {
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
}
