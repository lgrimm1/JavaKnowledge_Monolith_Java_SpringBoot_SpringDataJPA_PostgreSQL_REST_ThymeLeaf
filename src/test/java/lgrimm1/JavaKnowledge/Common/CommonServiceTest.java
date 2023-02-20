package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Process.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.mockito.*;
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
		//setting up common mocked behaviours
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

		Assertions.assertEquals(1, result.getModel().size());

		Assertions.assertTrue(result.getModel().containsKey("search_text"));
		Assertions.assertTrue(result.getModel().get("search_text") instanceof String);
		Assertions.assertTrue(((String) result.getModel().get("search_text")).isEmpty());
	}

	@Test
	void searchPagesNoSearchText() {
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(List.of("Title 1", "Title 2"));
		ModelAndView result = commonService.searchPages(null, new ModelAndView("list", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("list", result.getViewName());

		Assertions.assertEquals(1, result.getModel().size());

		Assertions.assertTrue(result.getModel().containsKey("titles"));
		Assertions.assertTrue(result.getModel().get("titles") instanceof List);
		Assertions.assertEquals(List.of("Title 1", "Title 2"), result.getModel().get("titles"));
	}

	@Test
	void searchPagesBlankSearchText() {
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(List.of("Title 1", "Title 2"));
		ModelAndView result = commonService.searchPages("  ", new ModelAndView("list", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("list", result.getViewName());

		Assertions.assertEquals(1, result.getModel().size());

		Assertions.assertTrue(result.getModel().containsKey("titles"));
		Assertions.assertTrue(result.getModel().get("titles") instanceof List);
		Assertions.assertEquals(List.of("Title 1", "Title 2"), result.getModel().get("titles"));
	}

	@Test
	void searchPagesExistingSearchText() {
		when(processRecords.searchBySearchText("Word2 Word1", titleRepository, txtRepository))
				.thenReturn(Set.of("Title 2", "Title 1"));
		ModelAndView result = commonService.searchPages("  ", new ModelAndView("list", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("list", result.getViewName());

		Assertions.assertEquals(1, result.getModel().size());

		Assertions.assertTrue(result.getModel().containsKey("titles"));
		Assertions.assertTrue(result.getModel().get("titles") instanceof List);
		Assertions.assertEquals(List.of("Title 1", "Title 2"), result.getModel().get("titles"));
	}
}