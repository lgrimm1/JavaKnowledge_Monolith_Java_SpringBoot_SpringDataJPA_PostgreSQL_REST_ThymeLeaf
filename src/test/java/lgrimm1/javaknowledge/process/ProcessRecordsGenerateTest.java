package lgrimm1.javaknowledge.process;

import lgrimm1.javaknowledge.databasestorage.*;
import lgrimm1.javaknowledge.datamodels.HtmlContentAndReferences;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;

class ProcessRecordsGenerateTest {

	ProcessRecords processRecords;
/*
	TitleRepository titleRepository;
	TxtRepository txtRepository;
	HtmlRepository htmlRepository;
*/
	FileOperations fileOperations;
	Extractors extractors;
	ProcessPage processPage;
	HtmlGenerators htmlGenerators;
	DatabaseStorageService databaseStorageService;

	@BeforeEach
	void setUp() {
/*
		titleRepository = Mockito.mock(TitleRepository.class);
		txtRepository = Mockito.mock(TxtRepository.class);
		htmlRepository = Mockito.mock(HtmlRepository.class);
*/
		fileOperations = Mockito.mock(FileOperations.class);
		extractors = Mockito.mock(Extractors.class);
		processPage = Mockito.mock(ProcessPage.class);
		htmlGenerators = Mockito.mock(HtmlGenerators.class);
		databaseStorageService = Mockito.mock(DatabaseStorageService.class);
		processRecords = new ProcessRecords(databaseStorageService, fileOperations, extractors, processPage);
	}

	@Test
	void generate() {
		String title1 = "Title 1";
		String title2 = "Title 2";
		String title3 = "Title 3";
		String txtContentString2 = "Line 21\nLine 22\n";
		List<String> txtContentList2 = List.of("Line 21", "Line 22");
		List<String> htmlContentList2 = List.of("New Line 21", "New Line 22");
		String txtContentString3 = "Line 31\nLine 32\n";
		List<String> txtContentList3 = List.of("Line 31", "Line 32");
		List<String> htmlContentList3 = List.of("New Line 31", "New Line 32");
		TitleEntity titleEntityWithoutTxtEntity = new TitleEntity(1L, title1, "title_1", 1L, 1L);
		TitleEntity titleEntityWithTxtEntityWithoutHtmlEntity = new TitleEntity(2L, title2, "title_2", 2L, 2L);
		TitleEntity titleEntityWithTxtEntityWithHtmlEntity = new TitleEntity(3L, title3, "title_3", 3L, 3L);

		when(databaseStorageService.findAllTitles())
				.thenReturn(List.of(
						titleEntityWithoutTxtEntity,
						titleEntityWithTxtEntityWithoutHtmlEntity,
						titleEntityWithTxtEntityWithHtmlEntity
				));

		when(databaseStorageService.findTxtById(1L))
				.thenReturn(Optional.empty());
		when(databaseStorageService.findTxtById(2L))
				.thenReturn(Optional.of(new TxtEntity(2L, txtContentString2)));
		when(databaseStorageService.findTxtById(3L))
				.thenReturn(Optional.of(new TxtEntity(3L, txtContentString3)));

		when(databaseStorageService.findHtmlById(2L))
				.thenReturn(Optional.empty());
		when(databaseStorageService.findHtmlById(3L))
				.thenReturn(Optional.of(new HtmlEntity(3L, List.of("original HTML content 3"), List.of("original page references 3"))));

		when(processPage.processTxt(txtContentList2, title2))
				.thenReturn(new HtmlContentAndReferences(List.of(
						"New Line 21",
						"New Line 22"
				), new ArrayList<>()));
		when(processPage.processTxt(txtContentList3, title3))
				.thenReturn(new HtmlContentAndReferences(List.of(
						"New Line 31",
						"New Line 32"
				), new ArrayList<>()));

		when(databaseStorageService.saveHtml(new HtmlEntity(htmlContentList2, new ArrayList<>())))
				.thenReturn(new HtmlEntity(12L, htmlContentList2, new ArrayList<>()));
		when(databaseStorageService.saveHtml(new HtmlEntity(htmlContentList3, new ArrayList<>())))
				.thenReturn(new HtmlEntity(13L, htmlContentList3, new ArrayList<>()));

		long[] result = processRecords.generate();
		Assertions.assertEquals(2, result[0]);
		Assertions.assertTrue(result[1] > 0);

		when(databaseStorageService.saveHtml(new HtmlEntity(htmlContentList3, new ArrayList<>())))
				.thenThrow(new RuntimeException("existing html 3 saved"));
		Exception e = Assertions.assertThrows(Exception.class, () -> processRecords.generate());
		Assertions.assertEquals("existing html 3 saved", e.getMessage());
	}
}