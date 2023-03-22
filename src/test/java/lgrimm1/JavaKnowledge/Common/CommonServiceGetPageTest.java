package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Process.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.test.web.*;
import org.springframework.web.servlet.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import static org.mockito.Mockito.*;

class CommonServiceGetPageTest {

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
	void getPage_NullTitles() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("search_text", "<all titles>");
		map.put("titles", titles);

		ModelAndView modelAndView = commonService.getPage("page", null);

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void getPage_EmptyTitles() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("search_text", "<all titles>");
		map.put("titles", titles);

		ModelAndView modelAndView = commonService.getPage("page", new ArrayList<>());

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void getPage_TooMuchTitles() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("search_text", "<all titles>");
		map.put("titles", titles);

		ModelAndView modelAndView = commonService.getPage("page", List.of("Title 1", "Title 2"));

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void getPage_BlankTitle() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("search_text", "<all titles>");
		map.put("titles", titles);

		ModelAndView modelAndView = commonService.getPage("page", List.of(" "));

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void getPage_NotExistingTitle() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		String title = "Title";
		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.empty());

		Map<String, Object> map = new HashMap<>();
		map.put("search_text", "<all titles>");
		map.put("titles", titles);

		ModelAndView modelAndView = commonService.getPage("page", List.of(title));

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void getPage_ExistingTitle() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		String title = "Title";
		String filename = "title";
		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.of(new TitleEntity(2L, title, filename, 2L, 2L)));

		List<String> content = List.of("Line 1", "Line 2");
		List<String> titleReferences = List.of("Reference 1", "Reference 2");
		when(htmlRepository.findById(2L))
				.thenReturn(Optional.of(new HtmlEntity(2L, content, titleReferences)));
		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("references", titleReferences);
		map.put("static_page_link", filename + ".html");

		ModelAndView modelAndView = commonService.getPage("page", List.of(title));

		ModelAndViewAssert.assertViewName(modelAndView, "page");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}
}