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

		Map<String, Object> map = new HashMap<>();
		map.put("title", "");
		map.put("file_name", fileName);
		map.put("content", content);
		map.put("edit_existing_page", editExistingPage);
		map.put("message", "Please define a title.");

		ModelAndView modelAndView = commonService.savePage("source", null, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void savePage_BlankTitle() {
		String title = "  ";
		String fileName = "file_name";
		List<String> content = List.of("Line 1", "Line 2");
		boolean editExistingPage = true;

		Map<String, Object> map = new HashMap<>();
		map.put("title", "");
		map.put("file_name", fileName);
		map.put("content", content);
		map.put("edit_existing_page", editExistingPage);
		map.put("message", "Please define a title.");

		ModelAndView modelAndView = commonService.savePage("source", title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void savePage_NullFileName() {
		String title = "Title";
		List<String> content = List.of("Line 1", "Line 2");
		boolean editExistingPage = true;

		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("file_name", "");
		map.put("content", content);
		map.put("edit_existing_page", editExistingPage);
		map.put("message", "Please define a file name.");

		ModelAndView modelAndView = commonService.savePage("source", title, null, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void savePage_BlankFileName() {
		String title = "Title";
		String fileName = "  ";
		List<String> content = List.of("Line 1", "Line 2");
		boolean editExistingPage = true;

		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("file_name", "");
		map.put("content", content);
		map.put("edit_existing_page", editExistingPage);
		map.put("message", "Please define a file name.");

		ModelAndView modelAndView = commonService.savePage("source", title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void savePage_NullContentEditButNonExistent() {
		String title = "Title";
		String fileName = "file_name";
		boolean editExistingPage = true;

		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.empty());

		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("file_name", fileName);
		map.put("content", new ArrayList<>());
		map.put("edit_existing_page", editExistingPage);
		map.put("message", "There is no existing page with this title.");

		ModelAndView modelAndView = commonService.savePage("source", title, fileName, null, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void savePage_EditButNonExistent() {
		String title = "Title";
		String fileName = "file_name";
		List<String> content = List.of("Line 1", "Line 2");
		boolean editExistingPage = true;

		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.empty());

		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("file_name", fileName);
		map.put("content", content);
		map.put("edit_existing_page", editExistingPage);
		map.put("message", "There is no existing page with this title.");

		ModelAndView modelAndView = commonService.savePage("source", title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
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

		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("file_name", fileName);
		map.put("content", content);
		map.put("edit_existing_page", editExistingPage);
		map.put("message", "Source page has been overwritten.");

		ModelAndView modelAndView = commonService.savePage("source", title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void savePage_NewButExistent() {
		String title = "Title";
		String fileName = "file_name";
		List<String> content = List.of("Line 1", "Line 2");
		boolean editExistingPage = false;

		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.of(new TitleEntity(2L, "Original title", "original_file_name", 3L, 4L)));

		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("file_name", fileName);
		map.put("content", content);
		map.put("edit_existing_page", editExistingPage);
		map.put("message", "There is an existing page with this title.");

		ModelAndView modelAndView = commonService.savePage("source", title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
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

		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("file_name", fileName);
		map.put("content", content);
		map.put("edit_existing_page", true);
		map.put("message", "Source page has been saved.");

		ModelAndView modelAndView = commonService.savePage("source", title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void savePage_NullEditFlagAndExistent() {
		String title = "Title";
		String fileName = "file_name";
		List<String> content = List.of("Line 1", "Line 2");

		when(titleRepository.findByTitle(title))
				.thenReturn(Optional.of(new TitleEntity(2L, "Original title", "original_file_name", 3L, 4L)));

		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("file_name", fileName);
		map.put("content", content);
		map.put("edit_existing_page", false);
		map.put("message", "There is an existing page with this title.");

		ModelAndView modelAndView = commonService.savePage("source", title, fileName, content, null);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}
}