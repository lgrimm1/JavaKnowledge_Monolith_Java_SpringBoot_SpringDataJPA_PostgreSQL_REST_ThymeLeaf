package lgrimm1.JavaKnowledge.Process;

import lgrimm1.JavaKnowledge.Title.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;

class HtmlGeneratorsTest {

	Extractors extractors;
	Formulas formulas;
	TitleRepository titleRepository;
	HtmlGenerators htmlGenerators = new HtmlGenerators();

	@BeforeEach
	void setUp() {
		extractors = Mockito.mock(Extractors.class);
		formulas = Mockito.mock(Formulas.class);
		when(formulas.getSuperLine())
				.thenReturn("SUPERLINE");
		when(formulas.getSubLine())
				.thenReturn("SUBLINE");
		when(formulas.getTabInSpaces())
				.thenReturn("TABINSPACES");
		when(formulas.getTabInHtml())
				.thenReturn("TABINHTML");
		when(formulas.getVersions())
				.thenReturn("VERSIONS");
		when(formulas.generateTabInSpaces(2))
				.thenReturn("TABINSPACESTABINSPACES");
		when(formulas.generateTabInSpaces(3))
				.thenReturn("TABINSPACESTABINSPACESTABINSPACES");
		when(formulas.generateTabInSpaces(4))
				.thenReturn("TABINSPACESTABINSPACESTABINSPACESTABINSPACES");
		when(formulas.getMore())
				.thenReturn("MORE");
		when(formulas.getTableStart())
				.thenReturn("TABLESTART");
		when(formulas.getExampleStart())
				.thenReturn("EXAMPLESTART");
		when(formulas.getExampleEnd())
				.thenReturn("EXAMPLEEND");
		when(formulas.getReference())
				.thenReturn("REFERENCE");
		when(formulas.getBulletWithHtmlSpaces())
				.thenReturn("TABINSPACES- ");
		when(formulas.getBulletWithTab())
				.thenReturn("\t- ");
		titleRepository = Mockito.mock(TitleRepository.class);
	}

	@Test
	void generateMainContent_Header1() {
		String header1 = "1. Header 1 text";
		List<String> txtContent = List.of(
				"SUPERLINE",
				header1,
				"SUPERLINE"
		);

		List<String> expectedHtml = List.of(
				"TABINSPACES" + "<a href=\"#top\"><i>Back to top of page</i></a><br>",
				"TABINSPACES" + "<a name=\"" + header1 + "\"></a>",
				"TABINSPACES" + "<h2>" + header1 + "</h2>"
		);

		MainHtmlContentPayload actualPayload = htmlGenerators.generateMainContent(
				txtContent,
				formulas,
				extractors,
				titleRepository);
		Assertions.assertEquals(expectedHtml, actualPayload.content());
		Assertions.assertTrue(actualPayload.titles().isEmpty());
	}

	@Test
	void generateMainContent_Header2() {
		String header2 = "1.2. Header 2 text";
		List<String> txtContent = List.of(
				header2,
				"SUBLINE"
		);

		List<String> expectedHtml = List.of(
				"TABINSPACES" + "<a href=\"#top\"><i>Back to top of page</i></a><br>",
				"TABINSPACES" + "<a name=\"" + header2 + "\"></a>",
				"TABINSPACES" + "<h3>" + header2 + "</h3>"
		);

		MainHtmlContentPayload actualPayload = htmlGenerators.generateMainContent(
				txtContent,
				formulas,
				extractors,
				titleRepository);
		Assertions.assertEquals(expectedHtml, actualPayload.content());
		Assertions.assertTrue(actualPayload.titles().isEmpty());
	}

