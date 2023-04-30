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
		when(formulas.getTitleSource())
				.thenReturn("SOURCETITLE");
	}

	@Test
	void addFormula_NullFormulaName() {
		String title = "Title";
		String originalContent = "Line1\nLine2\n";
		Boolean editExistingPage = true;
		String message = "";
		Payload receivedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				originalContent,
				editExistingPage,
				message,
				null,
				null,
				title,
				null
		);

		when(formulas.getFormula(null))
				.thenReturn(new ArrayList<>());

		List<String> originalContentList = new ArrayList<>();
		originalContentList.add("Line 1");
		originalContentList.add("Line 2");
		when(processRecords.stringToList(originalContent))
				.thenReturn(originalContentList);

		when(processRecords.listToString(originalContentList))
				.thenReturn(originalContent);

		Payload expectedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				originalContent,
				true,
				"WRONG FORMULA NAME WAS ASKED.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.addFormula("source", null, receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void addFormula_BlankFormulaName() {
		String formulaName = "  ";
		String title = "Title";
		String originalContent = "Line1\nLine2\n";
		Boolean editExistingPage = true;
		String message = "";
		Payload receivedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				originalContent,
				editExistingPage,
				message,
				null,
				null,
				title,
				null
		);

		when(formulas.getFormula(formulaName))
				.thenReturn(new ArrayList<>());

		List<String> originalContentList = new ArrayList<>();
		originalContentList.add("Line 1");
		originalContentList.add("Line 2");
		when(processRecords.stringToList(originalContent))
				.thenReturn(originalContentList);

		when(processRecords.listToString(originalContentList))
				.thenReturn(originalContent);

		Payload expectedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				originalContent,
				true,
				"WRONG FORMULA NAME WAS ASKED.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void addFormula_InvalidFormulaName() {
		String formulaName = "formula name";
		String title = "Title";
		String originalContent = "Line1\nLine2\n";
		Boolean editExistingPage = true;
		String message = "";
		Payload receivedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				originalContent,
				editExistingPage,
				message,
				null,
				null,
				title,
				null
		);

		when(formulas.getFormula(formulaName))
				.thenReturn(new ArrayList<>());

		List<String> originalContentList = new ArrayList<>();
		originalContentList.add("Line 1");
		originalContentList.add("Line 2");
		when(processRecords.stringToList(originalContent))
				.thenReturn(originalContentList);

		when(processRecords.listToString(originalContentList))
				.thenReturn(originalContent);

		Payload expectedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				originalContent,
				true,
				"WRONG FORMULA NAME WAS ASKED.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void addFormula_NullPayload() {
		String formulaName = "formula name";

		String title = "";
		String content = "";
		Boolean editExistingPage = false;
		Payload expectedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				content,
				editExistingPage,
				"THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, null);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void addFormula_NullTitle() {
		String formulaName = "formula name";
		String originalContent = "Line1\nLine2\n";
		Boolean editExistingPage = true;
		String message = "";
		Payload receivedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				originalContent,
				editExistingPage,
				message,
				null,
				null,
				null,
				null
		);

		List<String> formulaList = new ArrayList<>();
		formulaList.add("Formula line 1");
		formulaList.add("Formula line 2");
		String formula = "Formula line 1\nFormula line 2\n";
		when(formulas.getFormula(formulaName))
				.thenReturn(formulaList);

		List<String> originalContentList = new ArrayList<>();
		originalContentList.add("Line 1");
		originalContentList.add("Line 2");
		when(processRecords.stringToList(originalContent))
				.thenReturn(originalContentList);

		List<String> newContentList = new ArrayList<>(originalContentList);
		newContentList.addAll(formulaList);
		String newContent = originalContent + formula;
		when(processRecords.listToString(newContentList))
				.thenReturn(newContent);

		Payload expectedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				newContent,
				true,
				"FORMULA WAS APPENDED.",
				null,
				null,
				"",
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void addFormula_BlankTitle() {
		String formulaName = "formula name";
		String title = "  ";
		String originalContent = "Line1\nLine2\n";
		Boolean editExistingPage = true;
		String message = "";
		Payload receivedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				originalContent,
				editExistingPage,
				message,
				null,
				null,
				title,
				null
		);

		List<String> formulaList = new ArrayList<>();
		formulaList.add("Formula line 1");
		formulaList.add("Formula line 2");
		String formula = "Formula line 1\nFormula line 2\n";
		when(formulas.getFormula(formulaName))
				.thenReturn(formulaList);

		List<String> originalContentList = new ArrayList<>();
		originalContentList.add("Line 1");
		originalContentList.add("Line 2");
		when(processRecords.stringToList(originalContent))
				.thenReturn(originalContentList);

		List<String> newContentList = new ArrayList<>(originalContentList);
		newContentList.addAll(formulaList);
		String newContent = originalContent + formula;
		when(processRecords.listToString(newContentList))
				.thenReturn(newContent);

		Payload expectedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				newContent,
				true,
				"FORMULA WAS APPENDED.",
				null,
				null,
				"",
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void addFormula_NullContent() {
		String formulaName = "formula name";
		String title = "Title";
		Boolean editExistingPage = true;
		String message = "";
		Payload receivedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				null,
				editExistingPage,
				message,
				null,
				null,
				title,
				null
		);

		List<String> formulaList = new ArrayList<>();
		formulaList.add("Formula line 1");
		formulaList.add("Formula line 2");
		String formula = "Formula line 1\nFormula line 2\n";
		when(formulas.getFormula(formulaName))
				.thenReturn(formulaList);

		List<String> originalContentList = new ArrayList<>();
		when(processRecords.stringToList(null))
				.thenReturn(originalContentList);

		List<String> newContentList = new ArrayList<>(originalContentList);
		newContentList.addAll(formulaList);
		when(processRecords.listToString(newContentList))
				.thenReturn(formula);

		Payload expectedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				formula,
				true,
				"FORMULA WAS APPENDED.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void addFormula_NullEditExistingPage() {
		String formulaName = "formula name";
		String title = "Title";
		String originalContent = "Line1\nLine2\n";
		Boolean editExistingPage = null;
		String message = "";
		Payload receivedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				originalContent,
				editExistingPage,
				message,
				null,
				null,
				title,
				null
		);

		List<String> formulaList = new ArrayList<>();
		formulaList.add("Formula line 1");
		formulaList.add("Formula line 2");
		String formula = "Formula line 1\nFormula line 2\n";
		when(formulas.getFormula(formulaName))
				.thenReturn(formulaList);

		List<String> originalContentList = new ArrayList<>();
		originalContentList.add("Line 1");
		originalContentList.add("Line 2");
		when(processRecords.stringToList(originalContent))
				.thenReturn(originalContentList);

		List<String> newContentList = new ArrayList<>(originalContentList);
		newContentList.addAll(formulaList);
		String newContent = originalContent + formula;
		when(processRecords.listToString(newContentList))
				.thenReturn(newContent);

		Payload expectedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				newContent,
				false,
				"FORMULA WAS APPENDED.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void addFormula_AllValidValues() {
		String formulaName = "formula name";
		String title = "Title";
		String originalContent = "Line1\nLine2\n";
		Boolean editExistingPage = true;
		String message = "";
		Payload receivedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				originalContent,
				editExistingPage,
				message,
				null,
				null,
				title,
				null
		);

		List<String> formulaList = new ArrayList<>();
		formulaList.add("Formula line 1");
		formulaList.add("Formula line 2");
		String formula = "Formula line 1\nFormula line 2\n";
		when(formulas.getFormula(formulaName))
				.thenReturn(formulaList);

		List<String> originalContentList = new ArrayList<>();
		originalContentList.add("Line 1");
		originalContentList.add("Line 2");
		when(processRecords.stringToList(originalContent))
				.thenReturn(originalContentList);

		List<String> newContentList = new ArrayList<>(originalContentList);
		newContentList.addAll(formulaList);
		String newContent = originalContent + formula;
		when(processRecords.listToString(newContentList))
				.thenReturn(newContent);

		Payload expectedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				newContent,
				true,
				"FORMULA WAS APPENDED.",
				null,
				null,
				title,
				null
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.addFormula("source", formulaName, receivedPayload);

		ModelAndViewAssert.assertViewName(modelAndView, "source");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}
}
