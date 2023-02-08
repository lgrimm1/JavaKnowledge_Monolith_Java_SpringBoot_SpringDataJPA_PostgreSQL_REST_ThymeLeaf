package lgrimm1.JavaKnowledge.Process;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.when;

//TODO review ProcessPage tests

class ProcessPageTest {

	Extractors extractors;
	ProcessPage processPage;

	String tabInSpaces = "    ";
	String tabInHtml = "&nbsp;&nbsp;&nbsp;&nbsp;";
	String languageVersions = "Version text";
	String rootHtmlName = "root_html";
	String fileName = "file_name";
	String title = "Title Word";

	List<String> pageText;
	List<String> expectedPageHtml;
	List<String> actualPageHtml;
	List<Title> actualTitles;
	List<Title> expectedTitles;

	@BeforeEach
	void setUp() {
/*
		extractors = Mockito.mock(Extractors.class);
		processPage = new ProcessPage();
*/
	}

	@Test
	void title() {
/*
		pageText = List.of(
				"=".repeat(81),
				title,
				"=".repeat(81)
		);

		expectedTitles = List.of(
				new Title(title, title, fileName, 1)
		);
		expectedPageHtml = processPage.generateFirstTags(title, tabInSpaces);
		expectedPageHtml.addAll(List.of(
				tabInSpaces + languageVersions + "<br>",
				tabInSpaces + "<h1>" + title + "</h1>"
		));
		expectedPageHtml.addAll(processPage.generateLastTags(rootHtmlName, tabInSpaces));

		actualTitles = new ArrayList<>();
		actualPageHtml = processPage.processTxt(
				pageText,
				actualTitles,
				fileName,
				tabInSpaces,
				tabInHtml,
				"=".repeat(81),
				"-".repeat(81),
				rootHtmlName,
				";",
				languageVersions);
		Assertions.assertEquals(expectedPageHtml, actualPageHtml);
		Assertions.assertEquals(expectedTitles, actualTitles);
*/
	}

	@Test
	void firstHeader() {
/*
		String header1 = "1. Header 1 text";
		pageText = List.of(
				"=".repeat(81),
				title,
				"=".repeat(81),
				"",
				"=".repeat(81),
				header1,
				"=".repeat(81)
		);

		expectedTitles = List.of(
				new Title(title, title, fileName, 1),
				new Title(title + ";0" + header1, header1, fileName + "#" + header1, 2)
		);
		expectedPageHtml = processPage.generateFirstTags(title, tabInSpaces);
		expectedPageHtml.addAll(List.of(
				tabInSpaces + languageVersions + "<br>",
				tabInSpaces + "<h1>" + title + "</h1>",
				tabInSpaces + "<br>",
				tabInSpaces + "<a href=\"#top\">Back to top of page</a><br>",
				tabInSpaces + "<a href=\""+ rootHtmlName +"\">Back to root page</a><br>",
				tabInSpaces + "<a name=\"" + header1 + "\"></a>",
				tabInSpaces + "<h2>" + header1 + "</h2>"
		));
		expectedPageHtml.addAll(processPage.generateLastTags(rootHtmlName, tabInSpaces));

		actualTitles = new ArrayList<>();
		actualPageHtml = processPage.processTxt(
				pageText,
				actualTitles,
				fileName,
				tabInSpaces,
				tabInHtml,
				"=".repeat(81),
				"-".repeat(81),
				rootHtmlName,
				";",
				languageVersions);
		Assertions.assertEquals(expectedPageHtml, actualPageHtml);
		Assertions.assertEquals(expectedTitles, actualTitles);
*/
	}

