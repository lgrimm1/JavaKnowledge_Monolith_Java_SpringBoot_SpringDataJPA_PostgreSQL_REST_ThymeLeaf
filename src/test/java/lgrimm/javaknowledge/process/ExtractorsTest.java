package lgrimm.javaknowledge.process;

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
				.thenReturn("TabInSpaces");
		when(formulas.getSuperLine())
				.thenReturn("SuperLine");
		when(formulas.getHeaderSeparator())
				.thenReturn(";");
		when(formulas.generateTabInSpaces(0))
				.thenReturn("");
		when(formulas.generateTabInSpaces(1))
				.thenReturn("TabInSpaces");
		when(formulas.generateTabInSpaces(2))
				.thenReturn("TabInSpacesTabInSpaces");
		when(formulas.generateTabInSpaces(3))
				.thenReturn("TabInSpacesTabInSpacesTabInSpaces");
		when(formulas.generateTabInSpaces(4))
				.thenReturn("TabInSpacesTabInSpacesTabInSpacesTabInSpaces");
		when(formulas.getBulletWithSpaces())
				.thenReturn("BulletWithSpaces");
		when(formulas.getBulletWithTab())
				.thenReturn("BulletWithTab");
		when(formulas.getHeaderSeparator())
				.thenReturn("HeaderSeparator");
		when(formulas.getTableStart())
				.thenReturn("TableStart");
		when(formulas.getReference())
				.thenReturn("Reference");
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
				"abc",
				"",
				"xyz"
		);
		Assertions.assertTrue(extractors.extractTitle(originalText).isEmpty());
	}

	@Test
	void extractTitle_ValidTitle() {
		List<String> originalText = List.of(
				"SuperLine",
				"Title Text",
				"SuperLine"
		);
		Assertions.assertEquals("Title Text".toUpperCase(), extractors.extractTitle(originalText));
	}

	@Test
	void extractTable() {
		List<String> originalText = List.of(
				"TableStartHeader 1|Header 2|Header 3TableStart",
				"TableStartCell 11|Cell 12|Cell 13TableStart",
				"TableStartCell 21|Cell 22|Cell 23TableStart"
		);
		List<String> expectedHtml = List.of(
				"TabInSpaces" + "<table class=\"table\">",
				"TabInSpaces" + "TabInSpaces" + "<tr>",
				"TabInSpaces" + "TabInSpaces" + "TabInSpaces" + "<th class=\"table_th\"><i>Header 1</i></th>",
				"TabInSpaces" + "TabInSpaces" + "TabInSpaces" + "<th class=\"table_th\"><i>Header 2</i></th>",
				"TabInSpaces" + "TabInSpaces" + "TabInSpaces" + "<th class=\"table_th\"><i>Header 3</i></th>",
				"TabInSpaces" + "TabInSpaces" + "</tr>",
				"TabInSpaces" + "TabInSpaces" + "<tr>",
				"TabInSpaces" + "TabInSpaces" + "TabInSpaces" + "<td class=\"table_td\">Cell 11</td>",
				"TabInSpaces" + "TabInSpaces" + "TabInSpaces" + "<td class=\"table_td\">Cell 12</td>",
				"TabInSpaces" + "TabInSpaces" + "TabInSpaces" + "<td class=\"table_td\">Cell 13</td>",
				"TabInSpaces" + "TabInSpaces" + "</tr>",
				"TabInSpaces" + "TabInSpaces" + "<tr>",
				"TabInSpaces" + "TabInSpaces" + "TabInSpaces" + "<td class=\"table_td\">Cell 21</td>",
				"TabInSpaces" + "TabInSpaces" + "TabInSpaces" + "<td class=\"table_td\">Cell 22</td>",
				"TabInSpaces" + "TabInSpaces" + "TabInSpaces" + "<td class=\"table_td\">Cell 23</td>",
				"TabInSpaces" + "TabInSpaces" + "</tr>",
				"TabInSpaces" + "</table>"
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
		expectedHtml.add("TabInSpaces" + "<h4>" + originalText.get(0) + "</h4>");
		expectedHtml.add("TabInSpaces" + "<table class=\"formatter_table\">");
		expectedHtml.add("TabInSpaces" + "TabInSpaces" + "<tr>");
		expectedHtml.add("TabInSpaces" + "TabInSpaces" + "TabInSpaces" + "<td style=\"width: 85%\">");
		expectedHtml.add("<textarea id=\"example_2\" onclick=\"element_to_full_size(this)\" readonly>");
		expectedHtml.add("Line 1");
		expectedHtml.add("    Line 2");
		expectedHtml.add("</textarea>");
		expectedHtml.add("TabInSpaces" + "TabInSpaces" + "TabInSpaces" + "</td>");
		expectedHtml.add("TabInSpaces" + "TabInSpaces" + "TabInSpaces" + "<td style=\"width: 15%\">");
		expectedHtml.add("TabInSpaces" + "TabInSpaces" + "TabInSpaces" + "TabInSpaces" +
				"<input type=\"button\" value=\"COPY\" class=\"button_full\" onclick=\"content_to_clipboard('example_2')\" />");
		expectedHtml.add("TabInSpaces" + "TabInSpaces" + "TabInSpaces" + "</td>");
		expectedHtml.add("TabInSpaces" + "TabInSpaces" + "</tr>");
		expectedHtml.add("TabInSpaces" + "</table>");

		Assertions.assertEquals(expectedHtml, extractors.extractExample(originalText, exampleCounter));
	}

	@Test
	void extractReference_NoHeader() {
		String line = "ReferenceTitle Text";
		Assertions.assertEquals(
				"TabInSpaces" + "<p><i>See: Title Text</i></p>",
				extractors.extractReference(line)
		);
	}

	@Test
	void extractReference_WithHeader() {
		String line = "ReferenceTitle TextHeaderSeparator1.2. Header Word";
		Assertions.assertEquals(
				"TabInSpaces" + "<p><i>See: Title Text / 1.2. Header Word</i></p>",
				extractors.extractReference(line)
		);
	}

	@Test
	void extractBulletedList() {
		List<String> originalText = List.of(
				"BulletWithSpacesList item 1",
				"BulletWithTabList item 2",
				"BulletWithSpacesList item 3"
		);
		List<String> expectedHtml = new ArrayList<>();
		expectedHtml.add("TabInSpaces" + "<ol>");
		expectedHtml.add("TabInSpaces" + "TabInSpaces" + "<li>List item 1</li>");
		expectedHtml.add("TabInSpaces" + "TabInSpaces" + "<li>List item 2</li>");
		expectedHtml.add("TabInSpaces" + "TabInSpaces" + "<li>List item 3</li>");
		expectedHtml.add("TabInSpaces" + "</ol>");

		Assertions.assertEquals(expectedHtml, extractors.extractBulletedList(originalText));
	}
}