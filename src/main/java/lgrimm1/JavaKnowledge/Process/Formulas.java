package lgrimm1.JavaKnowledge.Process;

/**
 * Carries constants and formulas.
 * @see #generateRepeatedPattern(String, int)
 * @see #getFormula(String)
 */
public class Formulas {

	public final String SUPERLINE = generateRepeatedPattern("=", 81);
	public final String SUBLINE = generateRepeatedPattern("-", 81);
	public final String TAB_IN_SPACES = generateRepeatedPattern(" ", 4);
	public final String TAB_IN_HTML = generateRepeatedPattern("&nbsp;", 4);
	public final String LEVEL_1_SEPARATOR = ";";
	public final String VERSIONS = "Up to Java 17, Spring 3";
	public final String ROOT_HTML_NAME = "!JavaKnowledge";
	public final String FORMULA_TITLE =
			SUPERLINE + "\n" +
			"TITLE\n" +
			SUPERLINE +"\n";
	public final String FORMULA_HEADER_1 =
			SUPERLINE + "\n" +
			"X. HEADER 1\n" +
			SUPERLINE +"\n";
	public final String FORMULA_HEADER_2 =
			"X.Y. HEADER 2\n" +
			SUBLINE +"\n";
	public final String FORMULA_TABLE =
			"||Column header 1|Column header 2||\n" +
			"||Cell 11|Cell 12||\n" +
			"||Cell 21|Cell 22||\n" +
			"||...|...||\n";
	public final String FORMULA_EXAMPLE =
			"EXAMPLE FOR ...\n" +
			"<codes>\n" +
			"END OF EXAMPLE\n";
	public final String FORMULA_REFERENCE = "=>file_name.html[;HEADER]";

	public String generateRepeatedPattern(String pattern, int numberOfRepeating) {
		return new String(new char[numberOfRepeating]).replace("\0", pattern);
	}

	/**
	 * In case of invalid name, returns empty String.
	 */
	public String getFormula(String formulaName) {
		if (formulaName == null || formulaName.isBlank()) {
			return "";
		}
		if (formulaName.equalsIgnoreCase("title")) {
			return FORMULA_TITLE;
		}
		if (formulaName.equalsIgnoreCase("header1")) {
			return FORMULA_HEADER_1;
		}
		if (formulaName.equalsIgnoreCase("header2")) {
			return FORMULA_HEADER_2;
		}
		if (formulaName.equalsIgnoreCase("table")) {
			return FORMULA_TABLE;
		}
		if (formulaName.equalsIgnoreCase("example")) {
			return FORMULA_EXAMPLE;
		}
		if (formulaName.equalsIgnoreCase("reference")) {
			return FORMULA_REFERENCE;
		}
		return "";
	}

}
