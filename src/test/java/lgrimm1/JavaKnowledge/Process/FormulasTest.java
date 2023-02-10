package lgrimm1.JavaKnowledge.Process;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class FormulasTest {

	Formulas formulas = new Formulas();

	@Test
	void generateRepeatedPattern() {
		Assertions.assertEquals("abcXYZabcXYZabcXYZ",
				formulas.generateRepeatedPattern("abcXYZ", 3));
	}

	@Test
	void getFormula() {
		Assertions.assertTrue(formulas.getFormula(null).isEmpty());
		Assertions.assertEquals(formulas.FORMULA_TITLE, formulas.getFormula("TITLE"));
		Assertions.assertEquals(formulas.FORMULA_TITLE, formulas.getFormula("title"));
		Assertions.assertEquals(formulas.FORMULA_HEADER_1, formulas.getFormula("HEADER1"));
		Assertions.assertEquals(formulas.FORMULA_HEADER_1, formulas.getFormula("header1"));
		Assertions.assertEquals(formulas.FORMULA_HEADER_2, formulas.getFormula("HEADER2"));
		Assertions.assertEquals(formulas.FORMULA_HEADER_2, formulas.getFormula("header2"));
		Assertions.assertEquals(formulas.FORMULA_TABLE, formulas.getFormula("TABLE"));
		Assertions.assertEquals(formulas.FORMULA_TABLE, formulas.getFormula("table"));
		Assertions.assertEquals(formulas.FORMULA_EXAMPLE, formulas.getFormula("EXAMPLE"));
		Assertions.assertEquals(formulas.FORMULA_EXAMPLE, formulas.getFormula("example"));
		Assertions.assertEquals(formulas.FORMULA_REFERENCE, formulas.getFormula("REFERENCE"));
		Assertions.assertEquals(formulas.FORMULA_REFERENCE, formulas.getFormula("reference"));
		Assertions.assertTrue(formulas.getFormula("xyz").isEmpty());
	}
}
