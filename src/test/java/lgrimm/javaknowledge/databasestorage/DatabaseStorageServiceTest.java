package lgrimm.javaknowledge.databasestorage;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;

class DatabaseStorageServiceTest {

	TitleRepository titleRepository;
	TxtRepository txtRepository;
	HtmlRepository htmlRepository;
	DatabaseStorageService databaseStorageService;

	@BeforeEach
	void setUp() {
		titleRepository = Mockito.mock(TitleRepository.class);
		txtRepository = Mockito.mock(TxtRepository.class);
		htmlRepository = Mockito.mock(HtmlRepository.class);
		databaseStorageService = new DatabaseStorageService(titleRepository, txtRepository, htmlRepository);
	}

	@Test
	void findTitleByTitle() {
		String title = "Title 1";
		TitleEntity expectedTitleEntity = new TitleEntity(1L, title, 1L, 1L);
		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.of(expectedTitleEntity));
		Optional<TitleEntity> resultedTitleEntity = databaseStorageService.findTitleByTitle(title);
		Assertions.assertTrue(resultedTitleEntity.isPresent());
		Assertions.assertEquals(expectedTitleEntity, resultedTitleEntity.get());
	}

	@Test
	void findAllTitles() {
		TitleEntity titleEntity1 = new TitleEntity(2L, "title 2", 2L, 2L);
		TitleEntity titleEntity2 = new TitleEntity(1L, "title 1", 1L, 1L);
		TitleEntity titleEntity3 = new TitleEntity(3L, "title 3", 3L, 3L);
		List<TitleEntity> titles = new ArrayList<>();
		titles.add(titleEntity1);
		titles.add(titleEntity2);
		titles.add(titleEntity3);
		when(titleRepository.findAll())
				.thenReturn(titles);
		Assertions.assertEquals(titles, databaseStorageService.findAllTitles());
	}

	@Test
	void getAllTitles() {
		when(titleRepository.findAll())
				.thenReturn(List.of(
						new TitleEntity(2L, "title 2", 2L, 2L),
						new TitleEntity(1L, "title 1", 1L, 1L),
						new TitleEntity(3L, "title 3", 3L, 3L)
				));
		Assertions.assertEquals(List.of(
				"title 1",
				"title 2",
				"title 3"
		), databaseStorageService.getAllTitles());
	}

	@Test
	void saveTitle() {
		TitleEntity givenTitleEntity = new TitleEntity("Title", 2L, 3L);
		TitleEntity savedTitleEntity = new TitleEntity(2L, "Title", 2L, 3L);
		when(databaseStorageService.saveTitle(givenTitleEntity))
				.thenReturn(savedTitleEntity);
		Assertions.assertEquals(savedTitleEntity, databaseStorageService.saveTitle(givenTitleEntity));

		givenTitleEntity = new TitleEntity(4L, "Title", 2L, 3L);
		savedTitleEntity = new TitleEntity(4L, "Title", 2L, 3L);
		when(databaseStorageService.saveTitle(givenTitleEntity))
				.thenReturn(savedTitleEntity);
		Assertions.assertEquals(savedTitleEntity, databaseStorageService.saveTitle(givenTitleEntity));
	}

	@Test
	void findTxtById() {
		long id = 2L;
		TxtEntity expectedTxtEntity = new TxtEntity(id,"content");
		when(txtRepository.findById(id))
				.thenReturn(Optional.of(expectedTxtEntity));
		Optional<TxtEntity> resultedTxtEntity = databaseStorageService.findTxtById(id);
		Assertions.assertTrue(resultedTxtEntity.isPresent());
		Assertions.assertEquals(expectedTxtEntity, resultedTxtEntity.get());
	}

	@Test
	void saveTxt() {
		TxtEntity givenTxtEntity = new TxtEntity("content");
		TxtEntity savedTxtEntity = new TxtEntity(2L, "content");
		when(databaseStorageService.saveTxt(givenTxtEntity))
				.thenReturn(savedTxtEntity);
		Assertions.assertEquals(savedTxtEntity, databaseStorageService.saveTxt(givenTxtEntity));

		givenTxtEntity = new TxtEntity(3L, "content");
		savedTxtEntity = new TxtEntity(3L, "content");
		when(databaseStorageService.saveTxt(givenTxtEntity))
				.thenReturn(savedTxtEntity);
		Assertions.assertEquals(savedTxtEntity, databaseStorageService.saveTxt(givenTxtEntity));
	}

	@Test
	void findHtmlById() {
		long id = 2L;
		HtmlEntity expectedHtmlEntity = new HtmlEntity(2L, "content", "Title reference 1");
		when(htmlRepository.findById(id))
				.thenReturn(Optional.of(expectedHtmlEntity));
		Optional<HtmlEntity> resultedHtmlEntity = databaseStorageService.findHtmlById(id);
		Assertions.assertTrue(resultedHtmlEntity.isPresent());
		Assertions.assertEquals(expectedHtmlEntity, resultedHtmlEntity.get());
	}

	@Test
	void saveHtml() {
		HtmlEntity givenHtmlEntity = new HtmlEntity("html content", "html reference");
		HtmlEntity savedHtmlEntity = new HtmlEntity(2L, "html content", "html reference");
		when(databaseStorageService.saveHtml(givenHtmlEntity))
				.thenReturn(savedHtmlEntity);
		Assertions.assertEquals(savedHtmlEntity, databaseStorageService.saveHtml(givenHtmlEntity));

		givenHtmlEntity = new HtmlEntity(3L, "html content", "html reference");
		savedHtmlEntity = new HtmlEntity(3L, "html content", "html reference");
		when(databaseStorageService.saveHtml(givenHtmlEntity))
				.thenReturn(savedHtmlEntity);
		Assertions.assertEquals(savedHtmlEntity, databaseStorageService.saveHtml(givenHtmlEntity));
	}

	@Test
	void findTitlesBySearchText_NullArgument() {
		Assertions.assertTrue(databaseStorageService.findTitlesBySearchText(null).isEmpty());
	}

	@Test
	void findTitlesBySearchText_BlankArgument() {
		String title1 = "Title 1";
		String title2 = "Title 2";
		when(titleRepository.findAll())
				.thenReturn(List.of(
						new TitleEntity(
								2L,
								title2,
								6L,
								7L
						),
						new TitleEntity(
								1L,
								title1,
								3L,
								4L
						)
				));
		List<String> titles = List.of(title1, title2);
		Assertions.assertEquals(titles, databaseStorageService.findTitlesBySearchText("  "));
	}

	@Test
	void findTitlesBySearchText_GivenArgument() {
		when(titleRepository.findByTitleContainingAllIgnoreCase("Word1"))
				.thenReturn(List.of(
						new TitleEntity(
								3L,
								"Title with word1 word2 word3",
								6L,
								7L
						),
						new TitleEntity(
								2L,
								"Title with word1",
								3L,
								4L
						)
				));
		when(titleRepository.findByTitleContainingAllIgnoreCase("WORD2"))
				.thenReturn(List.of(
						new TitleEntity(
								4L,
								"Title with word2",
								10L,
								11L
						),
						new TitleEntity(
								3L,
								"Title with word1 word2 word3",
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
						6L,
						7L
				)));
		when(titleRepository.findByTxtId(30L))
				.thenReturn(Optional.of(new TitleEntity(
						40L,
						"Another Title with another words",
						30L,
						7L
				)));

		Assertions.assertEquals(List.of(
				"Another Title with another words",
				"Title with word1",
				"Title with word1 word2 word3"
		), databaseStorageService.findTitlesBySearchText("Word1"));

		Assertions.assertEquals(List.of(
				"Title with word1 word2 word3",
				"Title with word2"
		), databaseStorageService.findTitlesBySearchText("WORD2"));
	}

	@Test
	void deleteByTitles() {
		when(titleRepository.findByTitle("title 1"))
				.thenReturn(Optional.of(new TitleEntity(
						1L,
						"title 1",
						11L,
						21L
				)));
		when(titleRepository.findByTitle("title 2"))
				.thenReturn(Optional.of(new TitleEntity(
						2L,
						"title 2",
						12L,
						22L
				)));
		when(titleRepository.findByTitle("title 3"))
				.thenReturn(Optional.empty());
		when(titleRepository.count())
				.thenReturn(2L);

		Assertions.assertEquals(0, databaseStorageService.deleteByTitles(null));
		Assertions.assertEquals(0, databaseStorageService.deleteByTitles(new ArrayList<>()));

		Assertions.assertEquals(2, databaseStorageService.deleteByTitles(List.of(
				"title 1",
				"title 2",
				"title 3"
		)));
	}
}