	@Test
	void secondHeader() {
/*
		String header1 = "1. Header 1 text";
		String header2 = "1.1. Header 2 text";
		pageText = List.of(
				"=".repeat(81),
				title,
				"=".repeat(81),
				"",
				"=".repeat(81),
				header1,
				"=".repeat(81),
				"",
				header2,
				"-".repeat(81)
		);

		expectedTitles = List.of(
				new Title(title, title, fileName, 1),
				new Title(title + ";0" + header1, header1, fileName + "#" + header1, 2),
				new Title(title + ";0" + header1 + ";0" + header2, header2, fileName + "#" + header2, 3)
		);
		expectedPageHtml = processPage.generateFirstTags(title, tabInSpaces);
		expectedPageHtml.addAll(List.of(
				tabInSpaces + languageVersions + "<br>",
				tabInSpaces + "<h1>" + title + "</h1>",
				tabInSpaces + "<br>",
				tabInSpaces + "<a href=\"#top\">Back to top of page</a><br>",
				tabInSpaces + "<a href=\""+ rootHtmlName +"\">Back to root page</a><br>",
				tabInSpaces + "<a name=\"" + header1 + "\"></a>",
				tabInSpaces + "<h2>" + header1 + "</h2>",
				tabInSpaces + "<br>",
				tabInSpaces + "<a href=\"#top\">Back to top of page</a><br>",
				tabInSpaces + "<a href=\""+ rootHtmlName +"\">Back to root page</a><br>",
				tabInSpaces + "<a name=\"" + header2 + "\"></a>",
				tabInSpaces + "<h3>" + header2 + "</h3>"
		));
		expectedPageHtml.addAll(processPage.generateLastTags(rootHtmlName, tabInSpaces));

		actualTitles = new ArrayList<>();
		actualPageHtml = processPage.processTxt(
				pageText,
				actualTitles,
				fileName,
				tabInSpaces,
				tabInHtml,
				"=".repeat(81),
				"-".repeat(81),
				rootHtmlName,
				";",
				languageVersions);
		Assertions.assertEquals(expectedPageHtml, actualPageHtml);
		Assertions.assertEquals(expectedTitles, actualTitles);
*/
	}

	@Test
	void example() {
/*
		pageText = List.of(
				"=".repeat(81),
				title,
				"=".repeat(81),
				"EXAMPLE FOR SOMETHING:",
				"first row",
				"    second row",
				"END OF EXAMPLE"
		);

		expectedTitles = List.of(
				new Title(title, title, fileName, 1)
		);
		expectedPageHtml = processPage.generateFirstTags(title, tabInSpaces);
		expectedPageHtml.addAll(List.of(
				tabInSpaces + languageVersions + "<br>",
				tabInSpaces + "<h1>" + title + "</h1>",
				tabInSpaces + "<h4>EXAMPLE FOR SOMETHING:</h4>",
				tabInSpaces + "<textarea>",
				tabInSpaces + tabInSpaces + "first row<br>",
				tabInSpaces + tabInSpaces + "&nbsp;&nbsp;&nbsp;&nbsp;second row<br>",
				tabInSpaces + "</textarea>"
		));
		expectedPageHtml.addAll(processPage.generateLastTags(rootHtmlName, tabInSpaces));

		actualTitles = new ArrayList<>();
		actualPageHtml = processPage.processTxt(
				pageText,
				actualTitles,
				fileName,
				tabInSpaces,
				tabInHtml,
				"=".repeat(81),
				"-".repeat(81),
				rootHtmlName,
				";",
				languageVersions);
		Assertions.assertEquals(expectedPageHtml, actualPageHtml);
		Assertions.assertEquals(expectedTitles, actualTitles);
*/
	}

	@Test
	void reference() {
/*
		pageText = List.of(
				"=".repeat(81),
				title,
				"=".repeat(81),
				"=>File_Name_1",
				"=>File_Name_2;1.1. HEADER"
		);

		when(extractors.extractReference("=>File_Name_1", tabInSpaces))
				.thenReturn(tabInSpaces + "<a href=\"File_Name_1.html\">See: File Name 1</a></br>");
		when(extractors.extractReference("=>File_Name_2;1.1. HEADER", tabInSpaces))
				.thenReturn(tabInSpaces + "<a href=\"File_Name_2.html#1.1. HEADER\">See: File Name 2 / 1.1. HEADER</a></br>");

		expectedTitles = List.of(
				new Title(title, title, fileName, 1)
		);
		expectedPageHtml = processPage.generateFirstTags(title, tabInSpaces);
		expectedPageHtml.addAll(List.of(
				tabInSpaces + languageVersions + "<br>",
				tabInSpaces + "<h1>" + title + "</h1>",
				tabInSpaces + "<a href=\"File_Name_1.html\">See: File Name 1</a></br>",
				tabInSpaces + "<a href=\"File_Name_2.html#1.1. HEADER\">See: File Name 2 / 1.1. HEADER</a></br>"
		));
		expectedPageHtml.addAll(processPage.generateLastTags(rootHtmlName, tabInSpaces));

		actualTitles = new ArrayList<>();
		actualPageHtml = processPage.processTxt(
				pageText,
				actualTitles,
				fileName,
				tabInSpaces,
				tabInHtml,
				"=".repeat(81),
				"-".repeat(81),
				rootHtmlName,
				";",
				languageVersions);
		Assertions.assertEquals(expectedPageHtml, actualPageHtml);
		Assertions.assertEquals(expectedTitles, actualTitles);
*/
	}
	
