package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Process.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
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
	void savePageNullTitle() {
		String fileName = "file_name";
		List<String> content = List.of(
				"Line 1",
				"Line 2"
		);
		boolean editExistingPage = true;
		ModelAndView result = commonService.savePage(null, "file_name", content, editExistingPage, new ModelAndView("source", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("source", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(5, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("title"));
		Assertions.assertTrue(modelMap.getAttribute("title") instanceof String);
		Assertions.assertEquals("", modelMap.getAttribute("title"));

		Assertions.assertTrue(modelMap.containsAttribute("file_name"));
		Assertions.assertTrue(modelMap.getAttribute("file_name") instanceof String);
		Assertions.assertEquals(fileName, modelMap.getAttribute("file_name"));

		Assertions.assertTrue(modelMap.containsAttribute("content"));
		Assertions.assertTrue(modelMap.getAttribute("content") instanceof List);
		Assertions.assertEquals(content, modelMap.getAttribute("content"));

		Assertions.assertTrue(modelMap.containsAttribute("edit_existing_page"));
		Assertions.assertTrue(modelMap.getAttribute("edit_existing_page") instanceof Boolean);
		Assertions.assertEquals(editExistingPage, modelMap.getAttribute("edit_existing_page"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("Please define a title.", modelMap.getAttribute("message"));

	}

	@Test
	void savePageBlankTitle() {
		String title = "  ";
		String fileName = "file_name";
		List<String> content = List.of(
				"Line 1",
				"Line 2"
		);
		boolean editExistingPage = true;
		ModelAndView result = commonService.savePage(title, "file_name", content, editExistingPage, new ModelAndView("source", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("source", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(5, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("title"));
		Assertions.assertTrue(modelMap.getAttribute("title") instanceof String);
		Assertions.assertEquals("", modelMap.getAttribute("title"));

		Assertions.assertTrue(modelMap.containsAttribute("file_name"));
		Assertions.assertTrue(modelMap.getAttribute("file_name") instanceof String);
		Assertions.assertEquals(fileName, modelMap.getAttribute("file_name"));

		Assertions.assertTrue(modelMap.containsAttribute("content"));
		Assertions.assertTrue(modelMap.getAttribute("content") instanceof List);
		Assertions.assertEquals(content, modelMap.getAttribute("content"));

		Assertions.assertTrue(modelMap.containsAttribute("edit_existing_page"));
		Assertions.assertTrue(modelMap.getAttribute("edit_existing_page") instanceof Boolean);
		Assertions.assertEquals(editExistingPage, modelMap.getAttribute("edit_existing_page"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("Please define a title.", modelMap.getAttribute("message"));
	}

	@Test
	void savePageNullFileName() {
		String title = "Title";
		List<String> content = List.of(
				"Line 1",
				"Line 2"
		);
		boolean editExistingPage = true;
		ModelAndView result = commonService.savePage(title, null, content, editExistingPage, new ModelAndView("source", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("source", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(5, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("title"));
		Assertions.assertTrue(modelMap.getAttribute("title") instanceof String);
		Assertions.assertEquals(title, modelMap.getAttribute("title"));

		Assertions.assertTrue(modelMap.containsAttribute("file_name"));
		Assertions.assertTrue(modelMap.getAttribute("file_name") instanceof String);
		Assertions.assertEquals("", modelMap.getAttribute("file_name"));

		Assertions.assertTrue(modelMap.containsAttribute("content"));
		Assertions.assertTrue(modelMap.getAttribute("content") instanceof List);
		Assertions.assertEquals(content, modelMap.getAttribute("content"));

		Assertions.assertTrue(modelMap.containsAttribute("edit_existing_page"));
		Assertions.assertTrue(modelMap.getAttribute("edit_existing_page") instanceof Boolean);
		Assertions.assertEquals(editExistingPage, modelMap.getAttribute("edit_existing_page"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("Please define a file name.", modelMap.getAttribute("message"));
	}

	@Test
	void savePageBlankFileName() {
		String title = "Title";
		String fileName = "  ";
		List<String> content = List.of(
				"Line 1",
				"Line 2"
		);
		boolean editExistingPage = true;
		ModelAndView result = commonService.savePage(title, fileName, content, editExistingPage, new ModelAndView("source", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("source", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(5, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("title"));
		Assertions.assertTrue(modelMap.getAttribute("title") instanceof String);
		Assertions.assertEquals(title, modelMap.getAttribute("title"));

		Assertions.assertTrue(modelMap.containsAttribute("file_name"));
		Assertions.assertTrue(modelMap.getAttribute("file_name") instanceof String);
		Assertions.assertEquals("", modelMap.getAttribute("file_name"));

		Assertions.assertTrue(modelMap.containsAttribute("content"));
		Assertions.assertTrue(modelMap.getAttribute("content") instanceof List);
		Assertions.assertEquals(content, modelMap.getAttribute("content"));

		Assertions.assertTrue(modelMap.containsAttribute("edit_existing_page"));
		Assertions.assertTrue(modelMap.getAttribute("edit_existing_page") instanceof Boolean);
		Assertions.assertEquals(editExistingPage, modelMap.getAttribute("edit_existing_page"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("Please define a file name.", modelMap.getAttribute("message"));
	}

	@Test
	void savePageNullContentEditButNonExistent() {
		String title = "Title";
		String fileName = "title";
		boolean editExistingPage = true;
		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.empty());
		ModelAndView result = commonService.savePage(title, fileName, null, editExistingPage, new ModelAndView("source", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("source", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(5, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("title"));
		Assertions.assertTrue(modelMap.getAttribute("title") instanceof String);
		Assertions.assertEquals(title, modelMap.getAttribute("title"));

		Assertions.assertTrue(modelMap.containsAttribute("file_name"));
		Assertions.assertTrue(modelMap.getAttribute("file_name") instanceof String);
		Assertions.assertEquals(fileName, modelMap.getAttribute("file_name"));

		Assertions.assertTrue(modelMap.containsAttribute("content"));
		Assertions.assertTrue(modelMap.getAttribute("content") instanceof List);
		Assertions.assertEquals(new ArrayList<>(), modelMap.getAttribute("content"));

		Assertions.assertTrue(modelMap.containsAttribute("edit_existing_page"));
		Assertions.assertTrue(modelMap.getAttribute("edit_existing_page") instanceof Boolean);
		Assertions.assertEquals(editExistingPage, modelMap.getAttribute("edit_existing_page"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("There is no existing page with this title.", modelMap.getAttribute("message"));
	}

	@Test
	void savePageEditButNonExistent() {
		String title = "Title";
		String fileName = "title";
		List<String> content = List.of(
				"Line 1",
				"Line 2"
		);
		boolean editExistingPage = true;
		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.empty());
		ModelAndView result = commonService.savePage(title, fileName, content, editExistingPage, new ModelAndView("source", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("source", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(5, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("title"));
		Assertions.assertTrue(modelMap.getAttribute("title") instanceof String);
		Assertions.assertEquals(title, modelMap.getAttribute("title"));

		Assertions.assertTrue(modelMap.containsAttribute("file_name"));
		Assertions.assertTrue(modelMap.getAttribute("file_name") instanceof String);
		Assertions.assertEquals(fileName, modelMap.getAttribute("file_name"));

		Assertions.assertTrue(modelMap.containsAttribute("content"));
		Assertions.assertTrue(modelMap.getAttribute("content") instanceof List);
		Assertions.assertEquals(content, modelMap.getAttribute("content"));

		Assertions.assertTrue(modelMap.containsAttribute("edit_existing_page"));
		Assertions.assertTrue(modelMap.getAttribute("edit_existing_page") instanceof Boolean);
		Assertions.assertEquals(editExistingPage, modelMap.getAttribute("edit_existing_page"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("There is no existing page with this title.", modelMap.getAttribute("message"));
	}

	@Test
	void savePageEditExistent() {
		String title = "Title";
		String fileName = "title";
		List<String> content = List.of(
				"Line 1",
				"Line 2"
		);
		boolean editExistingPage = true;
		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.of(new TitleEntity(2L, "Original title", "original_file_name", 3L, 4L)));
		when(txtRepository.save(new TxtEntity(content)))
				.thenReturn(new TxtEntity(13L, content));
		when(htmlRepository.save(new HtmlEntity(new ArrayList<>())))
				.thenReturn(new HtmlEntity(14L, new ArrayList<>()));
		when(titleRepository.save(new TitleEntity(title, fileName, 13L, 14L)))
				.thenReturn(new TitleEntity(12L, title, fileName, 13L, 14L));
		ModelAndView result = commonService.savePage(title, fileName, content, editExistingPage, new ModelAndView("source", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("source", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(5, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("title"));
		Assertions.assertTrue(modelMap.getAttribute("title") instanceof String);
		Assertions.assertEquals(title, modelMap.getAttribute("title"));

		Assertions.assertTrue(modelMap.containsAttribute("file_name"));
		Assertions.assertTrue(modelMap.getAttribute("file_name") instanceof String);
		Assertions.assertEquals(fileName, modelMap.getAttribute("file_name"));

		Assertions.assertTrue(modelMap.containsAttribute("content"));
		Assertions.assertTrue(modelMap.getAttribute("content") instanceof List);
		Assertions.assertEquals(content, modelMap.getAttribute("content"));

		Assertions.assertTrue(modelMap.containsAttribute("edit_existing_page"));
		Assertions.assertTrue(modelMap.getAttribute("edit_existing_page") instanceof Boolean);
		Assertions.assertEquals(editExistingPage, modelMap.getAttribute("edit_existing_page"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("Source page has been overwritten.", modelMap.getAttribute("message"));
	}

	@Test
	void savePageNewButExistent() {
		String title = "Title";
		String fileName = "title";
		List<String> content = List.of(
				"Line 1",
				"Line 2"
		);
		boolean editExistingPage = false;
		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.of(new TitleEntity(2L, "Original title", "original_file_name", 3L, 4L)));
		ModelAndView result = commonService.savePage(title, fileName, content, editExistingPage, new ModelAndView("source", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("source", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(5, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("title"));
		Assertions.assertTrue(modelMap.getAttribute("title") instanceof String);
		Assertions.assertEquals(title, modelMap.getAttribute("title"));

		Assertions.assertTrue(modelMap.containsAttribute("file_name"));
		Assertions.assertTrue(modelMap.getAttribute("file_name") instanceof String);
		Assertions.assertEquals(fileName, modelMap.getAttribute("file_name"));

		Assertions.assertTrue(modelMap.containsAttribute("content"));
		Assertions.assertTrue(modelMap.getAttribute("content") instanceof List);
		Assertions.assertEquals(content, modelMap.getAttribute("content"));

		Assertions.assertTrue(modelMap.containsAttribute("edit_existing_page"));
		Assertions.assertTrue(modelMap.getAttribute("edit_existing_page") instanceof Boolean);
		Assertions.assertEquals(editExistingPage, modelMap.getAttribute("edit_existing_page"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("There is an existing page with this title.", modelMap.getAttribute("message"));
	}

	@Test
	void savePageNewNonExistent() {
		String title = "Title";
		String fileName = "title";
		List<String> content = List.of(
				"Line 1",
				"Line 2"
		);
		boolean editExistingPage = false;
		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.empty());
		when(txtRepository.save(new TxtEntity(content)))
				.thenReturn(new TxtEntity(13L, content));
		when(htmlRepository.save(new HtmlEntity(new ArrayList<>())))
				.thenReturn(new HtmlEntity(14L, new ArrayList<>()));
		when(titleRepository.save(new TitleEntity(title, fileName, 13L, 14L)))
				.thenReturn(new TitleEntity(12L, title, fileName, 13L, 14L));
		ModelAndView result = commonService.savePage(title, fileName, content, editExistingPage, new ModelAndView("source", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("source", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(5, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("title"));
		Assertions.assertTrue(modelMap.getAttribute("title") instanceof String);
		Assertions.assertEquals(title, modelMap.getAttribute("title"));

		Assertions.assertTrue(modelMap.containsAttribute("file_name"));
		Assertions.assertTrue(modelMap.getAttribute("file_name") instanceof String);
		Assertions.assertEquals(fileName, modelMap.getAttribute("file_name"));

		Assertions.assertTrue(modelMap.containsAttribute("content"));
		Assertions.assertTrue(modelMap.getAttribute("content") instanceof List);
		Assertions.assertEquals(content, modelMap.getAttribute("content"));

		Assertions.assertTrue(modelMap.containsAttribute("edit_existing_page"));
		Assertions.assertTrue(modelMap.getAttribute("edit_existing_page") instanceof Boolean);
		Assertions.assertEquals(!editExistingPage, modelMap.getAttribute("edit_existing_page"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("Source page has been saved.", modelMap.getAttribute("message"));
	}

	@Test
	void savePageNullEditFlagAndExistent() {
		String title = "Title";
		String fileName = "title";
		List<String> content = List.of(
				"Line 1",
				"Line 2"
		);
		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.of(new TitleEntity(2L, "Original title", "original_file_name", 3L, 4L)));
		ModelAndView result = commonService.savePage(title, fileName, content, null, new ModelAndView("source", new HashMap<>()));

		Assertions.assertTrue(result.hasView());
		Assertions.assertEquals("source", result.getViewName());

		ModelMap modelMap = result.getModelMap();
		Assertions.assertEquals(5, modelMap.size());

		Assertions.assertTrue(modelMap.containsAttribute("title"));
		Assertions.assertTrue(modelMap.getAttribute("title") instanceof String);
		Assertions.assertEquals(title, modelMap.getAttribute("title"));

		Assertions.assertTrue(modelMap.containsAttribute("file_name"));
		Assertions.assertTrue(modelMap.getAttribute("file_name") instanceof String);
		Assertions.assertEquals(fileName, modelMap.getAttribute("file_name"));

		Assertions.assertTrue(modelMap.containsAttribute("content"));
		Assertions.assertTrue(modelMap.getAttribute("content") instanceof List);
		Assertions.assertEquals(content, modelMap.getAttribute("content"));

		Assertions.assertTrue(modelMap.containsAttribute("edit_existing_page"));
		Assertions.assertTrue(modelMap.getAttribute("edit_existing_page") instanceof Boolean);
		Assertions.assertEquals(false, modelMap.getAttribute("edit_existing_page"));

		Assertions.assertTrue(modelMap.containsAttribute("message"));
		Assertions.assertTrue(modelMap.getAttribute("message") instanceof String);
		Assertions.assertEquals("There is an existing page with this title.", modelMap.getAttribute("message"));
	}
}