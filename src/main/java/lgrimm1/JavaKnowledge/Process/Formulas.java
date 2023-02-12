package lgrimm1.JavaKnowledge.Process;

/**
 * Carries constants and formulas.
 * Fields are opened for public due to testing purposes, protected with final access modifiers.
 * @see #generateRepeatedPattern(String, int)
 * @see #getFormula(String)
 * @see #getConstant(ConstantName)
 */
public class Formulas {

	public final String SUPERLINE = generateRepeatedPattern("=", 81);
	public final String SUBLINE = generateRepeatedPattern("-", 81);
	public final String TAB_IN_SPACES = generateRepeatedPattern(" ", 4);
	public final String TAB_IN_HTML = generateRepeatedPattern("&nbsp;", 4);
	public final String LEVEL_1_SEPARATOR = ";";
	public final String VERSIONS = "Up to Java 17, Spring 3";
	public final String ROOT_HTML_NAME = "!JavaKnowledge";
	public enum ConstantName {
		SUPERLINE, SUBLINE, TAB_IN_SPACES, TAB_IN_HTML, LEVEL_1_SEPARATOR, VERSIONS, ROOT_HTML_NAME
	}

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
			default -> "";
		};
	}

	public String getConstant(ConstantName constantName) {
		return switch (constantName) {
			case SUPERLINE -> SUPERLINE;
			case SUBLINE -> SUBLINE;
			case TAB_IN_SPACES -> TAB_IN_SPACES;
			case TAB_IN_HTML -> TAB_IN_HTML;
			case LEVEL_1_SEPARATOR -> LEVEL_1_SEPARATOR;
			case VERSIONS -> VERSIONS;
			case ROOT_HTML_NAME -> ROOT_HTML_NAME;
		};

	}

}
