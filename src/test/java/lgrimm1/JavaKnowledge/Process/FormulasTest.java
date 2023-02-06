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
		//TODO test getFormula
	}
}
