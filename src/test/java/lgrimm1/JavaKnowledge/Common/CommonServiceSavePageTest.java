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
	void savePage_NullTitle() {
		String fileName = "file_name";
		List<String> content = List.of("Line 1", "Line 2");
		boolean editExistingPage = true;

		Payload expectedPayload = new Payload(
				null,
				content,
				editExistingPage,
				fileName,
				null,
				"PLEASE DEFINE A TITLE.",
				null,
				null,
				"",
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.savePage("source", null, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void savePage_BlankTitle() {
		String title = "  ";
		String fileName = "file_name";
		List<String> content = List.of("Line 1", "Line 2");
		boolean editExistingPage = true;

		Payload expectedPayload = new Payload(
				null,
				content,
				editExistingPage,
				fileName,
				null,
				"PLEASE DEFINE A TITLE.",
				null,
				null,
				"",
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.savePage("source", title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void savePage_NullFileName() {
		String title = "Title";
		List<String> content = List.of("Line 1", "Line 2");
		boolean editExistingPage = true;

		Payload expectedPayload = new Payload(
				null,
				content,
				editExistingPage,
				"",
				null,
				"PLEASE DEFINE A FILE NAME.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.savePage("source", title, null, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void savePage_BlankFileName() {
		String title = "Title";
		String fileName = "  ";
		List<String> content = List.of("Line 1", "Line 2");
		boolean editExistingPage = true;

		Payload expectedPayload = new Payload(
				null,
				content,
				editExistingPage,
				"",
				null,
				"PLEASE DEFINE A FILE NAME.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.savePage("source", title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void savePage_NullContentEditButNonExistent() {
		String title = "Title";
		String fileName = "file_name";
		boolean editExistingPage = true;

		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.empty());

		Payload expectedPayload = new Payload(
				null,
				new ArrayList<>(),
				editExistingPage,
				fileName,
				null,
				"THERE IS NO EXISTING PAGE WITH THIS TITLE.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.savePage("source", title, fileName, null, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void savePage_EditButNonExistent() {
		String title = "Title";
		String fileName = "file_name";
		List<String> content = List.of("Line 1", "Line 2");
		boolean editExistingPage = true;

		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.empty());

		Payload expectedPayload = new Payload(
				null,
				content,
				editExistingPage,
				fileName,
				null,
				"THERE IS NO EXISTING PAGE WITH THIS TITLE.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.savePage("source", title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void savePage_EditExistent() {
		String title = "Title";
		String fileName = "file_name";
		List<String> content = List.of("Line 1", "Line 2");
		String stringContent = "Line 1\nLine 2\n";
		boolean editExistingPage = true;

		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.of(new TitleEntity(2L, "Original title", "original_file_name", 3L, 4L)));
		when(processRecords.listToString(content))
				.thenReturn(stringContent);
		when(txtRepository.save(new TxtEntity(stringContent)))
				.thenReturn(new TxtEntity(13L, stringContent));
		when(htmlRepository.save(new HtmlEntity(new ArrayList<>(), new ArrayList<>())))
				.thenReturn(new HtmlEntity(14L, new ArrayList<>(), new ArrayList<>()));
		when(titleRepository.save(new TitleEntity(title, fileName, 13L, 14L)))
				.thenReturn(new TitleEntity(12L, title, fileName, 13L, 14L));

		Payload expectedPayload = new Payload(
				null,
				content,
				editExistingPage,
				fileName,
				null,
				"SOURCE PAGE HAS BEEN OVERWRITTEN.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.savePage("source", title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void savePage_NewButExistent() {
		String title = "Title";
		String fileName = "file_name";
		List<String> content = List.of("Line 1", "Line 2");
		boolean editExistingPage = false;

		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.of(new TitleEntity(2L, "Original title", "original_file_name", 3L, 4L)));

		Payload expectedPayload = new Payload(
				null,
				content,
				editExistingPage,
				fileName,
				null,
				"THERE IS AN EXISTING PAGE WITH THIS TITLE.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.savePage("source", title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void savePage_NewNonExistent() {
		String title = "Title";
		String fileName = "file_name";
		List<String> content = List.of("Line 1", "Line 2");
		String stringContent = "Line 1\nLine 2\n";
		boolean editExistingPage = false;

		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.empty());
		when(processRecords.listToString(content))
				.thenReturn(stringContent);
		when(txtRepository.save(new TxtEntity(stringContent)))
				.thenReturn(new TxtEntity(13L, stringContent));
		when(htmlRepository.save(new HtmlEntity(new ArrayList<>(), new ArrayList<>())))
				.thenReturn(new HtmlEntity(14L, new ArrayList<>(), new ArrayList<>()));
		when(titleRepository.save(new TitleEntity(title, fileName, 13L, 14L)))
				.thenReturn(new TitleEntity(12L, title, fileName, 13L, 14L));

		Payload expectedPayload = new Payload(
				null,
				content,
				true,
				fileName,
				null,
				"SOURCE PAGE HAS BEEN SAVED.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.savePage("source", title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void savePage_NullEditFlagAndExistent() {
		String title = "Title";
		String fileName = "file_name";
		List<String> content = List.of("Line 1", "Line 2");

		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.of(new TitleEntity(2L, "Original title", "original_file_name", 3L, 4L)));

		Payload expectedPayload = new Payload(
				null,
				content,
				false,
				fileName,
				null,
				"THERE IS AN EXISTING PAGE WITH THIS TITLE.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.savePage("source", title, fileName, content, null);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}
}