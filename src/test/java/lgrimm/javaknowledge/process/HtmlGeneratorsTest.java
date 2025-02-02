package lgrimm.javaknowledge.process;

import lgrimm.javaknowledge.datamodels.HtmlContentAndReferences;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

class HtmlGeneratorsTest {

	Extractors extractors;
	Formulas formulas;
	HtmlGenerators htmlGenerators;

	@BeforeEach
	void setUp() {
		extractors = Mockito.mock(Extractors.class);
		formulas = Mockito.mock(Formulas.class);
		when(formulas.getSuperLine())
				.thenReturn("SuperLine");
		when(formulas.getSubLine())
				.thenReturn("SubLine");
		when(formulas.getTabInSpaces())
				.thenReturn("TabInSpaces");
		when(formulas.getTabInHtml())
				.thenReturn("TabInHtml");
		when(formulas.getVersions())
				.thenReturn("Versions");
		when(formulas.generateTabInSpaces(2))
				.thenReturn("TabInSpacesTabInSpaces");
		when(formulas.generateTabInSpaces(3))
				.thenReturn("TabInSpacesTabInSpacesTabInSpaces");
		when(formulas.generateTabInSpaces(4))
				.thenReturn("TabInSpacesTabInSpacesTabInSpacesTabInSpaces");
		when(formulas.getMore())
				.thenReturn("More");
		when(formulas.getTableStart())
				.thenReturn("TableStart");
		when(formulas.getExampleStart())
				.thenReturn("ExampleStart");
		when(formulas.getExampleEnd())
				.thenReturn("ExampleEnd");
		when(formulas.getReference())
				.thenReturn("Reference");
		when(formulas.getBulletWithSpaces())
				.thenReturn("TabInSpaces- ");
		when(formulas.getBulletWithTab())
				.thenReturn("\t- ");
		htmlGenerators = new HtmlGenerators(extractors, formulas);
/*
		titleRepository = Mockito.mock(TitleRepository.class);
*/
	}

	@Test
	void generateMainContent_Header1() {
		String header1 = "1. Header 1 text";
		List<String> txtContent = List.of(
				"SuperLine",
				header1,
				"SuperLine"
		);

		List<String> expectedHtml = List.of(
				"TabInSpaces" + "<a href=\"#top\"><i>Back to top of page</i></a><br>",
				"TabInSpaces" + "<a id=\"" + header1 + "\"></a>",
				"TabInSpaces" + "<h2>" + header1 + "</h2>"
		);

		HtmlContentAndReferences actualPayload = htmlGenerators.generateMainContent(txtContent);
		Assertions.assertEquals(expectedHtml, actualPayload.content());
		Assertions.assertTrue(actualPayload.titles().isEmpty());
	}

	@Test
	void generateMainContent_Header2() {
		String header2 = "1.2. Header 2 text";
		List<String> txtContent = List.of(
				header2,
				"SubLine"
		);

		List<String> expectedHtml = List.of(
				"TabInSpaces" + "<a href=\"#top\"><i>Back to top of page</i></a><br>",
				"TabInSpaces" + "<a id=\"" + header2 + "\"></a>",
				"TabInSpaces" + "<h3>" + header2 + "</h3>"
		);

		HtmlContentAndReferences actualPayload = htmlGenerators.generateMainContent(txtContent);
		Assertions.assertEquals(expectedHtml, actualPayload.content());
		Assertions.assertTrue(actualPayload.titles().isEmpty());
	}

	@Test
	void generateMainContent_Table() {
		List<String> txtContent = List.of(
				"TableStartHeader 1|Header 2|Header 3TableStart",
				"TableStartCell 11|Cell 12|Cell 13TableStart",
				"TableStartCell 21|Cell 22|Cell 23TableStart"
		);

		when(extractors.extractTable(List.of(
				"TableStartHeader 1|Header 2|Header 3TableStart",
				"TableStartCell 11|Cell 12|Cell 13TableStart",
				"TableStartCell 21|Cell 22|Cell 23TableStart"
		)))
				.thenReturn(List.of(
						"HTML table"
				));

		List<String> expectedHtml = List.of(
				"HTML table"
		);

		HtmlContentAndReferences actualPayload = htmlGenerators.generateMainContent(txtContent);
		Assertions.assertEquals(expectedHtml, actualPayload.content());
		Assertions.assertTrue(actualPayload.titles().isEmpty());
	}

	@Test
	void generateMainContent_ExampleWithoutClosingText() {
		List<String> txtContent = List.of(
				"ExampleStart",
				"first row",
				"  second row",
				"some other rows"
		);

		List<String> expectedHtml = List.of(
				"EXAMPLE HEADER",
				"first row",
				"  second row",
				"some other rows"
		);

		int exampleCounter = 1;
		when(extractors.extractExample(txtContent, exampleCounter))
				.thenReturn(expectedHtml);

		HtmlContentAndReferences actualPayload = htmlGenerators.generateMainContent(txtContent);
		Assertions.assertEquals(expectedHtml, actualPayload.content());
		Assertions.assertTrue(actualPayload.titles().isEmpty());
	}

	@Test
	void generateMainContent_ExampleWithClosingText() {
		List<String> txtContent = List.of(
				"ExampleStart",
				"first row",
				"  second row",
				"ExampleEnd"
		);

		List<String> expectedHtml = List.of(
				"EXAMPLE HEADER",
				"first row",
				"  second row"
		);

		int exampleCounter = 1;
		when(extractors.extractExample(txtContent.subList(0, 3), exampleCounter))
				.thenReturn(expectedHtml);

		HtmlContentAndReferences actualPayload = htmlGenerators.generateMainContent(txtContent);
		Assertions.assertEquals(expectedHtml, actualPayload.content());
		Assertions.assertTrue(actualPayload.titles().isEmpty());
	}

