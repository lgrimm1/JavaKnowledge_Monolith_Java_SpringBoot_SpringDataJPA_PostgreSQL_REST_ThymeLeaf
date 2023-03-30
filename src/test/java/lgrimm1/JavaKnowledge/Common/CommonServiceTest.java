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
import java.util.stream.*;

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
		Payload payload = new Payload(
				null,
				null,
				null,
				null,
				null,
				null,
				"",
				null,
				null,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", payload);

		ModelAndView modelAndView = commonService.getRoot("root");
		ModelAndViewAssert.assertViewName(modelAndView, "root");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void searchPages_NullPayload() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload payload = new Payload(
				null,
				null,
				null,
				null,
				null,
				null,
				"<all titles>",
				null,
				null,
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", payload);

		ModelAndView modelAndView = commonService.searchPages("list", null);

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void searchPages_NullSearchText() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload payload = new Payload(
				null,
				null,
				null,
				null,
				null,
				null,
				"<all titles>",
				null,
				null,
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", payload);

		Payload payload2 = new Payload(
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null
		);
		ModelAndView modelAndView = commonService.searchPages("list", payload2);

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void searchPages_BlankSearchText() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload payload = new Payload(
				null,
				null,
				null,
				null,
				null,
				null,
				"<all titles>",
				null,
				null,
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", payload);

		Payload payload2 = new Payload(
				null,
				null,
				null,
				null,
				null,
				null,
				"  ",
				null,
				null,
				null
		);
		ModelAndView modelAndView = commonService.searchPages("list", payload2);

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void searchPages_ExistingSearchText() {
		String searchText = "Word2 Word1";
		Payload payload = new Payload(
				null,
				null,
				null,
				null,
				null,
				null,
				searchText,
				null,
				null,
				null
		);

		Set<String> titlesSet = Set.of("Title 2", "Title 1");
		when(processRecords.searchBySearchText(searchText, titleRepository, txtRepository))
				.thenReturn(titlesSet);

		List<String> titles = List.of("Title 1", "Title 2");
		Payload payload2 = new Payload(
				null,
				null,
				null,
				null,
				null,
				null,
				searchText,
				null,
				null,
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", payload2);

		ModelAndView modelAndView = commonService.searchPages("list", payload);

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void managePages() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", "");
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
		map.put("files", "");
		map.put("confirm", false);
		map.put("message", "5 HTML files were pre-deleted, 4 were published.");

		ModelAndView modelAndView = commonService.publishPages("management");

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void generateHtml_NullConfirm() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", "");
		map.put("confirm", false);
		map.put("message", "Please confirm generating pages.");

		ModelAndView modelAndView = commonService.generateHtml("management", null);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void generateHtml_NotConfirmed() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", "");
		map.put("confirm", false);
		map.put("message", "Please confirm generating pages.");

		ModelAndView modelAndView = commonService.generateHtml("management", false);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void generateHtml_Confirmed() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		when(processRecords.generate(titleRepository, txtRepository, htmlRepository, formulas, processPage, extractors, htmlGenerators))
				.thenReturn(new long[]{12, 24});

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", "");
		map.put("confirm", false);
		map.put("message", "12 pages in 24 seconds has been processed.");

		ModelAndView modelAndView = commonService.generateHtml("management", true);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}
}