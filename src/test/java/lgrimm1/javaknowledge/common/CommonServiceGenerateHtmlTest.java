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

class CommonServiceGenerateHtmlTest {

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
	void generateHtml_NullPayload() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PLEASE CONFIRM GENERATING PAGES.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.generateHtml("management", null);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void generateHtml_NullConfirm() {
		List<String> receivedTitles = List.of("Title 1");
		Boolean confirm = null;
		String message = "message text";
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				confirm,
				null,
				null,
				message,
				null,
				null,
				"",
				receivedTitles
		);

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PLEASE CONFIRM GENERATING PAGES.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.generateHtml("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void generateHtml_NotConfirmed() {
		List<String> receivedTitles = List.of("Title 1");
		Boolean confirm = false;
		String message = "message text";
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				confirm,
				null,
				null,
				message,
				null,
				null,
				"",
				receivedTitles
		);

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PLEASE CONFIRM GENERATING PAGES.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.generateHtml("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void generateHtml_Confirmed() {
		List<String> receivedTitles = List.of("Title 1");
		Boolean confirm = true;
		String message = "message text";
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				confirm,
				null,
				null,
				message,
				null,
				null,
				"",
				receivedTitles
		);

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		when(processRecords.generate(titleRepository, txtRepository, htmlRepository, formulas, processPage, extractors, htmlGenerators))
				.thenReturn(new long[]{12, 24});

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"12 PAGES IN 24 SECONDS HAS BEEN PROCESSED.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.generateHtml("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}
}