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

class CommonServiceEditSourcePageTest {
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
	void editSourcePage_NullTitles() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				false,
				null,
				null,
				null,
				"",
				"Please select exactly one title for editing.",
				null,
				null,
				null,
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.editSourcePage("source", null);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void editSourcePage_MoreThanOneTitles() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				false,
				null,
				null,
				null,
				"",
				"Please select exactly one title for editing.",
				null,
				null,
				null,
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.editSourcePage("source", List.of("Title 1", "Title 2"));

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void editSourcePage_FirstTitleIsNull() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				false,
				null,
				null,
				null,
				"",
				"Please select exactly one title for editing.",
				null,
				null,
				null,
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		List<String> requestTitles = new ArrayList<>();
		requestTitles.add(null);
		ModelAndView modelAndView = commonService.editSourcePage("source", requestTitles);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void editSourcePage_FirstTitleBlank() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				false,
				null,
				null,
				null,
				"",
				"Please select exactly one title for editing.",
				null,
				null,
				null,
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		List<String> requestTitles = new ArrayList<>();
		requestTitles.add("  ");
		ModelAndView modelAndView = commonService.editSourcePage("source", requestTitles);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void editSourcePage_NoSuchTitle() {
		List<String> requestTitles = List.of("Title 3");
		when(titleRepository.findByTitle(requestTitles.get(0)))
				.thenReturn(Optional.empty());

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				false,
				null,
				null,
				null,
				"",
				"Please select exactly one title for editing.",
				null,
				null,
				null,
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.editSourcePage("source", requestTitles);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void editSourcePage_NoSuchTxt() {
		List<String> requestTitles = List.of("Title 3");
		when(titleRepository.findByTitle(requestTitles.get(0)))
				.thenReturn(Optional.of(new TitleEntity(3L, "Title 3", "title_3", 2L, 4L)));

		when(txtRepository.findById(2L))
				.thenReturn(Optional.empty());

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				false,
				null,
				null,
				null,
				"",
				"Please select exactly one title for editing.",
				null,
				null,
				null,
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.editSourcePage("source", requestTitles);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void editSourcePage_ExistingTitleAndTxt() {
		String requestTitle = "Title 3";
		String requestFilename = "title_3";
		List<String> requestTitles = List.of(requestTitle);
		when(titleRepository.findByTitle(requestTitles.get(0)))
				.thenReturn(Optional.of(new TitleEntity(3L, requestTitle, requestFilename, 2L, 4L)));

		String content = "Line 1\nLine 2\n";
		List<String> listContent = List.of("Line 1", "Line 2");
		when(txtRepository.findById(2L))
				.thenReturn(Optional.of(new TxtEntity(2L, content)));
		when(processRecords.stringToList(content))
				.thenReturn(listContent);

		Payload expectedPayload = new Payload(
				null,
				listContent,
				true,
				requestFilename,
				null,
				"",
				null,
				null,
				requestTitle,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.editSourcePage("source", requestTitles);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}
}