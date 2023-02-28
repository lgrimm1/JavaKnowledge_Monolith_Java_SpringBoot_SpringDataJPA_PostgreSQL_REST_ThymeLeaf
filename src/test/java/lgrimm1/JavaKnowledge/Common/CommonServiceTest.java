package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Process.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.mockito.*;
import org.springframework.test.web.*;
import org.springframework.ui.*;
import org.springframework.web.servlet.*;

import java.io.*;
import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

class CommonServiceTest {

	TitleRepository titleRepository;
	TxtRepository txtRepository;
	HtmlRepository htmlRepository;
	Formulas formulas;
	ProcessRecords processRecords;
	FileOperations fileOperations;
	Extractors extractors;
	ProcessPage processPage;
	HtmlGenerators htmlGenerators;
	CommonService commonService;

	@BeforeEach
	void setUp() {
		titleRepository = Mockito.mock(TitleRepository.class);
		txtRepository = Mockito.mock(TxtRepository.class);
		htmlRepository = Mockito.mock(HtmlRepository.class);
		formulas = Mockito.mock(Formulas.class);
		processRecords = Mockito.mock(ProcessRecords.class);
		fileOperations = Mockito.mock(FileOperations.class);
		extractors = Mockito.mock(Extractors.class);
		processPage = Mockito.mock(ProcessPage.class);
		htmlGenerators = Mockito.mock(HtmlGenerators.class);
		commonService = new CommonService(
				titleRepository,
				txtRepository,
				htmlRepository,
				formulas,
				processRecords,
				fileOperations,
				extractors,
				processPage,
				htmlGenerators);
	}

	@Test
	void getRoot() {
		Map<String, Object> map = new HashMap<>();
		map.put("search_text", "");

		ModelAndView modelAndView = commonService.getRoot("root");
		ModelAndViewAssert.assertViewName(modelAndView, "root");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void searchPagesNullSearchText() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);

		ModelAndView modelAndView = commonService.searchPages("list", null);

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void searchPagesEmptySearchText() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);

		ModelAndView modelAndView = commonService.searchPages("list", "");

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void searchPagesBlankSearchText() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);

		ModelAndView modelAndView = commonService.searchPages("list", "  ");

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void searchPagesExistingSearchText() {
		String searchText = "Word2 Word1";
		Set<String> searchResult = Set.of("Title 2", "Title 1");
		when(processRecords.searchBySearchText(searchText, titleRepository, txtRepository))
				.thenReturn(searchResult);

		List<String> expectedList = List.of("Title 1", "Title 2");
		Map<String, Object> map = new HashMap<>();
		map.put("titles", expectedList);

		ModelAndView modelAndView = commonService.searchPages("list", searchText);

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void managePages() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", new ArrayList<>());
		map.put("confirm", false);
		map.put("message", "");

		ModelAndView modelAndView = commonService.managePages("management");

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void createSourcePage() {
		Map<String, Object> map = new HashMap<>();
		map.put("title", "");
		map.put("file_name", "");
		map.put("content", new ArrayList<>());
		map.put("edit_existing_page", false);
		map.put("message", "");

		ModelAndView modelAndView = commonService.createSourcePage("source");

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void publishPages() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		when(processRecords.publishHtml(titleRepository, htmlRepository, fileOperations))
				.thenReturn(new long[]{5L, 4L});

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", new ArrayList<>());
		map.put("confirm", false);
		map.put("message", "5 HTML files were pre-deleted, 4 were published.");

		ModelAndView modelAndView = commonService.publishPages("management");

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void importTxtNullFiles() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", new ArrayList<>());
		map.put("confirm", false);
		map.put("message", "Please upload minimum one file and confirm source overwriting.");

		ModelAndView modelAndView = commonService.importTxt("management", null, true);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void importTxtEmptyFiles() {
		List<File> files = new ArrayList<>();
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", new ArrayList<>());
		map.put("confirm", false);
		map.put("message", "Please upload minimum one file and confirm source overwriting.");

		ModelAndView modelAndView = commonService.importTxt("management", files, true);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void importTxtNullConfirm() {
		List<File> files = new ArrayList<>();
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", new ArrayList<>());
		map.put("confirm", false);
		map.put("message", "Please upload minimum one file and confirm source overwriting.");

		ModelAndView modelAndView = commonService.importTxt("management", files, null);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void importTxtNotConfirmed() {
		List<File> files = new ArrayList<>();
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", new ArrayList<>());
		map.put("confirm", false);
		map.put("message", "Please upload minimum one file and confirm source overwriting.");

		ModelAndView modelAndView = commonService.importTxt("management", files, false);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void importTxtConfirmed() {
		List<File> files = List.of(
				new File("file_1"),
				new File("file_2"),
				new File("file_3"),
				new File("file_4"),
				new File("file_5")
		);
		when(processRecords.importTxtFiles(files, titleRepository, txtRepository, htmlRepository, fileOperations, formulas, extractors))
				.thenReturn(List.of(new File("file_1"), new File("file_2"), new File("file_3")));

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", new ArrayList<>());
		map.put("confirm", false);
		map.put("message", "3 of 5 files were not imported.");

		ModelAndView modelAndView = commonService.importTxt("management", files, true);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void generateHtmlNullConfirm() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", new ArrayList<>());
		map.put("confirm", false);
		map.put("message", "Please confirm generating pages.");

		ModelAndView modelAndView = commonService.generateHtml("management", null);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void generateHtmlNotConfirmed() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", new ArrayList<>());
		map.put("confirm", false);
		map.put("message", "Please confirm generating pages.");

		ModelAndView modelAndView = commonService.generateHtml("management", false);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void generateHtmlConfirmed() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		when(processRecords.generate(titleRepository, txtRepository, htmlRepository, formulas, processPage, extractors, htmlGenerators))
				.thenReturn(new long[]{12, 24});

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", new ArrayList<>());
		map.put("confirm", false);
		map.put("message", "12 pages in 24 seconds has been processed.");

		ModelAndView modelAndView = commonService.generateHtml("management", true);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}
}