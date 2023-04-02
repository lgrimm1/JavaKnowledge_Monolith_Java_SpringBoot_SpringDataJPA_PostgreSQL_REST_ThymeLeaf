package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Process.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.test.web.*;
import org.springframework.ui.*;
import org.springframework.web.servlet.*;

import java.util.*;

import static org.mockito.Mockito.*;

class CommonServiceDeletePagesTest {

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
	void deletePages_NullTitles() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				false,
				null,
				null,
				null,
				"",
				"PLEASE SELECT TITLES YOU WISH TO DELETE.",
				null,
				null,
				null,
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.deletePages("management", null, true);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void deletePages_NoTitles() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				false,
				null,
				null,
				null,
				"",
				"PLEASE SELECT TITLES YOU WISH TO DELETE.",
				null,
				null,
				null,
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.deletePages("management", new ArrayList<>(), true);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void deletePages_NotConfirmed() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				false,
				null,
				null,
				null,
				"",
				"PLEASE CONFIRM DELETION.",
				null,
				null,
				null,
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		List<String> requestTitles = List.of("Title 3", "Title 4");
		ModelAndView modelAndView = commonService.deletePages("management", requestTitles, false);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void deletePages_WithNoValidTitles() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				false,
				null,
				null,
				null,
				"",
				"PLEASE SELECT EXISTING TITLES YOU WISH TO DELETE.",
				null,
				null,
				null,
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		List<String> requestTitles = new ArrayList<>();
		requestTitles.add(null);
		requestTitles.add("  ");
		ModelAndView modelAndView = commonService.deletePages("management", requestTitles, true);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void deletePages_WithValidTitles() {
		List<String> restOfTitles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(restOfTitles);

		List<String> cleanedRequestTitles = List.of("Title 3", "Title 4");
		when(processRecords.deleteByTitles(cleanedRequestTitles, titleRepository, txtRepository, htmlRepository))
				.thenReturn(1L);

		Payload expectedPayload = new Payload(
				false,
				null,
				null,
				null,
				"",
				"1 OF 2 TITLES WERE DELETED.",
				null,
				null,
				null,
				restOfTitles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		List<String> requestTitles = new ArrayList<>();
		requestTitles.add(null);
		requestTitles.add("  ");
		requestTitles.addAll(cleanedRequestTitles);
		ModelAndView modelAndView = commonService.deletePages("management", requestTitles, true);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}
}