	@Test
	void generateMainContent_Table() {
		List<String> txtContent = List.of(
				"TABLESTARTHeader 1|Header 2|Header 3TABLESTART",
				"TABLESTARTCell 11|Cell 12|Cell 13TABLESTART",
				"TABLESTARTCell 21|Cell 22|Cell 23TABLESTART"
		);

		when(extractors.extractTable(List.of(
				"TABLESTARTHeader 1|Header 2|Header 3TABLESTART",
				"TABLESTARTCell 11|Cell 12|Cell 13TABLESTART",
				"TABLESTARTCell 21|Cell 22|Cell 23TABLESTART"
		), formulas))
				.thenReturn(List.of(
						"HTML table"
				));

		List<String> expectedHtml = List.of(
				"HTML table"
		);

		MainHtmlContentPayload actualPayload = htmlGenerators.generateMainContent(
				txtContent,
				formulas,
				extractors,
				titleRepository);
		Assertions.assertEquals(expectedHtml, actualPayload.content());
		Assertions.assertTrue(actualPayload.titles().isEmpty());
	}

	@Test
	void generateMainContent_ExampleWithoutClosingText() {
		List<String> txtContent = List.of(
				"EXAMPLESTART",
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

		when(extractors.extractExample(txtContent, formulas))
				.thenReturn(expectedHtml);

		MainHtmlContentPayload actualPayload = htmlGenerators.generateMainContent(
				txtContent,
				formulas,
				extractors,
				titleRepository);
		Assertions.assertEquals(expectedHtml, actualPayload.content());
		Assertions.assertTrue(actualPayload.titles().isEmpty());
	}

	@Test
	void generateMainContent_ExampleWithClosingText() {
		List<String> txtContent = List.of(
				"EXAMPLESTART",
				"first row",
				"  second row",
				"EXAMPLEEND"
		);

		List<String> expectedHtml = List.of(
				"EXAMPLE HEADER",
				"first row",
				"  second row"
		);

		when(extractors.extractExample(txtContent.subList(0, 3), formulas))
				.thenReturn(expectedHtml);

		MainHtmlContentPayload actualPayload = htmlGenerators.generateMainContent(
				txtContent,
				formulas,
				extractors,
				titleRepository);
		Assertions.assertEquals(expectedHtml, actualPayload.content());
		Assertions.assertTrue(actualPayload.titles().isEmpty());
	}

	@Test
	void generateMainContent_InternalReference() {
		List<String> txtContent = List.of(
				"REFERENCETitle Word 1",
				"REFERENCETitle Word 2;1.2. Header header"
		);

		when(extractors.extractReference("REFERENCETitle Word 1", formulas, titleRepository))
				.thenReturn("Existing reference 1");
		when(extractors.extractReference("REFERENCETitle Word 2;1.2. Header header", formulas, titleRepository))
				.thenReturn("Existing reference 2");

		List<String> expectedHtml = List.of(
				"Existing reference 1",
				"Existing reference 2"
		);
		List<String> expectedTitles = List.of(
				"Title Word 1",
				"Title Word 2;1.2. Header header"
		);

		MainHtmlContentPayload actualPayload = htmlGenerators.generateMainContent(
				txtContent,
				formulas,
				extractors,
				titleRepository);
		Assertions.assertEquals(expectedHtml, actualPayload.content());
		Assertions.assertEquals(expectedTitles, actualPayload.titles());
	}

	@Test
	void generateMainContent_ExternalReference() {
		List<String> txtContent = List.of(
				"MOREexternal reference text"
		);

		List<String> expectedHtml = new ArrayList<>();

		MainHtmlContentPayload actualPayload = htmlGenerators.generateMainContent(
				txtContent,
				formulas,
				extractors,
				titleRepository);
		Assertions.assertEquals(expectedHtml, actualPayload.content());
		Assertions.assertTrue(actualPayload.titles().isEmpty());
	}

	@Test
	void generateMainContent_BulletedList() {
		List<String> txtContent = List.of(
				"TABINSPACES- List item 1",
				"TABINSPACES- List item 2",
				"TABINSPACES- List item 3"
		);
		List<String> txtContentWithHtmlSpaces = List.of(
				"TABINHTML- List item 1",
				"TABINHTML- List item 2",
				"TABINHTML- List item 3"
		);
		when(extractors.extractBulletedList(txtContentWithHtmlSpaces, formulas))
				.thenReturn(List.of(
						"List"
				));
		List<String> expectedHtml = List.of(
				"List"
		);

		MainHtmlContentPayload actualPayload = htmlGenerators.generateMainContent(
				txtContent,
				formulas,
				extractors,
				titleRepository);
		Assertions.assertEquals(expectedHtml, actualPayload.content());
		Assertions.assertTrue(actualPayload.titles().isEmpty());
	}

	@Test
	void generateMainContent_NormalText() {
		List<String> txtContent = List.of(
				"Line 1",
				"TABINSPACES" + "Line 2",
				"TABINSPACES" + "  Line 3",
				"  Line 4",
				"\t<tag>" + "TABINSPACES",
				"  ",
				""
		);

		List<String> expectedHtml = List.of(
				"TABINSPACES" + "<p>" + "Line 1</p>",
				"TABINSPACES" + "<p>" + "TABINHTML" + "Line 2</p>",
				"TABINSPACES" + "<p>" + "TABINHTML" + "  Line 3</p>",
				"TABINSPACES" + "<p>" + "Line 4</p>",
				"TABINSPACES" + "<p>" + "TABINHTML" + "&lt;tag>" + "TABINHTML</p>",
				"TABINSPACES" + "<br>",
				"TABINSPACES" + "<br>"
		);

		MainHtmlContentPayload actualPayload = htmlGenerators.generateMainContent(
				txtContent,
				formulas,
				extractors,
				titleRepository);
		Assertions.assertEquals(expectedHtml, actualPayload.content());
		Assertions.assertTrue(actualPayload.titles().isEmpty());
	}

	@Test
	void generateFirstTags() {
		String title = "TITLE TITLE";
		List<String> neededFirstTags = List.of(
/*
				"<!DOCTYPE html>",
				"<html lang=\"en\">",
				"<head>",
				"TABINSPACES" + "<title>" + title + "</title>",
				"TABINSPACES" + "<meta charset=\"UTF-8\">",
				"TABINSPACES" + "<link rel=\"icon\" type=\"image/x-icon\" th:href=\"@{/images/favicon.ico}\" href=\"/images/favicon.ico\">",
				"TABINSPACES" + "<link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">",
				"TABINSPACES" + "<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>",
				"TABINSPACES" + "<link href=\"https://fonts.googleapis.com/css2?family=Roboto+Mono&display=swap\" rel=\"stylesheet\">",
				"TABINSPACES" + "<link rel=\"stylesheet\" th:href=\"@{/styles/desktop.css}\" href=\"/styles/desktop.css\">",
				"</head>",
				"<body>",
				"TABINSPACES" + "<a name=\"top\"></a>",
*/
				"TABINSPACES" + "<i>VERSIONS" + "</i><br>",
				"TABINSPACES" + "<h1>" + title + "</h1>"
		);
		Assertions.assertEquals(neededFirstTags, htmlGenerators.generateFirstTags(title, formulas));
	}

	@Test
	void generateLastTags() {
		List<String> neededLastTags = List.of(
				"TABINSPACES" + "<br>",
				"TABINSPACES" + "<a href=\"#top\"><i>Back to top of page</i></a><br>"
/*
				"TABINSPACES" + "<script>",
				"TABINSPACES" + "TABINSPACES" + "function content_to_clipboard(element) {",
				"TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "element.select();",
				"TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "document.execCommand('copy');",
				"TABINSPACES" + "TABINSPACES" + "}",
				"TABINSPACES" + "TABINSPACES" + "function element_to_full_size(element) {",
				"TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "element.style.height = \"\";",
				"TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "element.style.height = element.scrollHeight + \"px\";",
				"TABINSPACES" + "TABINSPACES" + "}",
				"TABINSPACES" + "</script>",
				"</body>",
				"</html>"
*/
		);
		Assertions.assertEquals(neededLastTags, htmlGenerators.generateLastTags(formulas));
	}

	@Test
	void changeToHtmlCharsInLine() {
		Assertions.assertEquals(
				"&lt;tag>" + formulas.getTabInHtml() + "text" +
						formulas.getTabInHtml() + "text>tag2&lt;",
				htmlGenerators.changeToHtmlCharsInLine("<tag>" + formulas.getTabInSpaces() +
						"text\ttext>tag2<", formulas));
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
		Assertions.assertEquals(needed, htmlGenerators.collectAndReferenceHeaders(html, formulas));
	}

}