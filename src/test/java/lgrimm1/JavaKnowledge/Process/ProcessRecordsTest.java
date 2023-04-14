package lgrimm1.JavaKnowledge.Process;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.*;
import java.util.*;

import static org.mockito.Mockito.*;

class ProcessRecordsTest {

	ProcessRecords processRecords = new ProcessRecords();
	TitleRepository titleRepository;
	TxtRepository txtRepository;
	HtmlRepository htmlRepository;
	FileOperations fileOperations;
	Formulas formulas;
	Extractors extractors;
	ProcessPage processPage;

	@BeforeEach
	void setUp() {
		titleRepository = Mockito.mock(TitleRepository.class);
		txtRepository = Mockito.mock(TxtRepository.class);
		htmlRepository = Mockito.mock(HtmlRepository.class);
		fileOperations = Mockito.mock(FileOperations.class);
		formulas = Mockito.mock(Formulas.class);
		extractors = Mockito.mock(Extractors.class);
		processPage = Mockito.mock(ProcessPage.class);
	}

	@Test
	void searchBySearchText() {
		Assertions.assertTrue(processRecords.searchBySearchText(null, titleRepository, txtRepository).isEmpty());
		Assertions.assertTrue(processRecords.searchBySearchText("  ", titleRepository, txtRepository).isEmpty());

		when(titleRepository.findByTitleContainingAllIgnoreCase("Word1"))
				.thenReturn(List.of(
						new TitleEntity(
								2L,
								"Title with word1",
								"title_with_word1",
								3L,
								4L
						),
						new TitleEntity(
								3L,
								"Title with word1 word2 word3",
								"title_with_word1_word2_word3",
								6L,
								7L
						)
				));
		when(titleRepository.findByTitleContainingAllIgnoreCase("WORD2"))
				.thenReturn(List.of(
						new TitleEntity(
								4L,
								"Title with word2",
								"title_with_word2",
								10L,
								11L
						),
						new TitleEntity(
								3L,
								"Title with word1 word2 word3",
								"title_with_word1_word2_word3",
								6L,
								7L
						)
				));

		when(txtRepository.findByContentContainingAllIgnoreCase("Word1"))
				.thenReturn(List.of(
						new TxtEntity(20L, "SOME text WITH WorD1 in IT."),
						new TxtEntity(6L, "other TEXT with word1 in it"),
						new TxtEntity(30L, "some other TEXT with word1 in it")
				));
		when(txtRepository.findByContentContainingAllIgnoreCase("WORD2"))
				.thenReturn(new ArrayList<>());

		when(titleRepository.findByTxtId(20L))
				.thenReturn(Optional.empty());
		when(titleRepository.findByTxtId(6L))
				.thenReturn(Optional.of(new TitleEntity(
						3L,
						"Title with word1 word2 word3",
						"title_with_word1_word2_word3",
						6L,
						7L
				)));
		when(titleRepository.findByTxtId(30L))
				.thenReturn(Optional.of(new TitleEntity(
						40L,
						"Another Title with another words",
						"another_title_with_another_words",
						30L,
						7L
				)));

		Assertions.assertEquals(Set.of(
				"Title with word1",
				"Title with word1 word2 word3",
				"Another Title with another words"
		), processRecords.searchBySearchText("Word1", titleRepository, txtRepository));

		Assertions.assertEquals(Set.of(
				"Title with word2",
				"Title with word1 word2 word3"
		), processRecords.searchBySearchText("WORD2", titleRepository, txtRepository));
	}

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
