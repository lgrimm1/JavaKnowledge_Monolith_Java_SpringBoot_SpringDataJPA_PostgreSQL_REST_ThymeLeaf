package lgrimm1.javaknowledge.process;

import org.junit.jupiter.api.*;

import java.util.*;

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
		Assertions.assertEquals("Up to Java 17, Spring Boot 3 (Hibernate 6), JUnit 5, PostgreSQL 11, HTML 5, CSS 3.",
				formulas.getVersions());
	}

	@Test
	void getHeaderSeparator() {
		Assertions.assertEquals(";", formulas.getHeaderSeparator());
	}

	@Test
	void getBulletWithSpaces() {
		Assertions.assertEquals("    - ", formulas.getBulletWithSpaces());
	}

	@Test
	void getBulletWithTab() {
		Assertions.assertEquals("\t- ", formulas.getBulletWithTab());
	}

	@Test
	void getMore() {
		Assertions.assertEquals("MORE HERE: ", formulas.getMore());
	}

	@Test
	void getTableStart() {
		Assertions.assertEquals("||", formulas.getTableStart());
	}

	@Test
	void getExampleStart() {
		Assertions.assertEquals("EXAMPLE FOR ", formulas.getExampleStart());
	}

	@Test
	void getExampleEnd() {
		Assertions.assertEquals("END OF EXAMPLE", formulas.getExampleEnd());
	}

	@Test
	void getReference() {
		Assertions.assertEquals("=>", formulas.getReference());
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
		List<String> expected = List.of(
				"=".repeat(81),
				"TITLE",
				"=".repeat(81));
		Assertions.assertEquals(expected, formulas.getFormula("tItLe"));
	}

	@Test
	void getFormula_Header1() {
		List<String> expected = List.of(
				"=".repeat(81),
				"X. HEADER 1",
				"=".repeat(81));
		Assertions.assertEquals(expected, formulas.getFormula("hEaDeR1"));
	}

	@Test
	void getFormula_Header2() {
		List<String> expected = List.of(
				"X.Y. HEADER 2",
				"-".repeat(81));
		Assertions.assertEquals(expected, formulas.getFormula("hEaDeR2"));
	}

	@Test
	void getFormula_Table() {
		List<String> expected = List.of(
				"||COLUMN HEADER 1|COLUMN HEADER 2||",
				"||Cell 11|Cell 12||",
				"||Cell 21|Cell 22||",
				"||...|...||");
		Assertions.assertEquals(expected, formulas.getFormula("tAbLe"));
	}

	@Test
	void getFormula_Example() {
		List<String> expected = List.of(
				"EXAMPLE FOR ...",
				"<codes>",
				"END OF EXAMPLE");
		Assertions.assertEquals(expected, formulas.getFormula("eXaMpLe"));
	}

	@Test
	void getFormula_Reference() {
		List<String> expected = List.of("=>TITLE[;HEADER]");
		Assertions.assertEquals(expected, formulas.getFormula("rEfErEnCe"));
	}

	@Test
	void getFormula_More() {
		List<String> expected = List.of("MORE HERE: ...");
		Assertions.assertEquals(expected, formulas.getFormula("mOrE"));
	}

	@Test
	void getFormula_List() {
		List<String> expected = List.of("    - ...");
		Assertions.assertEquals(expected, formulas.getFormula("lIsT"));
	}

	@Test
	void getFormula_Tab() {
		List<String> expected = List.of("    ...");
		Assertions.assertEquals(expected, formulas.getFormula("tAb"));
	}

	@Test
	void getFormula_Tab2() {
		List<String> expected = List.of("        ...");
		Assertions.assertEquals(expected, formulas.getFormula("tAb2"));
	}

	@Test
	void getFormula_Tab3() {
		List<String> expected = List.of("            ...");
		Assertions.assertEquals(expected, formulas.getFormula("tAb3"));
	}

	@Test
	void getTitleRoot() {
		Assertions.assertEquals("WELCOME TO JAVA KNOWLEDGE!", formulas.getTitleRoot());
	}

	@Test
	void getTitleList() {
		Assertions.assertEquals("LIST OF TITLES", formulas.getTitleList());
	}

	@Test
	void getTitlePage() {
		Assertions.assertEquals("KNOWLEDGE PAGE", formulas.getTitlePage());
	}

	@Test
	void getTitleManagement() {
		Assertions.assertEquals("JAVA KNOWLEDGE MANAGEMENT", formulas.getTitleManagement());
	}

	@Test
	void getTitleSource() {
		Assertions.assertEquals("JAVA KNOWLEDGE SOURCE PAGE", formulas.getTitleSource());
	}

	@Test
	void generateTabInSpaces() {
		Assertions.assertEquals(formulas.getTabInSpaces().repeat(3), formulas.generateTabInSpaces(3));
	}
}
