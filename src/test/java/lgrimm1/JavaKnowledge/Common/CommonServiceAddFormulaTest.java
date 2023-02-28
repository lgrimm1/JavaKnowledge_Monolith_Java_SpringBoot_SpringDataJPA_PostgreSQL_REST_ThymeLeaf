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

class CommonServiceAddFormulaTest {

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
	void addFormulaNullFormulaName() {
		String title = "Title";
		String fileName = "file_name";
		List<String> content = new ArrayList<>();
		content.add("Line 1");
		content.add("Line 2");
		Boolean editExistingPage = true;

		when(formulas.getFormula(null))
				.thenReturn("");

		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("file_name", fileName);
		map.put("content", content);
		map.put("edit_existing_page", true);
		map.put("message", "Wrong formula name was asked.");

		ModelAndView modelAndView = commonService.addFormula("source", null, title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void addFormulaBlankFormulaName() {
		String formulaName = "  ";
		String title = "Title";
		String fileName = "file_name";
		List<String> content = new ArrayList<>();
		content.add("Line 1");
		content.add("Line 2");
		Boolean editExistingPage = true;

		when(formulas.getFormula(formulaName))
				.thenReturn("");

		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("file_name", fileName);
		map.put("content", content);
		map.put("edit_existing_page", true);
		map.put("message", "Wrong formula name was asked.");

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void addFormulaInvalidFormulaName() {
		String formulaName = "formula name";
		String title = "Title";
		String fileName = "file_name";
		List<String> content = new ArrayList<>();
		content.add("Line 1");
		content.add("Line 2");
		Boolean editExistingPage = true;

		when(formulas.getFormula(formulaName))
				.thenReturn("");

		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("file_name", fileName);
		map.put("content", content);
		map.put("edit_existing_page", true);
		map.put("message", "Wrong formula name was asked.");

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void addFormulaNullFileName() {
		String formulaName = "formula name";
		String title = "Title";
		List<String> content = new ArrayList<>();
		content.add("Line 1");
		content.add("Line 2");
		Boolean editExistingPage = true;

		String formula = "Formula line 1\nFormula line 2";
		when(formulas.getFormula(formulaName))
				.thenReturn(formula);

		List<String> newContent = new ArrayList<>(content);
		newContent.add("Formula line 1");
		newContent.add("Formula line 2");

		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("file_name", "");
		map.put("content", newContent);
		map.put("edit_existing_page", true);
		map.put("message", "Formula was appended.");

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, title, null, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void addFormulaNullTitle() {
		String formulaName = "formula name";
		String fileName = "file_name";
		List<String> content = new ArrayList<>();
		content.add("Line 1");
		content.add("Line 2");
		Boolean editExistingPage = true;

		String formula = "Formula line 1\nFormula line 2";
		when(formulas.getFormula(formulaName))
				.thenReturn(formula);

		List<String> newContent = new ArrayList<>(content);
		newContent.add("Formula line 1");
		newContent.add("Formula line 2");

		Map<String, Object> map = new HashMap<>();
		map.put("title", "");
		map.put("file_name", fileName);
		map.put("content", newContent);
		map.put("edit_existing_page", true);
		map.put("message", "Formula was appended.");

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, null, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void addFormulaBlankTitle() {
		String formulaName = "formula name";
		String title = "  ";
		String fileName = "file_name";
		List<String> content = new ArrayList<>();
		content.add("Line 1");
		content.add("Line 2");
		Boolean editExistingPage = true;

		String formula = "Formula line 1\nFormula line 2";
		when(formulas.getFormula(formulaName))
				.thenReturn(formula);

		List<String> newContent = new ArrayList<>(content);
		newContent.add("Formula line 1");
		newContent.add("Formula line 2");

		Map<String, Object> map = new HashMap<>();
		map.put("title", "");
		map.put("file_name", fileName);
		map.put("content", newContent);
		map.put("edit_existing_page", true);
		map.put("message", "Formula was appended.");

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void addFormulaBlankFileName() {
		String formulaName = "formula name";
		String title = "Title";
		String fileName = "  ";
		List<String> content = new ArrayList<>();
		content.add("Line 1");
		content.add("Line 2");
		Boolean editExistingPage = true;

		String formula = "Formula line 1\nFormula line 2";
		when(formulas.getFormula(formulaName))
				.thenReturn(formula);

		List<String> newContent = new ArrayList<>(content);
		newContent.add("Formula line 1");
		newContent.add("Formula line 2");

		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("file_name", "");
		map.put("content", newContent);
		map.put("edit_existing_page", true);
		map.put("message", "Formula was appended.");

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void addFormulaNullContent() {
		String formulaName = "formula name";
		String title = "Title";
		String fileName = "file_name";
		Boolean editExistingPage = true;

		String formula = "Formula line 1\nFormula line 2";
		when(formulas.getFormula(formulaName))
				.thenReturn(formula);

		List<String> newContent = new ArrayList<>();
		newContent.add("Formula line 1");
		newContent.add("Formula line 2");

		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("file_name", fileName);
		map.put("content", newContent);
		map.put("edit_existing_page", true);
		map.put("message", "Formula was appended.");

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, title, fileName, null, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void addFormulaNullEditExistingPage() {
		String formulaName = "formula name";
		String title = "Title";
		String fileName = "file_name";
		List<String> content = new ArrayList<>();
		content.add("Line 1");
		content.add("Line 2");

		String formula = "Formula line 1\nFormula line 2";
		when(formulas.getFormula(formulaName))
				.thenReturn(formula);

		List<String> newContent = new ArrayList<>(content);
		newContent.add("Formula line 1");
		newContent.add("Formula line 2");

		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("file_name", fileName);
		map.put("content", newContent);
		map.put("edit_existing_page", false);
		map.put("message", "Formula was appended.");

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, title, fileName, content, null);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void addFormulaAllValidValues() {
		String formulaName = "formula name";
		String title = "Title";
		String fileName = "file_name";
		List<String> content = new ArrayList<>();
		content.add("Line 1");
		content.add("Line 2");
		Boolean editExistingPage = true;

		String formula = "Formula line 1\nFormula line 2";
		when(formulas.getFormula(formulaName))
				.thenReturn(formula);

		List<String> newContent = new ArrayList<>(content);
		newContent.add("Formula line 1");
		newContent.add("Formula line 2");

		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("file_name", fileName);
		map.put("content", newContent);
		map.put("edit_existing_page", true);
		map.put("message", "Formula was appended.");

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}
}
