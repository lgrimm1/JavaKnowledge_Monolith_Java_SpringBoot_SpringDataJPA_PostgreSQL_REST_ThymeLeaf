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

class CommonServiceTest {

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
		when(formulas.getTitleRoot())
				.thenReturn("ROOTTITLE");
		when(formulas.getTitleManagement())
				.thenReturn("MANAGEMENTTITLE");
		when(formulas.getTitleSource())
				.thenReturn("SOURCETITLE");
	}

	@Test
	void getRoot() {
		Payload expectedPayload = new Payload(
				formulas.getTitleRoot(),
				null,
				null,
				null,
				null,
				"",
				null,
				null,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.getRoot("root");
		ModelAndViewAssert.assertViewName(modelAndView, "root");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void managePages() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"",
				null,
				null,
				"",
				titles
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.managePages("management");

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void createSourcePage() {
		Payload expectedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				"",
				false,
				"",
				null,
				null,
				"",
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.createSourcePage("source");

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}
}