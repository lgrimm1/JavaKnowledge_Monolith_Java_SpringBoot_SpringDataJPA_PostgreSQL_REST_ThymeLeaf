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
	}

	@Test
	void importTxt_NullPayload() {
		Payload receivedPayload = null;

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				false,
				null,
				null,
				null,
				"",
				"PLEASE UPLOAD MINIMUM ONE FILE AND CONFIRM SOURCE OVERWRITING.",
				null,
				null,
				null,
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
		String files = null;
		String message = "";
		List<String> receivedTitles = List.of("Title 1");
		Payload receivedPayload = new Payload(
				confirm,
				null,
				null,
				null,
				files,
				message,
				null,
				null,
				null,
				receivedTitles
		);

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				false,
				null,
				null,
				null,
				"",
				"PLEASE UPLOAD MINIMUM ONE FILE AND CONFIRM SOURCE OVERWRITING.",
				null,
				null,
				null,
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
		String files = "  ";
		String message = "";
		List<String> receivedTitles = List.of("Title 1");
		Payload receivedPayload = new Payload(
				confirm,
				null,
				null,
				null,
				files,
				message,
				null,
				null,
				null,
				receivedTitles
		);

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				false,
				null,
				null,
				null,
				"",
				"PLEASE UPLOAD MINIMUM ONE FILE AND CONFIRM SOURCE OVERWRITING.",
				null,
				null,
				null,
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
		String files = "title_3.txt;title_4.txt";
		String message = "";
		List<String> receivedTitles = List.of("Title 1");
		Payload receivedPayload = new Payload(
				confirm,
				null,
				null,
				null,
				files,
				message,
				null,
				null,
				null,
				receivedTitles
		);

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				false,
				null,
				null,
				null,
				"",
				"PLEASE UPLOAD MINIMUM ONE FILE AND CONFIRM SOURCE OVERWRITING.",
				null,
				null,
				null,
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
		String files = "title_3.txt;title_4.txt";
		String message = "";
		List<String> receivedTitles = List.of("Title 1");
		Payload receivedPayload = new Payload(
				confirm,
				null,
				null,
				null,
				files,
				message,
				null,
				null,
				null,
				receivedTitles
		);

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				false,
				null,
				null,
				null,
				"",
				"PLEASE UPLOAD MINIMUM ONE FILE AND CONFIRM SOURCE OVERWRITING.",
				null,
				null,
				null,
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
		String files = "title_1;title_2;title_3;title_4;title_5";
		String message = "";
		List<String> receivedTitles = List.of("Title 1");
		Payload receivedPayload = new Payload(
				confirm,
				null,
				null,
				null,
				files,
				message,
				null,
				null,
				null,
				receivedTitles
		);

		when(fileOperations.getOSPathSeparator())
				.thenReturn(";");

/*
		String notImportedFiles = "file_1;file_2;file_3";
		String allFiles = notImportedFiles + ";file_4;file_5";

		List<File> notImported = Stream.of(notImportedFiles.split(";"))
				.sorted()
				.map(File::new)
				.toList();
		List<File> all = Stream.of(allFiles.split(";"))
				.sorted()
				.map(File::new)
				.toList();
*/

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
				false,
				null,
				null,
				null,
				"",
				"3 OF 5 FILES WERE NOT IMPORTED.",
				null,
				null,
				null,
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.importTxt("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}
}