	@Test
	void generateMainContent_InternalReference() {
		List<String> txtContent = List.of(
				"ReferenceTitle Word 1",
				"ReferenceTitle Word 2;1.2. Header header"
		);

		when(extractors.extractReference("ReferenceTitle Word 1"))
				.thenReturn("Existing reference 1");
		when(extractors.extractReference("ReferenceTitle Word 2;1.2. Header header"))
				.thenReturn("Existing reference 2");

		List<String> expectedHtml = List.of(
				"Existing reference 1",
				"Existing reference 2"
		);
		List<String> expectedTitles = List.of(
				"Title Word 1",
				"Title Word 2;1.2. Header header"
		);

		HtmlContentAndReferences actualPayload = htmlGenerators.generateMainContent(txtContent);
		Assertions.assertEquals(expectedHtml, actualPayload.content());
		Assertions.assertEquals(expectedTitles, actualPayload.titles());
	}

	@Test
	void generateMainContent_ExternalReference() {
		List<String> txtContent = List.of(
				"MoreExternal reference text"
		);

		List<String> expectedHtml = new ArrayList<>();

		HtmlContentAndReferences actualPayload = htmlGenerators.generateMainContent(txtContent);
		Assertions.assertEquals(expectedHtml, actualPayload.content());
		Assertions.assertTrue(actualPayload.titles().isEmpty());
	}

	@Test
	void generateMainContent_BulletedList() {
		List<String> txtContent = List.of(
				"TabInSpaces- List item 1",
				"\t- List item 2",
				"TabInSpaces- List item 3"
		);
		when(extractors.extractBulletedList(txtContent))
				.thenReturn(List.of(
						"List"
				));
		List<String> expectedHtml = List.of(
				"List"
		);

		HtmlContentAndReferences actualPayload = htmlGenerators.generateMainContent(txtContent);
		Assertions.assertEquals(expectedHtml, actualPayload.content());
		Assertions.assertTrue(actualPayload.titles().isEmpty());
	}

	@Test
	void generateMainContent_NormalText() {
		List<String> txtContent = List.of(
				"Line 1",
				"TabInSpaces" + "Line 2",
				"TabInSpaces" + "  Line 3",
				"  Line 4",
				"\t<tag>" + "TabInSpaces",
				"  ",
				""
		);

		List<String> expectedHtml = List.of(
				"TabInSpaces" + "<p>" + "Line 1</p>",
				"TabInSpaces" + "<p>" + "TabInHtml" + "Line 2</p>",
				"TabInSpaces" + "<p>" + "TabInHtml" + "  Line 3</p>",
				"TabInSpaces" + "<p>" + "Line 4</p>",
				"TabInSpaces" + "<p>" + "TabInHtml" + "&lt;tag>" + "TabInHtml</p>",
				"TabInSpaces" + "<br>",
				"TabInSpaces" + "<br>"
		);

		HtmlContentAndReferences actualPayload = htmlGenerators.generateMainContent(txtContent);
		Assertions.assertEquals(expectedHtml, actualPayload.content());
		Assertions.assertTrue(actualPayload.titles().isEmpty());
	}

	@Test
	void generateFirstTags() {
		String title = "TITLE TITLE";
		List<String> neededFirstTags = List.of(
				"TabInSpaces" + "<i>Versions" + "</i><br>",
				"TabInSpaces" + "<h1>" + title + "</h1>"
		);
		Assertions.assertEquals(neededFirstTags, htmlGenerators.generateFirstTags(title));
	}

	@Test
	void generateLastTags() {
		List<String> neededLastTags = List.of(
				"TabInSpaces" + "<br>",
				"TabInSpaces" + "<a href=\"#top\"><i>Back to top of page</i></a><br>"
		);
		Assertions.assertEquals(neededLastTags, htmlGenerators.generateLastTags());
	}

	@Test
	void collectAndReferencingHeaders() {
		List<String> html = List.of(
				formulas.getTabInSpaces() + "<p>Text 1</p>",
				formulas.getTabInSpaces() + "<p>Text 2</p>",
				formulas.getTabInSpaces() + "<p>Header 1</p>",
				formulas.getTabInSpaces() + "<p>Header 2</p>",
				formulas.getTabInSpaces() + "<br>",
				formulas.getTabInSpaces() + "<h2>Header 1</h2>",
				formulas.getTabInSpaces() + "<p>Text 3</p>",
				formulas.getTabInSpaces() + "<br>",
				formulas.getTabInSpaces() + "<h3>Header 2</h3>",
				formulas.getTabInSpaces() + "<p>Text 4</p>"
		);
		List<String> needed = List.of(
				formulas.getTabInSpaces() + "<p>Text 1</p>",
				formulas.getTabInSpaces() + "<p>Text 2</p>",
				formulas.getTabInSpaces() + "<a href=\"#Header 1\"><i>Header 1</i></a><br>",
				formulas.getTabInSpaces() + "<a href=\"#Header 2\"><i>Header 2</i></a><br>",
				formulas.getTabInSpaces() + "<br>",
				formulas.getTabInSpaces() + "<h2>Header 1</h2>",
				formulas.getTabInSpaces() + "<p>Text 3</p>",
				formulas.getTabInSpaces() + "<br>",
				formulas.getTabInSpaces() + "<h3>Header 2</h3>",
				formulas.getTabInSpaces() + "<p>Text 4</p>"
		);
		Assertions.assertEquals(needed, htmlGenerators.collectAndReferenceHeaders(html));
	}

}