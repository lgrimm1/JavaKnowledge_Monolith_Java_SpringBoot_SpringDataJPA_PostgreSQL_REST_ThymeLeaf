package lgrimm1.javaknowledge.process;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

class ExtractorsTest {

	Formulas formulas;
	Extractors extractors;

	@BeforeEach
	void setUp() {
		formulas = Mockito.mock(Formulas.class);
		when(formulas.getTabInSpaces())
				.thenReturn("TABINSPACES");
		when(formulas.getSuperLine())
				.thenReturn("SUPERLINE");
		when(formulas.getHeaderSeparator())
				.thenReturn(";");
		when(formulas.generateTabInSpaces(0))
				.thenReturn("");
		when(formulas.generateTabInSpaces(1))
				.thenReturn("TABINSPACES");
		when(formulas.generateTabInSpaces(2))
				.thenReturn("TABINSPACESTABINSPACES");
		when(formulas.generateTabInSpaces(3))
				.thenReturn("TABINSPACESTABINSPACESTABINSPACES");
		when(formulas.generateTabInSpaces(4))
				.thenReturn("TABINSPACESTABINSPACESTABINSPACESTABINSPACES");
		when(formulas.getBulletWithSpaces())
				.thenReturn("BULLETWITHSPACES");
		when(formulas.getBulletWithTab())
				.thenReturn("BULLETWITHTAB");
		when(formulas.getHeaderSeparator())
				.thenReturn("HEADERSEPARATOR");
		when(formulas.getTableStart())
				.thenReturn("TABLESTART");
		when(formulas.getReference())
				.thenReturn("REFERENCE");
		extractors = new Extractors(formulas);
	}

	@Test
	void extractTitle_EmptyText() {
		List<String> originalText = new ArrayList<>();
		Assertions.assertTrue(extractors.extractTitle(originalText).isEmpty());
	}

	@Test
	void extractTitle_NoValidTitle() {
		List<String> originalText = List.of(
				"abcdef",
				"",
				"xyz"
		);
		Assertions.assertTrue(extractors.extractTitle(originalText).isEmpty());
	}

	@Test
	void extractTitle_ValidTitle() {
		List<String> originalText = List.of(
				"SUPERLINE",
				"Title Text",
				"SUPERLINE"
		);
		Assertions.assertEquals("Title Text".toUpperCase(), extractors.extractTitle(originalText));
	}

	@Test
	void extractTable() {
		List<String> originalText = List.of(
				"TABLESTARTHeader 1|Header 2|Header 3TABLESTART",
				"TABLESTARTCell 11|Cell 12|Cell 13TABLESTART",
				"TABLESTARTCell 21|Cell 22|Cell 23TABLESTART"
		);
		List<String> expectedHtml = List.of(
				"TABINSPACES" + "<table class=\"table\">",
				"TABINSPACES" + "TABINSPACES" + "<tr>",
				"TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "<th class=\"table_th\"><i>Header 1</i></th>",
				"TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "<th class=\"table_th\"><i>Header 2</i></th>",
				"TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "<th class=\"table_th\"><i>Header 3</i></th>",
				"TABINSPACES" + "TABINSPACES" + "</tr>",
				"TABINSPACES" + "TABINSPACES" + "<tr>",
				"TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "<td class=\"table_td\">Cell 11</td>",
				"TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "<td class=\"table_td\">Cell 12</td>",
				"TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "<td class=\"table_td\">Cell 13</td>",
				"TABINSPACES" + "TABINSPACES" + "</tr>",
				"TABINSPACES" + "TABINSPACES" + "<tr>",
				"TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "<td class=\"table_td\">Cell 21</td>",
				"TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "<td class=\"table_td\">Cell 22</td>",
				"TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "<td class=\"table_td\">Cell 23</td>",
				"TABINSPACES" + "TABINSPACES" + "</tr>",
				"TABINSPACES" + "</table>"
		);
		Assertions.assertIterableEquals(expectedHtml, extractors.extractTable(originalText));
	}

	@Test
	void extractExample() {
		List<String> originalText = List.of(
				"EXAMPLE FOR SOMETHING",
				"Line 1",
				"    Line 2"
		);
		int exampleCounter = 2;
		List<String> expectedHtml = new ArrayList<>();
		expectedHtml.add("TABINSPACES" + "<h4>" + originalText.get(0) + "</h4>");
		expectedHtml.add("TABINSPACES" + "<table class=\"formatter_table\">");
		expectedHtml.add("TABINSPACES" + "TABINSPACES" + "<tr>");
		expectedHtml.add("TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "<td style=\"width: 85%\">");
		expectedHtml.add("<textarea id=\"example_2\" onclick=\"element_to_full_size(this)\" readonly>");
		expectedHtml.add("Line 1");
		expectedHtml.add("    Line 2");
		expectedHtml.add("</textarea>");
		expectedHtml.add("TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "</td>");
		expectedHtml.add("TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "<td style=\"width: 15%\">");
		expectedHtml.add("TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "TABINSPACES" +
				"<input type=\"button\" value=\"COPY\" class=\"button_full\" onclick=\"content_to_clipboard('example_2')\" />");
		expectedHtml.add("TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "</td>");
		expectedHtml.add("TABINSPACES" + "TABINSPACES" + "</tr>");
		expectedHtml.add("TABINSPACES" + "</table>");

		Assertions.assertEquals(expectedHtml, extractors.extractExample(originalText, exampleCounter));
	}

	@Test
	void extractReference_NoHeader() {
		String line = "REFERENCETitle Text";
		Assertions.assertEquals(
				"TABINSPACES" + "<p><i>See: Title Text</i></p>",
				extractors.extractReference(line)
		);
	}

	@Test
	void extractReference_WithHeader() {
		String line = "REFERENCETitle TextHEADERSEPARATOR1.2. Header Word";
		Assertions.assertEquals(
				"TABINSPACES" + "<p><i>See: Title Text / 1.2. Header Word</i></p>",
				extractors.extractReference(line)
		);
	}

	@Test
	void extractBulletedList() {
		List<String> originalText = List.of(
				"BULLETWITHSPACESList item 1",
				"BULLETWITHTABList item 2",
				"BULLETWITHSPACESList item 3"
		);
		List<String> expectedHtml = new ArrayList<>();
		expectedHtml.add("TABINSPACES" + "<ol>");
		expectedHtml.add("TABINSPACES" + "TABINSPACES" + "<li>List item 1</li>");
		expectedHtml.add("TABINSPACES" + "TABINSPACES" + "<li>List item 2</li>");
		expectedHtml.add("TABINSPACES" + "TABINSPACES" + "<li>List item 3</li>");
		expectedHtml.add("TABINSPACES" + "</ol>");

		Assertions.assertEquals(expectedHtml, extractors.extractBulletedList(originalText));
	}
}