	@Test
	void table() {
/*
		pageText = List.of(
				"=".repeat(81),
				title,
				"=".repeat(81),
				"||Header 1|Header 2|Header 3||",
				"||Cell 11|Cell 12|Cell 13||",
				"||Cell 21|Cell 22|Cell 23||"
		);

		when(extractors.extractTable(List.of(
				"||Header 1|Header 2|Header 3||",
				"||Cell 11|Cell 12|Cell 13||",
				"||Cell 21|Cell 22|Cell 23||"
		), tabInSpaces))
				.thenReturn(List.of(
						tabInSpaces + "<table>",
						tabInSpaces + tabInSpaces + "<tr>",
						tabInSpaces + tabInSpaces + tabInSpaces + "<th>Header 1</th>",
						tabInSpaces + tabInSpaces + tabInSpaces + "<th>Header 2</th>",
						tabInSpaces + tabInSpaces + tabInSpaces + "<th>Header 3</th>",
						tabInSpaces + tabInSpaces + "</tr>",
						tabInSpaces + tabInSpaces + "<tr>",
						tabInSpaces + tabInSpaces + tabInSpaces + "<td>Cell 11</td>",
						tabInSpaces + tabInSpaces + tabInSpaces + "<td>Cell 12</td>",
						tabInSpaces + tabInSpaces + tabInSpaces + "<td>Cell 13</td>",
						tabInSpaces + tabInSpaces + "</tr>",
						tabInSpaces + tabInSpaces + "<tr>",
						tabInSpaces + tabInSpaces + tabInSpaces + "<td>Cell 21</td>",
						tabInSpaces + tabInSpaces + tabInSpaces + "<td>Cell 22</td>",
						tabInSpaces + tabInSpaces + tabInSpaces + "<td>Cell 23</td>",
						tabInSpaces + tabInSpaces + "</tr>",
						tabInSpaces + "</table>"
				));

		expectedTitles = List.of(
				new Title(title, title, fileName, 1)
		);
		expectedPageHtml = processPage.generateFirstTags(title, tabInSpaces);
		expectedPageHtml.addAll(List.of(
				tabInSpaces + languageVersions + "<br>",
				tabInSpaces + "<h1>" + title + "</h1>",
				tabInSpaces + "<table>",
				tabInSpaces + tabInSpaces + "<tr>",
				tabInSpaces + tabInSpaces + tabInSpaces + "<th>Header 1</th>",
				tabInSpaces + tabInSpaces + tabInSpaces + "<th>Header 2</th>",
				tabInSpaces + tabInSpaces + tabInSpaces + "<th>Header 3</th>",
				tabInSpaces + tabInSpaces + "</tr>",
				tabInSpaces + tabInSpaces + "<tr>",
				tabInSpaces + tabInSpaces + tabInSpaces + "<td>Cell 11</td>",
				tabInSpaces + tabInSpaces + tabInSpaces + "<td>Cell 12</td>",
				tabInSpaces + tabInSpaces + tabInSpaces + "<td>Cell 13</td>",
				tabInSpaces + tabInSpaces + "</tr>",
				tabInSpaces + tabInSpaces + "<tr>",
				tabInSpaces + tabInSpaces + tabInSpaces + "<td>Cell 21</td>",
				tabInSpaces + tabInSpaces + tabInSpaces + "<td>Cell 22</td>",
				tabInSpaces + tabInSpaces + tabInSpaces + "<td>Cell 23</td>",
				tabInSpaces + tabInSpaces + "</tr>",
				tabInSpaces + "</table>"
		));
		expectedPageHtml.addAll(processPage.generateLastTags(rootHtmlName, tabInSpaces));

		actualTitles = new ArrayList<>();
		actualPageHtml = processPage.processTxt(
				pageText,
				actualTitles,
				fileName,
				tabInSpaces,
				tabInHtml,
				"=".repeat(81),
				"-".repeat(81),
				rootHtmlName,
				";",
				languageVersions);
		Assertions.assertEquals(expectedPageHtml, actualPageHtml);
		Assertions.assertEquals(expectedTitles, actualTitles);
*/
	}

