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
/*
		Map<String, Object> expectedModelMap = new HashMap<>();
		expectedModelMap.put("search_text", "");

		ModelAndView actualModelAndView = commonService.getRoot("root");
		ModelAndViewAssert.assertViewName(actualModelAndView, "root");
		ModelAndViewAssert.assertModelAttributeValues(actualModelAndView, expectedModelMap);
*/
	}

	@Test
	void searchPagesNoSearchText() {
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(List.of("Title 1", "Title 2"));
		ModelAndView result = commonService.searchPages(null, new ModelAndView("list", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("list", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(1, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("titles"));
		Assertions.assertTrue(modelMap.getAttribute("titles") instanceof List);
		Assertions.assertEquals(List.of("Title 1", "Title 2"), modelMap.getAttribute("titles"));
	}

	@Test
	void searchPagesEmptySearchText() {
		String searchText = "";
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(List.of("Title 1", "Title 2"));
		ModelAndView result = commonService.searchPages(searchText, new ModelAndView("list", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("list", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(1, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("titles"));
		Assertions.assertTrue(modelMap.getAttribute("titles") instanceof List);
		Assertions.assertEquals(List.of("Title 1", "Title 2"), modelMap.getAttribute("titles"));
	}

	@Test
	void searchPagesBlankSearchText() {
		String searchText = "  ";
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(List.of("Title 1", "Title 2"));
		ModelAndView result = commonService.searchPages(searchText, new ModelAndView("list", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("list", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(1, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("titles"));
		Assertions.assertTrue(modelMap.getAttribute("titles") instanceof List);
		Assertions.assertEquals(List.of("Title 1", "Title 2"), modelMap.getAttribute("titles"));
	}

	@Test
	void searchPagesExistingSearchText() {
		String searchText = "Word2 Word1";
		when(processRecords.searchBySearchText(searchText, titleRepository, txtRepository))
				.thenReturn(Set.of("Title 2", "Title 1"));
		ModelAndView result = commonService.searchPages(searchText, new ModelAndView("list", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("list", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(1, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("titles"));
		Assertions.assertTrue(modelMap.getAttribute("titles") instanceof List);
		Assertions.assertEquals(List.of("Title 1", "Title 2"), modelMap.getAttribute("titles"));
	}

	@Test
	void managePages() {
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(List.of("Title 1", "Title 2"));
		ModelAndView result = commonService.managePages(new ModelAndView("management", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("management", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(4, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("titles"));
		Assertions.assertTrue(modelMap.getAttribute("titles") instanceof List);
		Assertions.assertEquals(List.of("Title 1", "Title 2"), modelMap.getAttribute("titles"));

		Assertions.assertTrue(modelMap.containsAttribute("files"));
		Assertions.assertTrue(modelMap.getAttribute("files") instanceof List);
		Assertions.assertEquals(new ArrayList<>(), modelMap.getAttribute("files"));

		Assertions.assertTrue(modelMap.containsAttribute("confirm"));
		Assertions.assertTrue(modelMap.getAttribute("confirm") instanceof Boolean);
		Assertions.assertEquals(false, modelMap.getAttribute("confirm"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("", modelMap.getAttribute("message"));
	}

	@Test
	void createSourcePage() {
		ModelAndView result = commonService.createSourcePage(new ModelAndView("source", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("source", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(5, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("title"));
		Assertions.assertTrue(modelMap.getAttribute("title") instanceof String);
		Assertions.assertEquals("", modelMap.getAttribute("title"));

		Assertions.assertTrue(modelMap.containsAttribute("file_name"));
		Assertions.assertTrue(modelMap.getAttribute("file_name") instanceof String);
		Assertions.assertEquals("", modelMap.getAttribute("file_name"));

		Assertions.assertTrue(modelMap.containsAttribute("content"));
		Assertions.assertTrue(modelMap.getAttribute("content") instanceof List);
		Assertions.assertEquals(new ArrayList<>(), modelMap.getAttribute("content"));

		Assertions.assertTrue(modelMap.containsAttribute("edit_existing_page"));
		Assertions.assertTrue(modelMap.getAttribute("edit_existing_page") instanceof Boolean);
		Assertions.assertEquals(false, modelMap.getAttribute("edit_existing_page"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("", modelMap.getAttribute("message"));
	}

	@Test
	void publishPages() {
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(List.of("Title 1", "Title 2"));
		when(processRecords.publishHtml(titleRepository, htmlRepository, fileOperations))
				.thenReturn(new long[]{5L, 4L});
		ModelAndView result = commonService.publishPages(new ModelAndView("management", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("management", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(4, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("titles"));
		Assertions.assertTrue(modelMap.getAttribute("titles") instanceof List);
		Assertions.assertEquals(List.of("Title 1", "Title 2"), modelMap.getAttribute("titles"));

		Assertions.assertTrue(modelMap.containsAttribute("files"));
		Assertions.assertTrue(modelMap.getAttribute("files") instanceof List);
		Assertions.assertEquals(new ArrayList<>(), modelMap.getAttribute("files"));

		Assertions.assertTrue(modelMap.containsAttribute("confirm"));
		Assertions.assertTrue(modelMap.getAttribute("confirm") instanceof Boolean);
		Assertions.assertEquals(false, modelMap.getAttribute("confirm"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("5 HTML files were pre-deleted, 4 were published.", modelMap.getAttribute("message"));
	}

	@Test
	void importTxtNullFiles() {
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(List.of("Title 1", "Title 2"));
		ModelAndView result = commonService.importTxt(null, true, new ModelAndView("management", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("management", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(4, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("titles"));
		Assertions.assertTrue(modelMap.getAttribute("titles") instanceof List);
		Assertions.assertEquals(List.of("Title 1", "Title 2"), modelMap.getAttribute("titles"));

		Assertions.assertTrue(modelMap.containsAttribute("files"));
		Assertions.assertTrue(modelMap.getAttribute("files") instanceof List);
		Assertions.assertEquals(new ArrayList<>(), modelMap.getAttribute("files"));

		Assertions.assertTrue(modelMap.containsAttribute("confirm"));
		Assertions.assertTrue(modelMap.getAttribute("confirm") instanceof Boolean);
		Assertions.assertEquals(false, modelMap.getAttribute("confirm"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("Please upload minimum one file and confirm source overwriting.", modelMap.getAttribute("message"));
	}

	@Test
	void importTxtEmptyFiles() {
		List<File> files = new ArrayList<>();
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(List.of("Title 1", "Title 2"));
		ModelAndView result = commonService.importTxt(files, true, new ModelAndView("management", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("management", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(4, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("titles"));
		Assertions.assertTrue(modelMap.getAttribute("titles") instanceof List);
		Assertions.assertEquals(List.of("Title 1", "Title 2"), modelMap.getAttribute("titles"));

		Assertions.assertTrue(modelMap.containsAttribute("files"));
		Assertions.assertTrue(modelMap.getAttribute("files") instanceof List);
		Assertions.assertEquals(new ArrayList<>(), modelMap.getAttribute("files"));

		Assertions.assertTrue(modelMap.containsAttribute("confirm"));
		Assertions.assertTrue(modelMap.getAttribute("confirm") instanceof Boolean);
		Assertions.assertEquals(false, modelMap.getAttribute("confirm"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("Please upload minimum one file and confirm source overwriting.", modelMap.getAttribute("message"));
	}

	@Test
	void importTxtNullConfirm() {
		List<File> files = new ArrayList<>();
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(List.of("Title 1", "Title 2"));
		ModelAndView result = commonService.importTxt(files, null, new ModelAndView("management", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("management", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(4, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("titles"));
		Assertions.assertTrue(modelMap.getAttribute("titles") instanceof List);
		Assertions.assertEquals(List.of("Title 1", "Title 2"), modelMap.getAttribute("titles"));

		Assertions.assertTrue(modelMap.containsAttribute("files"));
		Assertions.assertTrue(modelMap.getAttribute("files") instanceof List);
		Assertions.assertEquals(new ArrayList<>(), modelMap.getAttribute("files"));

		Assertions.assertTrue(modelMap.containsAttribute("confirm"));
		Assertions.assertTrue(modelMap.getAttribute("confirm") instanceof Boolean);
		Assertions.assertEquals(false, modelMap.getAttribute("confirm"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("Please upload minimum one file and confirm source overwriting.", modelMap.getAttribute("message"));
	}

	@Test
	void importTxtNotConfirmed() {
		List<File> files = new ArrayList<>();
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(List.of("Title 1", "Title 2"));
		ModelAndView result = commonService.importTxt(files, false, new ModelAndView("management", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("management", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(4, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("titles"));
		Assertions.assertTrue(modelMap.getAttribute("titles") instanceof List);
		Assertions.assertEquals(List.of("Title 1", "Title 2"), modelMap.getAttribute("titles"));

		Assertions.assertTrue(modelMap.containsAttribute("files"));
		Assertions.assertTrue(modelMap.getAttribute("files") instanceof List);
		Assertions.assertEquals(new ArrayList<>(), modelMap.getAttribute("files"));

		Assertions.assertTrue(modelMap.containsAttribute("confirm"));
		Assertions.assertTrue(modelMap.getAttribute("confirm") instanceof Boolean);
		Assertions.assertEquals(false, modelMap.getAttribute("confirm"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("Please upload minimum one file and confirm source overwriting.", modelMap.getAttribute("message"));
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
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(List.of("Title 1", "Title 2"));
		ModelAndView result = commonService.importTxt(files, true, new ModelAndView("management", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("management", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(4, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("titles"));
		Assertions.assertTrue(modelMap.getAttribute("titles") instanceof List);
		Assertions.assertEquals(List.of("Title 1", "Title 2"), modelMap.getAttribute("titles"));

		Assertions.assertTrue(modelMap.containsAttribute("files"));
		Assertions.assertTrue(modelMap.getAttribute("files") instanceof List);
		Assertions.assertEquals(new ArrayList<>(), modelMap.getAttribute("files"));

		Assertions.assertTrue(modelMap.containsAttribute("confirm"));
		Assertions.assertTrue(modelMap.getAttribute("confirm") instanceof Boolean);
		Assertions.assertEquals(false, modelMap.getAttribute("confirm"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("3 of 5 files were not imported.", modelMap.getAttribute("message"));
	}

	@Test
	void generateHtmlNullConfirm() {
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(List.of("Title 1", "Title 2"));
		ModelAndView result = commonService.generateHtml(null, new ModelAndView("management", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("management", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(4, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("titles"));
		Assertions.assertTrue(modelMap.getAttribute("titles") instanceof List);
		Assertions.assertEquals(List.of("Title 1", "Title 2"), modelMap.getAttribute("titles"));

		Assertions.assertTrue(modelMap.containsAttribute("files"));
		Assertions.assertTrue(modelMap.getAttribute("files") instanceof List);
		Assertions.assertEquals(new ArrayList<>(), modelMap.getAttribute("files"));

		Assertions.assertTrue(modelMap.containsAttribute("confirm"));
		Assertions.assertTrue(modelMap.getAttribute("confirm") instanceof Boolean);
		Assertions.assertEquals(false, modelMap.getAttribute("confirm"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("Please confirm generating pages.", modelMap.getAttribute("message"));
	}

	@Test
	void generateHtmlNotConfirmed() {
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(List.of("Title 1", "Title 2"));
		ModelAndView result = commonService.generateHtml(false, new ModelAndView("management", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("management", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(4, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("titles"));
		Assertions.assertTrue(modelMap.getAttribute("titles") instanceof List);
		Assertions.assertEquals(List.of("Title 1", "Title 2"), modelMap.getAttribute("titles"));

		Assertions.assertTrue(modelMap.containsAttribute("files"));
		Assertions.assertTrue(modelMap.getAttribute("files") instanceof List);
		Assertions.assertEquals(new ArrayList<>(), modelMap.getAttribute("files"));

		Assertions.assertTrue(modelMap.containsAttribute("confirm"));
		Assertions.assertTrue(modelMap.getAttribute("confirm") instanceof Boolean);
		Assertions.assertEquals(false, modelMap.getAttribute("confirm"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("Please confirm generating pages.", modelMap.getAttribute("message"));
	}

	@Test
	void generateHtmlConfirmed() {
		when(processRecords.generate(titleRepository, txtRepository, htmlRepository, formulas, processPage, extractors, htmlGenerators))
				.thenReturn(new long[]{12, 24});
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(List.of("Title 1", "Title 2"));
		ModelAndView result = commonService.generateHtml(true, new ModelAndView("management", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("management", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(4, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("titles"));
		Assertions.assertTrue(modelMap.getAttribute("titles") instanceof List);
		Assertions.assertEquals(List.of("Title 1", "Title 2"), modelMap.getAttribute("titles"));

		Assertions.assertTrue(modelMap.containsAttribute("files"));
		Assertions.assertTrue(modelMap.getAttribute("files") instanceof List);
		Assertions.assertEquals(new ArrayList<>(), modelMap.getAttribute("files"));

		Assertions.assertTrue(modelMap.containsAttribute("confirm"));
		Assertions.assertTrue(modelMap.getAttribute("confirm") instanceof Boolean);
		Assertions.assertEquals(false, modelMap.getAttribute("confirm"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("12 pages in 24 seconds has been processed.", modelMap.getAttribute("message"));
	}
}