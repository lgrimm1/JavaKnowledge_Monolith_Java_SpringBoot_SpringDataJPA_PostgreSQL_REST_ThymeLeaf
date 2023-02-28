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
	void deletePagesNullTitles() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", new ArrayList<>());
		map.put("confirm", false);
		map.put("message", "Please select titles you wish to delete.");

		ModelAndView modelAndView = commonService.deletePages("management", null, true);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void deletePagesNoTitles() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", new ArrayList<>());
		map.put("confirm", false);
		map.put("message", "Please select titles you wish to delete.");

		ModelAndView modelAndView = commonService.deletePages("management", new ArrayList<>(), true);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void deletePagesNotConfirmed() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", new ArrayList<>());
		map.put("confirm", false);
		map.put("message", "Please confirm deletion.");

		List<String> requestTitles = List.of("Title 3", "Title 4");
		ModelAndView modelAndView = commonService.deletePages("management", requestTitles, false);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void deletePagesWithNoValidTitles() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", new ArrayList<>());
		map.put("confirm", false);
		map.put("message", "Please select existing titles you wish to delete.");

		List<String> requestTitles = new ArrayList<>();
		requestTitles.add(null);
		requestTitles.add("  ");
		ModelAndView modelAndView = commonService.deletePages("management", requestTitles, true);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void deletePagesWithValidTitles() {
		List<String> restOfTitles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(restOfTitles);

		List<String> cleanedRequestTitles = List.of("Title 3", "Title 4");
		when(processRecords.deleteByTitles(cleanedRequestTitles, titleRepository, txtRepository, htmlRepository))
				.thenReturn(1L);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", restOfTitles);
		map.put("files", new ArrayList<>());
		map.put("confirm", false);
		map.put("message", "1 of 2 titles were deleted.");

		List<String> requestTitles = new ArrayList<>();
		requestTitles.add(null);
		requestTitles.add("  ");
		requestTitles.addAll(cleanedRequestTitles);
		ModelAndView modelAndView = commonService.deletePages("management", requestTitles, true);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}
}