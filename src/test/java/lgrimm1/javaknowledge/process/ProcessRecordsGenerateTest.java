package lgrimm1.javaknowledge.process;

import lgrimm1.javaknowledge.databasestorage.*;
import lgrimm1.javaknowledge.datamodels.HtmlContentAndReferences;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;

class ProcessRecordsGenerateTest {

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
		databaseStorageService = Mockito.mock(DatabaseStorageService.class);
		processRecords = new ProcessRecords(databaseStorageService, fileOperations, extractors, processPage);
	}

	@Test
	void generate() {
		String title1 = "Title 1";
		String title2 = "Title 2";
		String title3 = "Title 3";
		String txtContentString2 = "Line 21\nLine 22\n";
		String txtContentString3 = "Line 31\nLine 32\n";
		List<String> txtContentList2 = List.of("Line 21", "Line 22");
		List<String> txtContentList3 = List.of("Line 31", "Line 32");
		String htmlContentString2 = "New Line 21\nNew Line 22\n";
		String htmlContentString3 = "New Line 31\nNew Line 32\n";
		List<String> htmlContentList2 = List.of("New Line 21", "New Line 22");
		List<String> htmlContentList3 = List.of("New Line 31", "New Line 32");
		HtmlContentAndReferences htmlContentAndReferences2 =
				new HtmlContentAndReferences(htmlContentList2, new ArrayList<>());
		HtmlContentAndReferences htmlContentAndReferences3 =
				new HtmlContentAndReferences(htmlContentList3, new ArrayList<>());
		TxtEntity txtEntity2 = new TxtEntity(22L, txtContentString2);
		TxtEntity txtEntity3 = new TxtEntity(33L, txtContentString3);
		HtmlEntity givenHtmlEntity2 = new HtmlEntity(htmlContentString2, "");
		HtmlEntity savedHtmlEntity2 = new HtmlEntity(444L, htmlContentString2, "");
		HtmlEntity originalHtmlEntity3 = new HtmlEntity(333L, "original html content 3", "original html references 3");
		HtmlEntity givenHtmlEntity3 = new HtmlEntity(333L, htmlContentString3, "");
		TitleEntity originalTitleEntity1 = new TitleEntity(1L, title1, 11L, 111L);
		TitleEntity originalTitleEntity2 = new TitleEntity(2L, title2, 22L, 222L);
		TitleEntity originalTitleEntity3 = new TitleEntity(3L, title3, 33L, 333L);

		when(databaseStorageService.findAllTitles())
				.thenReturn(List.of(
						originalTitleEntity1,
						originalTitleEntity2,
						originalTitleEntity3
				));

		when(databaseStorageService.findTxtById(11L))
				.thenReturn(Optional.empty());
		when(databaseStorageService.findTxtById(22L))
				.thenReturn(Optional.of(txtEntity2));
		when(databaseStorageService.findTxtById(33L))
				.thenReturn(Optional.of(txtEntity3));

		when(processPage.processTxt(txtContentList2, title2))
				.thenReturn(htmlContentAndReferences2);
		when(processPage.processTxt(txtContentList3, title3))
				.thenReturn(htmlContentAndReferences3);

		when(databaseStorageService.findHtmlById(222L))
				.thenReturn(Optional.empty());
		when(databaseStorageService.findHtmlById(333L))
				.thenReturn(Optional.of(originalHtmlEntity3));

		when(databaseStorageService.saveHtml(givenHtmlEntity2))
				.thenReturn(savedHtmlEntity2);
		when(databaseStorageService.saveHtml(givenHtmlEntity3))
				.thenReturn(givenHtmlEntity3);

		long[] result = processRecords.generate();
		Assertions.assertEquals(3, result[0]);
		Assertions.assertTrue(result[1] > 0);

		when(databaseStorageService.saveHtml(givenHtmlEntity3))
				.thenThrow(new RuntimeException("html 3 saved"));
		Exception e = Assertions.assertThrows(Exception.class, () -> processRecords.generate());
		Assertions.assertEquals("html 3 saved", e.getMessage());
	}
}