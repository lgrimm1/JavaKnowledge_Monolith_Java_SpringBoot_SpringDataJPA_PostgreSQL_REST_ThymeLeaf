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

/*
	@Test
	void deleteByTitles() {
		when(titleRepository.findByTitle("title 1"))
				.thenReturn(Optional.of(new TitleEntity(
						1L,
						"title 1",
						"title_1",
						11L,
						21L
				)));
		when(titleRepository.findByTitle("title 2"))
				.thenReturn(Optional.of(new TitleEntity(
						2L,
						"title 2",
						"title_2",
						12L,
						22L
				)));
		when(titleRepository.findByTitle("title 3"))
				.thenReturn(Optional.empty());
		when(titleRepository.count())
				.thenReturn(2L);

		Assertions.assertEquals(0, processRecords.deleteByTitles(
				null,
				titleRepository,
				txtRepository,
				htmlRepository
		));
		Assertions.assertEquals(0, processRecords.deleteByTitles(
				new ArrayList<>(),
				titleRepository,
				txtRepository,
				htmlRepository
		));

		Assertions.assertEquals(2, processRecords.deleteByTitles(List.of(
				"title 1",
				"title 2",
				"title 3"
		), titleRepository, txtRepository, htmlRepository));
	}

*/
/*
	@Test
	void getAllTitles() {
		when(titleRepository.findAll())
				.thenReturn(List.of(
						new TitleEntity(2L, "title 2", "title_2", 2L, 2L),
						new TitleEntity(1L, "title 1", "title_1", 1L, 1L),
						new TitleEntity(3L, "title 3", "title_3", 3L, 3L)
				));
		Assertions.assertEquals(List.of(
				"title 1",
				"title 2",
				"title 3"
		), processRecords.getAllTitles(titleRepository));
	}

*/
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
		Assertions.assertEquals("abc\n", processRecords.listToString(List.of("abc")));
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
