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
				.thenReturn("Version text");
		when(formulas.getConstant(Formulas.ConstantName.ROOT_HTML_NAME))
				.thenReturn("root_html");
		titleRepository = Mockito.mock(TitleRepository.class);
	}

	@Test
	void generateMainContentHeader1() {
		String header1 = "1. Header 1 text";
		List<String> txtContent = List.of(
				formulas.getConstant(Formulas.ConstantName.SUPERLINE),
				header1,
				formulas.getConstant(Formulas.ConstantName.SUPERLINE)
		);

		List<String> expectedHtml = List.of(
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<a href=\"#top\">Back to top of page</a><br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<a href=\""+ formulas.getConstant(Formulas.ConstantName.ROOT_HTML_NAME) +"\">Back to root page</a><br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<a name=\"" + header1 + "\"></a>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<h2>" + header1 + "</h2>"
		);

		List<String> actualHtml = htmlGenerators.generateMainContent(
				txtContent,
				formulas,
				extractors,
				titleRepository);
		Assertions.assertEquals(expectedHtml, actualHtml);
	}

	@Test
	void generateMainContentHeader2() {
		String header2 = "1.2. Header 2 text";
		List<String> txtContent = List.of(
				header2,
				formulas.getConstant(Formulas.ConstantName.SUBLINE)
		);

		List<String> expectedHtml = List.of(
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<a href=\"#top\">Back to top of page</a><br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<a href=\""+ formulas.getConstant(Formulas.ConstantName.ROOT_HTML_NAME) +"\">Back to root page</a><br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<a name=\"" + header2 + "\"></a>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<h3>" + header2 + "</h3>"
		);

		List<String> actualHtml = htmlGenerators.generateMainContent(
				txtContent,
				formulas,
				extractors,
				titleRepository);
		Assertions.assertEquals(expectedHtml, actualHtml);
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

		List<String> actualHtml = htmlGenerators.generateMainContent(
				txtContent,
				formulas,
				extractors,
				titleRepository);
		Assertions.assertEquals(expectedHtml, actualHtml);
	}

	@Test
	void generateMainContentExample() {
		List<String> txtContent = List.of(
				"EXAMPLE FOR SOMETHING:",
				"first row",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "second row",
				"END OF EXAMPLE"
		);

		List<String> expectedHtml = List.of(
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<h4>EXAMPLE FOR SOMETHING:</h4>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<textarea>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "first row<br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "second row<br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "</textarea>"
		);

		List<String> actualHtml = htmlGenerators.generateMainContent(
				txtContent,
				formulas,
				extractors,
				titleRepository);
		Assertions.assertEquals(expectedHtml, actualHtml);
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

		List<String> actualHtml = htmlGenerators.generateMainContent(
				txtContent,
				formulas,
				extractors,
				titleRepository);
		Assertions.assertEquals(expectedHtml, actualHtml);
	}

	@Test
	void generateMainContentNormalText() {
		List<String> txtContent = List.of(
				"Line 1",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Line 2"
		);

		List<String> expectedHtml = List.of(
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Line 1</br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Line 2</br>"
		);

		List<String> actualHtml = htmlGenerators.generateMainContent(
				txtContent,
				formulas,
				extractors,
				titleRepository);
		Assertions.assertEquals(expectedHtml, actualHtml);
	}

	@Test
	void generateFirstTags() {
		String title = "TITLE TITLE";
		List<String> neededFirstTags = List.of(
				"<!DOCTYPE html>",
				"<html lang=\"en\">",
				"<head>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<meta charset=\"UTF-8\">",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<title>" + title + "</title>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<style>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "table, th, td {",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"border: 1px solid black;",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"border-collapse: collapse;",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "}",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "h1, h2, h3 {color:red;}",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "h4 {color:blue;}",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "</style>",
				"</head>",
				"<body>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<a name=\"top\"></a>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						formulas.getConstant(Formulas.ConstantName.VERSIONS) + "<br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<h1>" + title + "</h1>"
		);
		Assertions.assertEquals(neededFirstTags, htmlGenerators.generateFirstTags(title, formulas));
	}

	@Test
	void generateLastTags() {
		List<String> neededLastTags = List.of(
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"<a href=\"#top\">Back to top of page</a><br>",
				formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) +
						"<a href=\""+ formulas.getConstant(Formulas.ConstantName.ROOT_HTML_NAME) +
						"\">Back to root page</a><br>",
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