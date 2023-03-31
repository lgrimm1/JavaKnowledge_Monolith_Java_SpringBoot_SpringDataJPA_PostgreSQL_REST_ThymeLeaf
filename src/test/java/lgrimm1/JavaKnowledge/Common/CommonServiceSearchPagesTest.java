package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Process.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.test.web.*;
import org.springframework.web.servlet.*;

import java.util.*;

import static org.mockito.Mockito.when;

public class CommonServiceSearchPagesTest {
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
	void searchPages_NullPayload() {
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
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.searchPages("list", null);

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void searchPages_NullSearchText() {
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
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		Payload incomingPayload = new Payload(
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
		ModelAndView modelAndView = commonService.searchPages("list", incomingPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void searchPages_BlankSearchText() {
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
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		Payload incomingPayload = new Payload(
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
		ModelAndView modelAndView = commonService.searchPages("list", incomingPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void searchPages_ExistingSearchText() {
		String searchText = "Word2 Word1";
		Payload incomingPayload = new Payload(
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
		Payload expectedPayload = new Payload(
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
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.searchPages("list", incomingPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "list");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}
}
