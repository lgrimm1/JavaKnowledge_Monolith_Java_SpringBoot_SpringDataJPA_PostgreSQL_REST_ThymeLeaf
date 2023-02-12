package lgrimm1.JavaKnowledge.Process;

import lgrimm1.JavaKnowledge.Title.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.when;

class ExtractorsTest {

	Extractors extractors = new Extractors();
	TitleRepository titleRepository;
	Formulas formulas;

	@BeforeEach
	void setUp() {
		formulas = Mockito.mock(Formulas.class);
		when(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES))
				.thenReturn("TABINSPACES");
		when(formulas.getConstant(Formulas.ConstantName.SUPERLINE))
				.thenReturn("SUPERLINE");
		titleRepository = Mockito.mock(TitleRepository.class);
	}

	@Test
	void extractReferenceNoHeader() {
		String line = "=>Title Text";

		when(titleRepository.findByTitle("Title Text"))
				.thenReturn(Optional.of(new TitleEntity(
						1L,
						"Title Text",
						"title_text",
						1L,
						1L
				)));
		Assertions.assertEquals(
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"<a href=\"title_text.html\">See: Title Text</a></br>",
				extractors.extractReference(line, formulas, titleRepository)
		);

		when(titleRepository.findByTitle("Title Text"))
				.thenReturn(Optional.empty());
		Assertions.assertEquals(
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"See: Title Text</br>",
				extractors.extractReference(line, formulas, titleRepository)
		);
	}

	@Test
	void extractReferenceWithHeader() {
		String line = "=>Title Text;1.2. Header Word";

		when(titleRepository.findByTitle("Title Text"))
				.thenReturn(Optional.of(new TitleEntity(1L,
						"Title Text",
						"title_text",
						1L,
						1L
				)));
		Assertions.assertEquals(
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"<a href=\"title_text.html#1.2. Header Word\">See: Title Text / 1.2. Header Word</a></br>",
				extractors.extractReference(line, formulas, titleRepository)
		);

		when(titleRepository.findByTitle("Title Text"))
				.thenReturn(Optional.empty());
		Assertions.assertEquals(
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"See: Title Text / 1.2. Header Word</br>",
				extractors.extractReference(line, formulas, titleRepository)
		);
	}

	@Test
	void extractTable() {
		List<String> originalText = List.of(
				"||Header 1|Header 2|Header 3||",
				"||Cell 11|Cell 12|Cell 13||",
				"||Cell 21|Cell 22|Cell 23||"
		);
		List<String> expectedHtml = List.of(
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<table>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<tr>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<th>Header 1</th>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<th>Header 2</th>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<th>Header 3</th>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "</tr>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<tr>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<td>Cell 11</td>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<td>Cell 12</td>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<td>Cell 13</td>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "</tr>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<tr>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<td>Cell 21</td>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<td>Cell 22</td>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<td>Cell 23</td>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "</tr>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "</table>"
		);
		Assertions.assertIterableEquals(expectedHtml, extractors.extractTable(originalText, formulas));
	}

	@Test
	void extractTitleWithEmptyText() {
		List<String> originalText = new ArrayList<>();
		Assertions.assertTrue(extractors.extractTitle(originalText, formulas).isEmpty());
	}

	@Test
	void extractTitleWithNoLegalTitle() {
		List<String> originalText = List.of(
				"abcdef",
				"",
				"xyz"
		);
		Assertions.assertTrue(extractors.extractTitle(originalText, formulas).isEmpty());
	}

	@Test
	void extractTitleWithLegalTitle() {
		List<String> originalText = List.of(
				formulas.getConstant(Formulas.ConstantName.SUPERLINE),
				"Title Text",
				formulas.getConstant(Formulas.ConstantName.SUPERLINE)
		);
		Assertions.assertEquals("Title Text".toUpperCase(), extractors.extractTitle(originalText, formulas));
	}
}