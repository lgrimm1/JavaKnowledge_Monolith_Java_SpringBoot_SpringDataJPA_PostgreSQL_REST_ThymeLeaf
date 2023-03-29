package lgrimm1.JavaKnowledge.Process;

import org.junit.jupiter.api.*;

class FormulasTest {

	Formulas formulas = new Formulas();

	@Test
	void getSuperLine() {
		Assertions.assertEquals("=".repeat(81), formulas.getSuperLine());
	}

	@Test
	void getSubLine() {
		Assertions.assertEquals("-".repeat(81), formulas.getSubLine());
	}

	@Test
	void getTabInSpaces() {
		Assertions.assertEquals(" ".repeat(4), formulas.getTabInSpaces());
	}

	@Test
	void getTabInHtml() {
		Assertions.assertEquals("&nbsp;".repeat(4), formulas.getTabInHtml());
	}

	@Test
	void getSpaceInHtml() {
		Assertions.assertEquals("&nbsp;", formulas.getSpaceInHtml());
	}

	@Test
	void getVersions() {
		Assertions.assertEquals("Up to Java 17, Spring Boot 3 (Hibernate 6), JUnit 5, PostgreSQL 11",
				formulas.getVersions());
	}
	@Test
	void getFormula_NullName() {
		Assertions.assertTrue(formulas.getFormula(null).isEmpty());
	}

	@Test
	void getFormula_NotValidName() {
		Assertions.assertTrue(formulas.getFormula("xyz").isEmpty());
	}

	@Test
	void getFormula_Title() {
		String expected = "=".repeat(81) + "\nTITLE\n" + "=".repeat(81) + "\n";
		Assertions.assertEquals(expected, formulas.getFormula("tItLe"));
	}

	@Test
	void getFormula_Header1() {
		String expected = "=".repeat(81) + "\nX. HEADER 1\n" + "=".repeat(81) + "\n";
		Assertions.assertEquals(expected, formulas.getFormula("hEaDeR1"));
	}

	@Test
	void getFormula_Header2() {
		String expected = "X.Y. HEADER 2\n" + "-".repeat(81) + "\n";
		Assertions.assertEquals(expected, formulas.getFormula("hEaDeR2"));
	}

	@Test
	void getFormula_Table() {
		String expected = "||Column header 1|Column header 2||\n" +
				"||Cell 11|Cell 12||\n" +
				"||Cell 21|Cell 22||\n" +
				"||...|...||\n";
		Assertions.assertEquals(expected, formulas.getFormula("tAbLe"));
	}

	@Test
	void getFormula_Example() {
		String expected = "EXAMPLE FOR ...\n" +
				"<codes>\n" +
				"END OF EXAMPLE\n";
		Assertions.assertEquals(expected, formulas.getFormula("eXaMpLe"));
	}

	@Test
	void getFormula_Reference() {
		String expected = "=>file_name.html[;HEADER]";
		Assertions.assertEquals(expected, formulas.getFormula("rEfErEnCe"));
	}

	@Test
	void getFormula_More() {
		String expected = "MORE HERE: ";
		Assertions.assertEquals(expected, formulas.getFormula("mOrE"));
	}

	@Test
	void generateTabInSpaces() {
		Assertions.assertEquals(formulas.getTabInSpaces().repeat(3), formulas.generateTabInSpaces(3));
	}
}