	@Test
	void normalText() {
/*
		pageText = List.of(
				"=".repeat(81),
				title,
				"=".repeat(81),
				"Normal text 1.",
				"Normal text 2. Normal text 2.",
				"",
				"Normal text 3:",
				"\ta. bullet 1",
				"    b. bullet 2",
				"Remarks on bullets.",
				""
		);

		expectedTitles = List.of(
				new Title(title, title, fileName, 1)
		);
		expectedPageHtml = processPage.generateFirstTags(title, tabInSpaces);
		expectedPageHtml.addAll(List.of(
				tabInSpaces + languageVersions + "<br>",
				tabInSpaces + "<h1>" + title + "</h1>",
				tabInSpaces + "Normal text 1.<br>",
				tabInSpaces + "Normal text 2. Normal text 2.<br>",
				tabInSpaces + "<br>",
				tabInSpaces + "Normal text 3:<br>",
				tabInSpaces + "&nbsp;&nbsp;&nbsp;&nbsp;a. bullet 1<br>",
				tabInSpaces + "&nbsp;&nbsp;&nbsp;&nbsp;b. bullet 2<br>",
				tabInSpaces + "Remarks on bullets.<br>",
				tabInSpaces + "<br>"
		));
		expectedPageHtml.addAll(processPage.generateLastTags(rootHtmlName, tabInSpaces));

		actualTitles = new ArrayList<>();
		actualPageHtml = processPage.processTxt(
				pageText,
				actualTitles,
				fileName,
				tabInSpaces,
				tabInHtml,
				"=".repeat(81),
				"-".repeat(81),
				rootHtmlName,
				";",
				languageVersions);
		Assertions.assertEquals(expectedPageHtml, actualPageHtml);
		Assertions.assertEquals(expectedTitles, actualTitles);
*/
	}

	@Test
	void generateFirstTags() {
		String title = "TITLE TITLE";
		String tabInSpaces = "    ";
		List<String> neededFirstTags = List.of(
				"<!DOCTYPE html>",
				"<html lang=\"en\">",
				"<head>",
				tabInSpaces + "<meta charset=\"UTF-8\">",
				tabInSpaces + "<title>" + title + "</title>",
				tabInSpaces + "<style>",
				tabInSpaces + "table, th, td {",
				tabInSpaces + tabInSpaces + "border: 1px solid black;",
				tabInSpaces + tabInSpaces + "border-collapse: collapse;",
				tabInSpaces + "}",
				tabInSpaces + "h1, h2, h3 {color:red;}",
				tabInSpaces + "h4 {color:blue;}",
				tabInSpaces + "</style>",
				"</head>",
				"<body>",
				tabInSpaces + "<a name=\"top\"></a>"
		);
		Assertions.assertIterableEquals(neededFirstTags, processPage.generateFirstTags(title, tabInSpaces));
	}

	@Test
	void generateLastTags() {
		String rootHtmlName = "Title_word";
		String tabInSpaces = "    ";
		List<String> neededLastTags = List.of(
				tabInSpaces + "<br>",
				tabInSpaces + "<a href=\"#top\">Back to top of page</a><br>",
				tabInSpaces + "<a href=\""+ rootHtmlName +"\">Back to root page</a><br>",
				"</body>",
				"</html>"
		);
		Assertions.assertIterableEquals(neededLastTags, processPage.generateLastTags(rootHtmlName, tabInSpaces));
	}

	@Test
	void changeToHtmlCharsInLine() {
		String tabInSpaces = "    ";
		String tabInHtml = "&nbsp;&nbsp;&nbsp;&nbsp;";
		Assertions.assertEquals(
				"&lt;tag>" + tabInHtml + "text" + tabInHtml + "text>tag2&lt;",
				processPage.changeToHtmlCharsInLine("<tag>    text\ttext>tag2<", tabInSpaces, tabInHtml));
	}

	@Test
	void collectAndReferencingHeaders() {
		List<String> html = List.of(
				tabInSpaces + "Text 1<br>",
				tabInSpaces + "Text 2<br>",
				tabInSpaces + "Header 1<br>",
				tabInSpaces + "Header 2<br>",
				tabInSpaces + "<br>",
				tabInSpaces + "<h2>Header 1</h2>",
				tabInSpaces + "Text 3<br>",
				tabInSpaces + "<br>",
				tabInSpaces + "<h3>Header 2</h3>",
				tabInSpaces + "Text 4<br>"
		);
		List<String> needed = List.of(
				tabInSpaces + "Text 1<br>",
				tabInSpaces + "Text 2<br>",
				tabInSpaces + "<a href=\"#Header 1\">" + tabInSpaces + "Header 1</a><br>",
				tabInSpaces + "<a href=\"#Header 2\">" + tabInSpaces + "Header 2</a><br>",
				tabInSpaces + "<br>",
				tabInSpaces + "<h2>Header 1</h2>",
				tabInSpaces + "Text 3<br>",
				tabInSpaces + "<br>",
				tabInSpaces + "<h3>Header 2</h3>",
				tabInSpaces + "Text 4<br>"
		);
		Assertions.assertIterableEquals(needed, processPage.collectAndReferenceHeaders(html, tabInSpaces));
	}
}