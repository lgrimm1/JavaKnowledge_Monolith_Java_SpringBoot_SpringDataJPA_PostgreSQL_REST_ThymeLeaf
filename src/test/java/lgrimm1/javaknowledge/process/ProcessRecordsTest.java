package lgrimm1.javaknowledge.process;

import lgrimm1.javaknowledge.databasestorage.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

class ProcessRecordsTest {

	ProcessRecords processRecords;
	FileOperations fileOperations;
	Extractors extractors;
	ProcessPage processPage;
	DatabaseStorageService databaseStorageService;

	@BeforeEach
	void setUp() {
		fileOperations = Mockito.mock(FileOperations.class);
		extractors = Mockito.mock(Extractors.class);
		processPage = Mockito.mock(ProcessPage.class);
		processRecords = new ProcessRecords(databaseStorageService, fileOperations, extractors, processPage);
	}

@Test
	void stringToList_NullString() {
		Assertions.assertTrue(processRecords.stringToList(null).isEmpty());
	}

	@Test
	void stringToList_BlankString() {
		Assertions.assertTrue(processRecords.stringToList("  ").isEmpty());
	}

	@Test
	void stringToList_OneLineWithoutPageBreak() {
		String string = "abc";
		List<String> expectedList = List.of("abc");

		List<String> actualList = processRecords.stringToList(string);
		Assertions.assertEquals(expectedList, actualList);
	}

	@Test
	void stringToList_OneLineWithPageBreak() {
		String string = "abc\n";
		List<String> expectedList = List.of("abc");

		List<String> actualList = processRecords.stringToList(string);
		Assertions.assertEquals(expectedList, actualList);
	}

	@Test
	void stringToList_MoreLinesWithoutPageBreakAtLast() {
		String string = "abc\nxyz";
		List<String> expectedList = List.of("abc", "xyz");

		List<String> actualList = processRecords.stringToList(string);
		Assertions.assertEquals(expectedList, actualList);
	}

	@Test
	void stringToList_MoreLinesWithPageBreakAtLast() {
		String string = "abc\nxyz\n";
		List<String> expectedList = List.of("abc", "xyz");

		List<String> actualList = processRecords.stringToList(string);
		Assertions.assertEquals(expectedList, actualList);
	}

	@Test
	void listToString_NullList() {
		Assertions.assertTrue(processRecords.listToString(null).isEmpty());
	}

	@Test
	void listToString_EmptyList() {
		Assertions.assertTrue(processRecords.listToString(new ArrayList<>()).isEmpty());
	}

	@Test
	void listToString_OneElement() {
		Assertions.assertEquals("abc\n",
				processRecords.listToString(List.of("abc")));
	}

	@Test
	void listToString_MoreElements() {
		Assertions.assertEquals("abc\nxyz\n", processRecords.listToString(List.of("abc", "xyz")));
	}

	@Test
	void listToString_MoreElements_OneNullElement() {
		List<String> list = new ArrayList<>();
		list.add("abc");
		list.add(null);
		list.add("xyz");
		Assertions.assertEquals("abc\n\nxyz\n", processRecords.listToString(list));
	}

	@Test
	void listToString_MoreElements_OneBlankElement() {
		List<String> list = new ArrayList<>();
		list.add("abc");
		list.add("  ");
		list.add("xyz");
		Assertions.assertEquals("abc\n\nxyz\n", processRecords.listToString(list));
	}
}
