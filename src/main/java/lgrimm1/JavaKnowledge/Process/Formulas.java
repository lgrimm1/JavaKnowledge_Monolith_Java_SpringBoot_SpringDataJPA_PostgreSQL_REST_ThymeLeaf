package lgrimm1.JavaKnowledge.Process;

import org.springframework.stereotype.*;

import java.util.*;

/**
 * Carries constants and formulas.
 * @see #getSuperLine()
 * @see #getSubLine()
 * @see #getTabInSpaces()
 * @see #getTabInHtml()
 * @see #getSpaceInHtml()
 * @see #getVersions()
 * @see #getHeaderSeparator()
 * @see #getBulletWithHtmlSpaces()
 * @see #getBulletWithTab()
 * @see #getMore()
 * @see #getTableStart()
 * @see #getExampleStart()
 * @see #getExampleEnd()
 * @see #getFormula(String)
 * @see #generateTabInSpaces(int)
 * @see #generateRepeatedPattern(String, int)
 */
@Component
public class Formulas {

	private final String superLine = generateRepeatedPattern("=", 81);
	private final String subLine = generateRepeatedPattern("-", 81);
	private final String tabInSpaces = generateRepeatedPattern(" ", 4);
	private final String tabInHtml = generateRepeatedPattern("&nbsp;", 4);
	private final String spaceInHtml = "&nbsp;";
	private final String versions = "Up to Java 17, Spring Boot 3 (Hibernate 6), JUnit 5, PostgreSQL 11, HTML 5, CSS 3.";
	private final String headerSeparator = ";";
	private final String bulletWithHtmlSpaces = tabInHtml + "- ";
	private final String bulletWithTab = "\t- ";
	private final String more = "MORE: ";
	private final String tableStart = "||";
	private final String exampleStart = "EXAMPLE FOR ";
	private final String exampleEnd = "END OF EXAMPLE";
	private final String reference = "=>";

	private final List<String> FORMULA_TITLE = List.of(
			superLine,
			"TITLE",
			superLine
	);
	private final List<String> FORMULA_HEADER_1 = List.of(
			superLine,
			"X. HEADER 1",
			superLine
	);
	private final List<String> FORMULA_HEADER_2 = List.of(
			"X.Y. HEADER 2",
			subLine
	);
	private final List<String> FORMULA_TABLE = List.of(
			"||COLUMN HEADER 1|COLUMN HEADER 2||",
			"||Cell 11|Cell 12||",
			"||Cell 21|Cell 22||",
			"||...|...||"
	);
	private final List<String> FORMULA_EXAMPLE = List.of(
			"EXAMPLE FOR ...",
			"<codes>",
			"END OF EXAMPLE"
	);
	private final List<String> FORMULA_REFERENCE = List.of("=>TITLE[;HEADER]");
	private final List<String> FORMULA_MORE = List.of("MORE HERE: ...");
	private final List<String> FORMULA_LIST_ITEM = List.of(tabInSpaces + "- ...");
	private final List<String> FORMULA_TAB = List.of(tabInSpaces + "...");
	private final List<String> FORMULA_TAB2 = List.of(tabInSpaces + tabInSpaces + "...");
	private final List<String> FORMULA_TAB3 = List.of(tabInSpaces + tabInSpaces + tabInSpaces + "...");

	public String getSuperLine() {
		return superLine;
	}

	public String getSubLine() {
		return subLine;
	}

	public String getTabInSpaces() {
		return tabInSpaces;
	}

	public String getTabInHtml() {
		return tabInHtml;
	}

	public String getSpaceInHtml() {
		return spaceInHtml;
	}

	public String getVersions() {
		return versions;
	}

	public String getHeaderSeparator() {
		return headerSeparator;
	}

	public String getBulletWithHtmlSpaces() {
		return bulletWithHtmlSpaces;
	}

	public String getBulletWithTab() {
		return bulletWithTab;
	}

	public String getMore() {
		return more;
	}

	public String getTableStart() {
		return tableStart;
	}

	public String getExampleStart() {
		return exampleStart;
	}

	public String getExampleEnd() {
		return exampleEnd;
	}

	public String getReference() {
		return reference;
	}

	/**
	 * In case of invalid argument, returns empty String.
	 */
	public List<String> getFormula(String formulaName) {
		if (formulaName == null) {
			return List.of();
		}
		return switch (formulaName.trim().toUpperCase()) {
			case "TITLE" -> FORMULA_TITLE;
			case "HEADER1" -> FORMULA_HEADER_1;
			case "HEADER2" -> FORMULA_HEADER_2;
			case "TABLE" -> FORMULA_TABLE;
			case "EXAMPLE" -> FORMULA_EXAMPLE;
			case "REFERENCE" -> FORMULA_REFERENCE;
			case "MORE" -> FORMULA_MORE;
			case "LIST" -> FORMULA_LIST_ITEM;
			case "TAB" -> FORMULA_TAB;
			case "TAB2" -> FORMULA_TAB2;
			case "TAB3" -> FORMULA_TAB3;
			default -> List.of();
		};
	}

	public String generateTabInSpaces(int repetition) {
		if (repetition < 1) {
			return "";
		}
		if (repetition == 1) {
			return tabInSpaces;
		}
		return tabInSpaces.repeat(repetition);
	}

	private String generateRepeatedPattern(String pattern, int numberOfRepeating) {
		return new String(new char[numberOfRepeating]).replace("\0", pattern);
	}

}
