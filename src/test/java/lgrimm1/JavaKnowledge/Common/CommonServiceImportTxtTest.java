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

class CommonServiceImportTxtTest {

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

/*
MultipartFile[] tests are needed!
	@Test
	void importTxt_NullPayload() {
		Payload receivedPayload = null;

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PLEASE UPLOAD MINIMUM ONE FILE AND CONFIRM SOURCE OVERWRITING.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.importTxt("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void importTxt_NullFiles() {
		Boolean confirm = true;
		String message = "";
		List<String> receivedTitles = List.of("Title 1");
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
				"PLEASE UPLOAD MINIMUM ONE FILE AND CONFIRM SOURCE OVERWRITING.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.importTxt("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void importTxt_BlankFiles() {
		Boolean confirm = true;
		String message = "";
		List<String> receivedTitles = List.of("Title 1");
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
				"PLEASE UPLOAD MINIMUM ONE FILE AND CONFIRM SOURCE OVERWRITING.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.importTxt("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void importTxt_NullConfirm() {
		Boolean confirm = null;
		String message = "";
		List<String> receivedTitles = List.of("Title 1");
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
				"PLEASE UPLOAD MINIMUM ONE FILE AND CONFIRM SOURCE OVERWRITING.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.importTxt("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void importTxt_NotConfirmed() {
		Boolean confirm = false;
		String message = "";
		List<String> receivedTitles = List.of("Title 1");
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
				"PLEASE UPLOAD MINIMUM ONE FILE AND CONFIRM SOURCE OVERWRITING.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.importTxt("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void importTxt_Confirmed() {
		Boolean confirm = true;
		String message = "";
		List<String> receivedTitles = List.of("Title 1");
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

		when(fileOperations.getOSPathSeparator())
				.thenReturn(";");

		List<File> allFiles = List.of(
				new File("title_1"),
				new File("title_2"),
				new File("title_3"),
				new File("title_4"),
				new File("title_5")
		);
		List<File> notImportedFiles = List.of(
				new File("title_1"),
				new File("title_2"),
				new File("title_3")
		);
		when(processRecords.importTxtFiles(
				allFiles,
				titleRepository,
				txtRepository,
				htmlRepository,
				fileOperations,
				formulas,
				extractors))
				.thenReturn(notImportedFiles);

		List<String> titles = List.of("Title 4", "Title 5");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"3 OF 5 FILES WERE NOT IMPORTED.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.importTxt("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}
*/
}