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

class CommonServiceSavePageTest {

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
	void savePage_NullPayload() {
		Payload expectedPayload = new Payload(
				null,
				"",
				false,
				null,
				"THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.",
				null,
				null,
				"",
				null
		);

		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.savePage("source", null);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void savePage_NullTitle() {
		String content = "Line 1\nLine 2\n";
		boolean editExistingPage = true;
		String message = "";
		String title = null;
		Payload receivedPayload = new Payload(
				null,
				content,
				editExistingPage,
				null,
				message,
				null,
				null,
				title,
				null
		);

		Payload expectedPayload = new Payload(
				null,
				content,
				editExistingPage,
				null,
				"PLEASE DEFINE A TITLE.",
				null,
				null,
				"",
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.savePage("source", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void savePage_BlankTitle() {
		String content = "Line 1\nLine 2\n";
		boolean editExistingPage = true;
		String message = "";
		String title = "  ";
		Payload receivedPayload = new Payload(
				null,
				content,
				editExistingPage,
				null,
				message,
				null,
				null,
				title,
				null
		);

		Payload expectedPayload = new Payload(
				null,
				content,
				editExistingPage,
				null,
				"PLEASE DEFINE A TITLE.",
				null,
				null,
				"",
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.savePage("source", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void savePage_NullContentEditButNonExistent() {
		String content = null;
		boolean editExistingPage = true;
		String message = "";
		String title = "Title";
		Payload receivedPayload = new Payload(
				null,
				content,
				editExistingPage,
				null,
				message,
				null,
				null,
				title,
				null
		);

		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.empty());

		Payload expectedPayload = new Payload(
				null,
				"",
				editExistingPage,
				null,
				"THERE IS NO EXISTING PAGE WITH THIS TITLE.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.savePage("source", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void savePage_EditButNonExistent() {
		String content = "Line 1\nLine 2\n";
		boolean editExistingPage = true;
		String message = "";
		String title = "Title";
		Payload receivedPayload = new Payload(
				null,
				content,
				editExistingPage,
				null,
				message,
				null,
				null,
				title,
				null
		);

		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.empty());

		Payload expectedPayload = new Payload(
				null,
				content,
				editExistingPage,
				null,
				"THERE IS NO EXISTING PAGE WITH THIS TITLE.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.savePage("source", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void savePage_EditExistent() {
		String content = "Line 1\nLine 2\n";
		boolean editExistingPage = true;
		String message = "";
		String title = "Title";
		Payload receivedPayload = new Payload(
				null,
				content,
				editExistingPage,
				null,
				message,
				null,
				null,
				title,
				null
		);

		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.of(new TitleEntity(2L, "Original title", "original_file_name", 3L, 4L)));
		when(txtRepository.save(new TxtEntity(content)))
				.thenReturn(new TxtEntity(13L, content));
		when(htmlRepository.save(new HtmlEntity(new ArrayList<>(), new ArrayList<>())))
				.thenReturn(new HtmlEntity(14L, new ArrayList<>(), new ArrayList<>()));
		when(titleRepository.save(new TitleEntity(title, "file_name", 13L, 14L)))
				.thenReturn(new TitleEntity(12L, title, "file_name", 13L, 14L));

		Payload expectedPayload = new Payload(
				null,
				content,
				editExistingPage,
				null,
				"SOURCE PAGE HAS BEEN OVERWRITTEN.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.savePage("source", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void savePage_NewButExistent() {
		String content = "Line 1\nLine 2\n";
		boolean editExistingPage = false;
		String message = "";
		String title = "Title";
		Payload receivedPayload = new Payload(
				null,
				content,
				editExistingPage,
				null,
				message,
				null,
				null,
				title,
				null
		);

		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.of(new TitleEntity(2L, "Original title", "original_file_name", 3L, 4L)));

		Payload expectedPayload = new Payload(
				null,
				content,
				editExistingPage,
				null,
				"THERE IS AN EXISTING PAGE WITH THIS TITLE.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.savePage("source", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void savePage_NewNonExistent() {
		String content = "Line 1\nLine 2\n";
		boolean editExistingPage = false;
		String message = "";
		String title = "Title";
		Payload receivedPayload = new Payload(
				null,
				content,
				editExistingPage,
				null,
				message,
				null,
				null,
				title,
				null
		);

		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.empty());
/*
		when(processRecords.listToString(content))
				.thenReturn(stringContent);
*/
		when(txtRepository.save(new TxtEntity(content)))
				.thenReturn(new TxtEntity(13L, content));
		when(htmlRepository.save(new HtmlEntity(new ArrayList<>(), new ArrayList<>())))
				.thenReturn(new HtmlEntity(14L, new ArrayList<>(), new ArrayList<>()));
		when(titleRepository.save(new TitleEntity(title, "file_name", 13L, 14L)))
				.thenReturn(new TitleEntity(12L, title, "file_name", 13L, 14L));

		Payload expectedPayload = new Payload(
				null,
				content,
				true,
				null,
				"SOURCE PAGE HAS BEEN SAVED.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.savePage("source", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void savePage_NullEditFlagAndExistent() {
		String content = "Line 1\nLine 2\n";
		Boolean editExistingPage = null;
		String message = "";
		String title = "Title";
		Payload receivedPayload = new Payload(
				null,
				content,
				editExistingPage,
				null,
				message,
				null,
				null,
				title,
				null
		);

		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.of(new TitleEntity(2L, "Original title", "original_file_name", 3L, 4L)));

		Payload expectedPayload = new Payload(
				null,
				content,
				false,
				null,
				"THERE IS AN EXISTING PAGE WITH THIS TITLE.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.savePage("source", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}
}