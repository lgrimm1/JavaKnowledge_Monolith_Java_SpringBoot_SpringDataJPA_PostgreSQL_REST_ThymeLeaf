package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Process.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.mockito.*;
import org.springframework.ui.*;
import org.springframework.web.servlet.*;

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
		ModelAndView result = commonService.getRoot(new ModelAndView("root", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("root", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(1, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("search_text"));
		Assertions.assertTrue(modelMap.getAttribute("search_text") instanceof String);
		Assertions.assertTrue(((String) modelMap.getAttribute("search_text")).isEmpty());
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
		Assertions.assertNotNull(modelMap.getAttribute("files"));
		Assertions.assertTrue(((List<?>) modelMap.getAttribute("files")).isEmpty());

		Assertions.assertTrue(modelMap.containsAttribute("confirm"));
		Assertions.assertTrue(modelMap.getAttribute("confirm") instanceof Boolean);
		Assertions.assertNotNull(modelMap.getAttribute("confirm"));
		Assertions.assertFalse((Boolean) modelMap.getAttribute("confirm"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertNotNull(modelMap.getAttribute("message"));
		Assertions.assertTrue(((String) modelMap.getAttribute("message")).isEmpty());
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
		Assertions.assertNotNull(modelMap.getAttribute("title"));
		Assertions.assertTrue(((String) modelMap.getAttribute("title")).isEmpty());

		Assertions.assertTrue(modelMap.containsAttribute("file_name"));
		Assertions.assertTrue(modelMap.getAttribute("file_name") instanceof String);
		Assertions.assertNotNull(modelMap.getAttribute("file_name"));
		Assertions.assertTrue(((String) modelMap.getAttribute("file_name")).isEmpty());

		Assertions.assertTrue(modelMap.containsAttribute("content"));
		Assertions.assertTrue(modelMap.getAttribute("content") instanceof List);
		Assertions.assertNotNull(modelMap.getAttribute("content"));
		Assertions.assertTrue(((List<?>) modelMap.getAttribute("content")).isEmpty());

		Assertions.assertTrue(modelMap.containsAttribute("edit_existing_page"));
		Assertions.assertTrue(modelMap.getAttribute("edit_existing_page") instanceof Boolean);
		Assertions.assertNotNull(modelMap.getAttribute("edit_existing_page"));
		Assertions.assertFalse((Boolean) modelMap.getAttribute("edit_existing_page"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertNotNull(modelMap.getAttribute("message"));
		Assertions.assertTrue(((String) modelMap.getAttribute("message")).isEmpty());
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
		Assertions.assertNotNull(modelMap.getAttribute("files"));
		Assertions.assertTrue(((List<?>) modelMap.getAttribute("files")).isEmpty());

		Assertions.assertTrue(modelMap.containsAttribute("confirm"));
		Assertions.assertTrue(modelMap.getAttribute("confirm") instanceof Boolean);
		Assertions.assertNotNull(modelMap.getAttribute("confirm"));
		Assertions.assertFalse((Boolean) modelMap.getAttribute("confirm"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertNotNull(modelMap.getAttribute("message"));
		Assertions.assertEquals("5 HTML files were pre-deleted, 4 were published.", (String) modelMap.getAttribute("message"));
	}
}