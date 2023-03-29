package lgrimm1.JavaKnowledge.Process;

import org.springframework.stereotype.*;

/**
 * Carries constants and formulas.
 * @see #getSuperLine()
 * @see #getSubLine()
 * @see #getTabInSpaces()
 * @see #getTabInHtml()
 * @see #getSpaceInHtml()
 * @see #getVersions()
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
	private final String versions = "Up to Java 17, Spring Boot 3 (Hibernate 6), JUnit 5, PostgreSQL 11";
/*
	private enum ConstantName {
		SUPERLINE, SUBLINE, TAB_IN_SPACES, TAB_IN_HTML, SPACE_IN_HTML, LEVEL_1_SEPARATOR, VERSIONS
	}
*/

	private final String FORMULA_TITLE =
			superLine + "\n" +
			"TITLE\n" +
					superLine +"\n";
	private final String FORMULA_HEADER_1 =
			superLine + "\n" +
			"X. HEADER 1\n" +
					superLine +"\n";
	private final String FORMULA_HEADER_2 =
			"X.Y. HEADER 2\n" +
					subLine +"\n";
	private final String FORMULA_TABLE =
			"||Column header 1|Column header 2||\n" +
			"||Cell 11|Cell 12||\n" +
			"||Cell 21|Cell 22||\n" +
			"||...|...||\n";
	private final String FORMULA_EXAMPLE =
			"EXAMPLE FOR ...\n" +
			"<codes>\n" +
			"END OF EXAMPLE\n";
	private final String FORMULA_REFERENCE = "=>file_name.html[;HEADER]";
	private final String FORMULA_MORE = "MORE HERE: ";

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

	/**
	 * In case of invalid argument, returns empty String.
	 */
	public String getFormula(String formulaName) {
		if (formulaName == null) {
			return "";
		}
		return switch (formulaName.trim().toUpperCase()) {
			case "TITLE" -> FORMULA_TITLE;
			case "HEADER1" -> FORMULA_HEADER_1;
			case "HEADER2" -> FORMULA_HEADER_2;
			case "TABLE" -> FORMULA_TABLE;
			case "EXAMPLE" -> FORMULA_EXAMPLE;
			case "REFERENCE" -> FORMULA_REFERENCE;
			case "MORE" -> FORMULA_MORE;
			default -> "";
		};
	}

/*
	public String getConstant(ConstantName constantName) {
		return switch (constantName) {
			case SUPERLINE -> SUPERLINE;
			case SUBLINE -> SUBLINE;
			case TAB_IN_SPACES -> TAB_IN_SPACES;
			case TAB_IN_HTML -> TAB_IN_HTML;
			case SPACE_IN_HTML -> SPACE_IN_HTML;
			case LEVEL_1_SEPARATOR -> LEVEL_1_SEPARATOR;
			case VERSIONS -> VERSIONS;
		};

	}
*/

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
