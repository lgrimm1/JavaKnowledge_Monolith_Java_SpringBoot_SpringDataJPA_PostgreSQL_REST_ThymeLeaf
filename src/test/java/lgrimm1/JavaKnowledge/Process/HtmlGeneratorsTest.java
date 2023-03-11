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
		when(formulas.getConstant(Formulas.ConstantName.SUPERLINE))
				.thenReturn("SUPERLINE");
		when(formulas.getConstant(Formulas.ConstantName.SUBLINE))
				.thenReturn("SUBLINE");
		when(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES))
				.thenReturn("TABINSPACES");
		when(formulas.getConstant(Formulas.ConstantName.TAB_IN_HTML))
				.thenReturn("TABINHTML");
		when(formulas.getConstant(Formulas.ConstantName.VERSIONS))
				.thenReturn("VERSIONS");
		titleRepository = Mockito.mock(TitleRepository.class);
	}

	@Test
	void generateMainContentHeader1() {
		String header1 = "1. Header 1 text";
		List<String> txtContent = List.of(
				"SUPERLINE",
				header1,
				"SUPERLINE"
		);

		List<String> expectedHtml = List.of(
				"TABINSPACES" + "<a href=\"#top\">Back to top of page</a><br>",
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
	void generateMainContentHeader2() {
		String header2 = "1.2. Header 2 text";
		List<String> txtContent = List.of(
				header2,
				"SUBLINE"
		);

		List<String> expectedHtml = List.of(
				"TABINSPACES" + "<a href=\"#top\">Back to top of page</a><br>",
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
	void generateMainContentTable() {
		List<String> txtContent = List.of(
				"||Header 1|Header 2|Header 3||",
				"||Cell 11|Cell 12|Cell 13||",
				"||Cell 21|Cell 22|Cell 23||"
		);

		when(extractors.extractTable(List.of(
				"||Header 1|Header 2|Header 3||",
				"||Cell 11|Cell 12|Cell 13||",
				"||Cell 21|Cell 22|Cell 23||"
		), formulas))
				.thenReturn(List.of(
						"HTML table line 1",
						"HTML table line 2"
				));

		List<String> expectedHtml = List.of(
				"HTML table line 1",
				"HTML table line 2"
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
	void generateMainContentExampleWithoutClosingText() {
		List<String> txtContent = List.of(
				"EXAMPLE FOR SOMETHING:",
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
	void generateMainContentExampleWithClosingText() {
		List<String> txtContent = List.of(
				"EXAMPLE FOR SOMETHING:",
				"first row",
				"  second row",
				"END OF EXAMPLE"
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
	void generateMainContentReference() {
		List<String> txtContent = List.of(
				"=>Title Word 1",
				"=>Title Word 2;1.2. Header header"
		);

		when(extractors.extractReference("=>Title Word 1", formulas, titleRepository))
				.thenReturn("Existing reference");
		when(extractors.extractReference("=>Title Word 2;1.2. Header header", formulas, titleRepository))
				.thenReturn("Existing reference");

		List<String> expectedHtml = List.of(
				"Existing reference",
				"Existing reference"
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
	void generateMainContentNormalText() {
		List<String> txtContent = List.of(
				"Line 1",
				"TABINSPACES" + "Line 2"
		);

		List<String> expectedHtml = List.of(
				"TABINSPACES" + "Line 1</br>",
				"TABINSPACES" + "TABINSPACES" + "Line 2</br>"
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
				"<!DOCTYPE html>",
				"<html lang=\"en\">",
				"<head>",
				"TABINSPACES" + "<title>" + title + "</title>",
				"TABINSPACES" + "<meta charset=\"UTF-8\">",
				"TABINSPACES" + "<link rel=\"stylesheet\" href=\"styles.css\">",
				"</head>",
				"<body>",
				"TABINSPACES" + "<a name=\"top\"></a>",
				"TABINSPACES" + "VERSIONS" + "<br>",
				"TABINSPACES" + "<h1>" + title + "</h1>"
		);
		Assertions.assertEquals(neededFirstTags, htmlGenerators.generateFirstTags(title, formulas));
	}

	@Test
	void generateLastTags() {
		List<String> neededLastTags = List.of(
				"TABINSPACES" + "<br>",
				"TABINSPACES" + "<a href=\"#top\">Back to top of page</a><br>",
				"TABINSPACES" + "<script>",
				"TABINSPACES" + "TABINSPACES" + "function example_to_clipboard(id) {",
				"TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "document.getElementById(id).select();",
				"TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "document.execCommand('copy');",
				"TABINSPACES" + "TABINSPACES" + "}",
				"TABINSPACES" + "TABINSPACES" + "function element_to_full_size(element) {",
				"TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "element.style.height = \"\";",
				"TABINSPACES" + "TABINSPACES" + "TABINSPACES" + "element.style.height = element.scrollHeight + \"px\";",
				"TABINSPACES" + "TABINSPACES" + "}",
				"TABINSPACES" + "</script>",
				"</body>",
				"</html>"
		);
		Assertions.assertEquals(neededLastTags, htmlGenerators.generateLastTags(formulas));
	}

	@Test
	void changeToHtmlCharsInLine() {
		Assertions.assertEquals(
				"&lt;tag>" + formulas.getConstant(Formulas.ConstantName.TAB_IN_HTML) + "text" +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_HTML) + "text>tag2&lt;",
				htmlGenerators.changeToHtmlCharsInLine("<tag>" +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "text\ttext>tag2<", formulas));
	}

	@Test
	void collectAndReferencingHeaders() {
		List<String> html = List.of(
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Text 1<br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Text 2<br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Header 1<br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Header 2<br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<h2>Header 1</h2>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Text 3<br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<h3>Header 2</h3>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Text 4<br>"
		);
		List<String> needed = List.of(
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Text 1<br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Text 2<br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<a href=\"#Header 1\">" +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Header 1</a><br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<a href=\"#Header 2\">" +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Header 2</a><br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<h2>Header 1</h2>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Text 3<br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<h3>Header 2</h3>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Text 4<br>"
		);
		Assertions.assertEquals(needed, htmlGenerators.collectAndReferenceHeaders(html, formulas));
	}

}