package lgrimm1.javaknowledge.services;

import lgrimm1.javaknowledge.databasestorage.*;
import lgrimm1.javaknowledge.datamodels.*;
import lgrimm1.javaknowledge.process.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;

class EditingServiceAddFormulaTest {

	DatabaseStorageService databaseStorageService;
	Formulas formulas;
	ProcessRecords processRecords;
	Extractors extractors;
	ProcessPage processPage;
	HtmlGenerators htmlGenerators;
	EditingService editingService;

	@BeforeEach
	void setUp() {
		databaseStorageService = Mockito.mock(DatabaseStorageService.class);
		formulas = Mockito.mock(Formulas.class);
		processRecords = Mockito.mock(ProcessRecords.class);
		extractors = Mockito.mock(Extractors.class);
		processPage = Mockito.mock(ProcessPage.class);
		htmlGenerators = Mockito.mock(HtmlGenerators.class);
		editingService = new EditingService(
				databaseStorageService,
				processRecords,
				formulas);
		when(formulas.getTitleSource())
				.thenReturn("SourceTitle");
	}

	@Test
	void addFormula_WrongArguments_NullArguments() {
		Payload receivedPayload = new Payload(
				"templateTitle",
				null,
				"content",
				true,
				"",
				null,
				"content",
				"title",
				null
		);
		Exception e = Assertions.assertThrows(Exception.class,
				() -> editingService.addFormula(null, receivedPayload));
		Assertions.assertEquals("THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.",
				e.getMessage());
		e = Assertions.assertThrows(Exception.class,
				() -> editingService.addFormula("formula", null));
		Assertions.assertEquals("THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.",
				e.getMessage());
	}

	@Test
	void addFormula_WrongArguments_InvalidFormulaName() {
		String formulaName = "formula name";
		String title = "Title";
		String originalContentString = "Line1\nLine2\n";
		List<String> originalContentList = new ArrayList<>();
		originalContentList.add("Line 1");
		originalContentList.add("Line 2");
		Boolean editExistingPage = true;
		Payload receivedPayload = new Payload(
				"templateTitle",
				null,
				originalContentString,
				editExistingPage,
				"",
				null,
				null,
				title,
				null
		);
		when(formulas.getFormula(formulaName))
				.thenReturn(new ArrayList<>());
		when(processRecords.stringToList(originalContentString))
				.thenReturn(originalContentList);
		when(processRecords.listToString(originalContentList))
				.thenReturn(originalContentString);
		Payload expectedPayload = new Payload(
				formulas.getTitleSource(),
				null,
				originalContentString,
				editExistingPage,
				"WRONG FORMULA NAME WAS ASKED.",
				null,
				null,
				title,
				null
		);

		Assertions.assertEquals(expectedPayload, editingService.addFormula(formulaName, receivedPayload));
	}

	@Test
	void addFormula_RightArguments_NullTitle() {
		String formulaName = "formula name";
		String originalContent = "Line1\nLine2\n";
		Boolean editExistingPage = true;
		Payload receivedPayload = new Payload(
				"templateTitle",
				null,
				originalContent,
				editExistingPage,
				"",
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
				editExistingPage,
				"FORMULA WAS APPENDED.",
				null,
				null,
				"",
				null
		);
		Assertions.assertEquals(expectedPayload, editingService.addFormula(formulaName, receivedPayload));
	}

	@Test
	void addFormula_RightArguments_BlankTitle() {
		String formulaName = "formula name";
		String originalContent = "Line1\nLine2\n";
		Boolean editExistingPage = true;
		Payload receivedPayload = new Payload(
				"templateTitle",
				null,
				originalContent,
				editExistingPage,
				"",
				null,
				null,
				"  ",
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
				editExistingPage,
				"FORMULA WAS APPENDED.",
				null,
				null,
				"",
				null
		);
		Assertions.assertEquals(expectedPayload, editingService.addFormula(formulaName, receivedPayload));
	}

	@Test
	void addFormula_RightArguments_NullContent() {
		String formulaName = "formula name";
		String title = "Title";
		Boolean editExistingPage = true;
		Payload receivedPayload = new Payload(
				"templateTitle",
				null,
				null,
				editExistingPage,
				"",
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
				editExistingPage,
				"FORMULA WAS APPENDED.",
				null,
				null,
				title,
				null
		);
		Assertions.assertEquals(expectedPayload, editingService.addFormula(formulaName, receivedPayload));
	}

	@Test
	void addFormula_RightArguments_NullEditExistingPage() {
		String formulaName = "formula name";
		String title = "Title";
		String originalContent = "Line1\nLine2\n";
		Payload receivedPayload = new Payload(
				"templateTitle",
				null,
				originalContent,
				null,
				"",
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
		Assertions.assertEquals(expectedPayload, editingService.addFormula(formulaName, receivedPayload));
	}

	@Test
	void addFormula_RightArguments_AllValidValues() {
		String formulaName = "formula name";
		String title = "Title";
		String originalContent = "Line1\nLine2\n";
		Boolean editExistingPage = true;
		Payload receivedPayload = new Payload(
				"templateTitle",
				null,
				originalContent,
				editExistingPage,
				"",
				null,
				originalContent,
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
				editExistingPage,
				"FORMULA WAS APPENDED.",
				null,
				null,
				title,
				null
		);
		Assertions.assertEquals(expectedPayload, editingService.addFormula(formulaName, receivedPayload));
	}
}
