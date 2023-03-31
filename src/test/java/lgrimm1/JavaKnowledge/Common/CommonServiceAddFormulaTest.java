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
	void addFormula_NullFormulaName() {
		String title = "Title";
		String fileName = "file_name";
		List<String> content = new ArrayList<>();
		content.add("Line 1");
		content.add("Line 2");
		Boolean editExistingPage = true;

		when(formulas.getFormula(null))
				.thenReturn("");

		Payload expectedPayload = new Payload(
				null,
				content,
				true,
				fileName,
				null,
				"Wrong formula name was asked.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.addFormula("source", null, title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void addFormula_BlankFormulaName() {
		String formulaName = "  ";
		String title = "Title";
		String fileName = "file_name";
		List<String> content = new ArrayList<>();
		content.add("Line 1");
		content.add("Line 2");
		Boolean editExistingPage = true;

		when(formulas.getFormula(formulaName))
				.thenReturn("");

		Payload expectedPayload = new Payload(
				null,
				content,
				true,
				fileName,
				null,
				"Wrong formula name was asked.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void addFormula_InvalidFormulaName() {
		String formulaName = "formula name";
		String title = "Title";
		String fileName = "file_name";
		List<String> content = new ArrayList<>();
		content.add("Line 1");
		content.add("Line 2");
		Boolean editExistingPage = true;

		when(formulas.getFormula(formulaName))
				.thenReturn("");

		Payload expectedPayload = new Payload(
				null,
				content,
				true,
				fileName,
				null,
				"Wrong formula name was asked.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void addFormula_NullFileName() {
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

		Payload expectedPayload = new Payload(
				null,
				newContent,
				true,
				"",
				null,
				"Formula was appended.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, title, null, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void addFormula_NullTitle() {
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

		Payload expectedPayload = new Payload(
				null,
				newContent,
				true,
				fileName,
				null,
				"Formula was appended.",
				null,
				null,
				"",
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, null, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void addFormula_BlankTitle() {
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

		Payload expectedPayload = new Payload(
				null,
				newContent,
				true,
				fileName,
				null,
				"Formula was appended.",
				null,
				null,
				"",
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void addFormula_BlankFileName() {
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

		Payload expectedPayload = new Payload(
				null,
				newContent,
				true,
				"",
				null,
				"Formula was appended.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void addFormula_NullContent() {
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

		Payload expectedPayload = new Payload(
				null,
				newContent,
				true,
				fileName,
				null,
				"Formula was appended.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, title, fileName, null, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void addFormula_NullEditExistingPage() {
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

		Payload expectedPayload = new Payload(
				null,
				newContent,
				false,
				fileName,
				null,
				"Formula was appended.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, title, fileName, content, null);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void addFormula_AllValidValues() {
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

		Payload expectedPayload = new Payload(
				null,
				newContent,
				true,
				fileName,
				null,
				"Formula was appended.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, title, fileName, content, editExistingPage);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}
}
