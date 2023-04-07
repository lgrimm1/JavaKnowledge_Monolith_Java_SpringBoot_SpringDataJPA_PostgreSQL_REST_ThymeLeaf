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
	void getPage_NullPayload() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
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
		Map<String, Object> map = new HashMap<>();
		map.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.getPage("page", null);

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void getPage_NullTitles() {
		String searchText = "text";
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

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
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
		Map<String, Object> map = new HashMap<>();
		map.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.getPage("page", payload);

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void getPage_EmptyTitles() {
		String searchText = "text";
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
				new ArrayList<>()
		);

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
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
		Map<String, Object> map = new HashMap<>();
		map.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.getPage("page", payload);

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void getPage_TooMuchTitles() {
		String searchText = "text";
		List<String> askedTitles = List.of("Title 1", "Title 2");
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
				askedTitles
		);

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
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
		Map<String, Object> map = new HashMap<>();
		map.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.getPage("page", payload);

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void getPage_BlankTitle() {
		String searchText = "text";
		List<String> askedTitles = List.of("  ");
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
				askedTitles
		);

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
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
		Map<String, Object> map = new HashMap<>();
		map.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.getPage("page", payload);

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void getPage_NotExistingTitle() {
		String searchText = "text";
		String title = "Title 3";
		List<String> askedTitles = List.of(title);
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
				askedTitles
		);

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.empty());

		Payload expectedPayload = new Payload(
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
		Map<String, Object> map = new HashMap<>();
		map.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.getPage("page", payload);

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void getPage_ExistingTitle() {
		String searchText = "text";
		String title = "Title 2";
		List<String> askedTitles = List.of(title);
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
				askedTitles
		);

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		String filename = "title_2";
		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.of(new TitleEntity(2L, title, filename, 2L, 2L)));

		List<String> content = List.of("Line 1", "Line 2");
		List<String> titleReferences = List.of("Reference 1", "Reference 2");
		when(htmlRepository.findById(2L))
				.thenReturn(Optional.of(new HtmlEntity(2L, content, titleReferences)));

		Payload expectedPayload = new Payload(
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				filename + ".html",
				null,
				titleReferences
		);
		Map<String, Object> map = new HashMap<>();
		map.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.getPage("page", payload);

		ModelAndViewAssert.assertViewName(modelAndView, "page");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}
}