package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Process.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.test.web.*;
import org.springframework.web.servlet.*;

import java.util.*;

import static org.mockito.Mockito.*;

class CommonServiceRenameSourcePageTest {
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
	void renameSourcePage_NullPayload() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.renameSourcePage("management", null);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void renameSourcePage_NullTitles() {
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"",
				null,
				null,
				"Title 3",
				null
		);

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.renameSourcePage("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void renameSourcePage_MoreThanOneTitles() {
		List<String> receivedTitles = List.of("Title 1", "Title 2");
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"",
				null,
				null,
				"Title 3",
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
				"PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.renameSourcePage("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void renameSourcePage_FirstTitleIsNull() {
		List<String> receivedTitles = new ArrayList<>();
		receivedTitles.add(null);
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"",
				null,
				null,
				"Title 3",
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
				"PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.renameSourcePage("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void renameSourcePage_FirstTitleBlank() {
		List<String> receivedTitles = new ArrayList<>();
		receivedTitles.add("  ");
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"",
				null,
				null,
				"Title 3",
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
				"PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.renameSourcePage("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void renameSourcePage_NoSuchTitle() {
		List<String> receivedTitles = new ArrayList<>();
		receivedTitles.add("Title 3");
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"",
				null,
				null,
				"Title 4",
				receivedTitles
		);

		when(titleRepository.findByTitle("Title 3"))
				.thenReturn(Optional.empty());

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.renameSourcePage("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void renameSourcePage_NullNewTitle() {
		List<String> receivedTitles = new ArrayList<>();
		receivedTitles.add("Title 1");
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"",
				null,
				null,
				null,
				receivedTitles
		);

/*
		when(titleRepository.findByTitle(receivedTitles.get(0)))
				.thenReturn(Optional.of(new TitleEntity(3L, "Title 3", "title_3", 2L, 4L)));
*/

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.renameSourcePage("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void renameSourcePage_BlankNewTitle() {
		List<String> receivedTitles = new ArrayList<>();
		receivedTitles.add("Title 1");
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"",
				null,
				null,
				"  ",
				receivedTitles
		);

/*
		when(titleRepository.findByTitle(receivedTitles.get(0)))
				.thenReturn(Optional.of(new TitleEntity(3L, "Title 3", "title_3", 2L, 4L)));
*/

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.renameSourcePage("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void renameSourcePage_AlreadyExistingNewTitle() {
		List<String> receivedTitles = new ArrayList<>();
		receivedTitles.add("Title 1");
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"",
				null,
				null,
				"Title 2",
				receivedTitles
		);

		when(titleRepository.findByTitle(receivedTitles.get(0)))
				.thenReturn(Optional.of(new TitleEntity(3L, "Title 1", "title_1", 2L, 4L)));

		when(titleRepository.findByTitle("Title 2"))
				.thenReturn(Optional.of(new TitleEntity(3L, "Title 2", "title_2", 2L, 4L)));

		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"THE GIVEN NEW TITLE ALREADY EXISTS, PLEASE DEFINE AN OTHER ONE.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.renameSourcePage("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void renameSourcePage_AllValid() {
		List<String> receivedTitles = new ArrayList<>();
		receivedTitles.add("Title 2");
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"",
				null,
				null,
				"Title 3",
				receivedTitles
		);

		when(titleRepository.findByTitle(receivedTitles.get(0)))
				.thenReturn(Optional.of(new TitleEntity(3L, "Title 2", "title_2", 2L, 4L)));

		when(titleRepository.findByTitle("Title 3"))
				.thenReturn(Optional.empty());

		when(fileOperations.generateFilename("Title 3", titleRepository))
				.thenReturn("title_3");

		when(titleRepository.save(new TitleEntity("Title 3", "title_3", 2L, 4L)))
				.thenReturn(new TitleEntity(5L, "Title 3", "title_3", 2L, 4L));

		List<String> titles = List.of("Title 1", "Title 3");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PAGE HAS BEEN RENAMED.",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.renameSourcePage("management", receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}
}