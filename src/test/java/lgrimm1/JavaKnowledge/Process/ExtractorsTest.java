package lgrimm1.JavaKnowledge.Process;

import org.junit.jupiter.api.*;

import java.util.*;

class ExtractorsTest {

	Extractors extractors = new Extractors();
	String tabInSpaces = "    ";

	@Test
	void extractReference_SimpleFileName() {
		String line = "=>HtmlFileName";
		Assertions.assertEquals(
				tabInSpaces + "<a href=\"HtmlFileName.html\">See: HtmlFileName</a></br>",
				extractors.extractReference(line, tabInSpaces)
		);
	}

	@Test
	void extractReference_FileNameMoreWords() {
		String line = "=>Html_File_Name";
		Assertions.assertEquals(
				tabInSpaces + "<a href=\"Html_File_Name.html\">See: Html File Name</a></br>",
				extractors.extractReference(line, tabInSpaces)
		);
	}

	@Test
	void extractReference_SimpleFileNameWithHeader() {
		String line = "=>HtmlFileName;3.1. Header Title";
		Assertions.assertEquals(
				tabInSpaces + "<a href=\"HtmlFileName.html#3.1. Header Title\">See: HtmlFileName / 3.1. Header Title</a></br>",
				extractors.extractReference(line, tabInSpaces)
		);
	}

	@Test
	void extractReference_FileNameMoreWordsWithHeader() {
		String line = "=>Html_File_Name;3.1. Header Title";
		Assertions.assertEquals(
				tabInSpaces + "<a href=\"Html_File_Name.html#3.1. Header Title\">See: Html File Name / 3.1. Header Title</a></br>",
				extractors.extractReference(line, tabInSpaces)
		);
	}

	@Test
	void extractTable() {
		List<String> tableInText = List.of(
				"||Header 1|Header 2|Header 3||",
				"||Cell 11|Cell 12|Cell 13||",
				"||Cell 21|Cell 22|Cell 23||"
		);
		List<String> needed = List.of(
				"  <table>",
				"    <tr>",
				"      <th>Header 1</th>",
				"      <th>Header 2</th>",
				"      <th>Header 3</th>",
				"    </tr>",
				"    <tr>",
				"      <td>Cell 11</td>",
				"      <td>Cell 12</td>",
				"      <td>Cell 13</td>",
				"    </tr>",
				"    <tr>",
				"      <td>Cell 21</td>",
				"      <td>Cell 22</td>",
				"      <td>Cell 23</td>",
				"    </tr>",
				"  </table>"
		);
		Assertions.assertIterableEquals(needed, extractors.extractTable(tableInText, "  "));
	}

}