package lgrimm1.javaknowledge.common;

import lgrimm1.javaknowledge.html.*;
import lgrimm1.javaknowledge.process.*;
import lgrimm1.javaknowledge.title.*;
import lgrimm1.javaknowledge.txt.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.test.web.*;
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
		when(formulas.getTitleManagement())
				.thenReturn("MANAGEMENTTITLE");
	}

	@Test
	void deletePages_NullPayload() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PLEASE SELECT TITLES YOU WISH TO DELETE.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.deletePages("management", null);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void deletePages_NullTitles() {
		Boolean confirm = true;
		String message = "";
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				confirm,
				null,
				null,
				message,
				null,
				null,
				"",
				null
		);

		List<String> expectedTitles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(expectedTitles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PLEASE SELECT TITLES YOU WISH TO DELETE.",
				null,
				null,
				"",
				expectedTitles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.deletePages("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void deletePages_NoTitles() {
		Boolean confirm = true;
		String message = "";
		List<String> titles = new ArrayList<>();
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				confirm,
				null,
				null,
				message,
				null,
				null,
				"",
				titles
		);

		List<String> expectedTitles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(expectedTitles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PLEASE SELECT TITLES YOU WISH TO DELETE.",
				null,
				null,
				"",
				expectedTitles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.deletePages("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void deletePages_NotConfirmed() {
		Boolean confirm = false;
		String message = "";
		List<String> titles = List.of("Title 1");
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				confirm,
				null,
				null,
				message,
				null,
				null,
				"",
				titles
		);

		List<String> expectedTitles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(expectedTitles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PLEASE CONFIRM DELETION.",
				null,
				null,
				"",
				expectedTitles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.deletePages("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void deletePages_WithNoValidTitles() {
		List<String> requestTitles = new ArrayList<>();
		requestTitles.add(null);
		requestTitles.add("  ");

		Boolean confirm = true;
		String message = "";
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				confirm,
				null,
				null,
				message,
				null,
				null,
				"",
				requestTitles
		);

		List<String> restOfTitles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(restOfTitles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PLEASE SELECT EXISTING TITLES YOU WISH TO DELETE.",
				null,
				null,
				"",
				restOfTitles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.deletePages("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void deletePages_WithValidTitles() {
		List<String> cleanedRequestTitles = List.of("Title 3", "Title 4");
		when(processRecords.deleteByTitles(cleanedRequestTitles, titleRepository, txtRepository, htmlRepository))
				.thenReturn(1L);

		List<String> requestTitles = new ArrayList<>();
		requestTitles.add(null);
		requestTitles.add("  ");
		requestTitles.addAll(cleanedRequestTitles);

		Boolean confirm = true;
		String message = "";
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				confirm,
				null,
				null,
				message,
				null,
				null,
				"",
				requestTitles
		);

		List<String> restOfTitles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(restOfTitles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"1 OF 2 TITLES WERE DELETED.",
				null,
				null,
				"",
				restOfTitles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.deletePages("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}
}