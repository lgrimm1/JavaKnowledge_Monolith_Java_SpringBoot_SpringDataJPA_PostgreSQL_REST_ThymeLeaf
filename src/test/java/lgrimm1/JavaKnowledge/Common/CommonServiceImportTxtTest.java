package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Process.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.test.web.*;
import org.springframework.web.servlet.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import static org.mockito.Mockito.when;

class CommonServiceImportTxtTest {

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
	String filename2, filename3, filename4;
	Path path2, path3, path4;
	Stream<Path> paths;
	File file2, file3, file4;
	List<File> uploadedFiles, notImportedFiles;
	long[] uploadResults;
	List<String> titlesBeforeUploading, titlesAfterUploading;

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
		titlesBeforeUploading = List.of("Title 1");
		titlesAfterUploading = List.of("Title 1", "Title 3");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titlesBeforeUploading);
		filename2 = "file2.txt";
		filename3 = "file3.txt";
		filename4 = "file4.txt";
		path2 = Path.of(filename2);
		path3 = Path.of(filename3);
		path4 = Path.of(filename4);
		paths = Stream.of(path2, path3, path4);
		file2 = path2.toFile();
		file3 = path3.toFile();
		file4 = path4.toFile();
		uploadedFiles = List.of(file2, file3, file4);
		notImportedFiles = List.of(file2);
		uploadResults = new long[]{3, 3};
	}

	@Test
	void importTxt_NullPayload() {
		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PLEASE UPLOAD MINIMUM ONE PROPER FILE AND CONFIRM SOURCE OVERWRITING.",
				null,
				null,
				"",
				titlesBeforeUploading
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.importTxt("management", null, paths, uploadResults);
		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void importTxt_NullConfirm() {
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				null,
				null,
				null,
				"",
				null,
				null,
				"",
				titlesBeforeUploading
		);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PLEASE UPLOAD MINIMUM ONE PROPER FILE AND CONFIRM SOURCE OVERWRITING.",
				null,
				null,
				"",
				titlesBeforeUploading
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.importTxt("management", receivedPayload, paths, uploadResults);
		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void importTxt_NotConfirmed() {
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"",
				null,
				null,
				"",
				titlesBeforeUploading
		);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PLEASE UPLOAD MINIMUM ONE PROPER FILE AND CONFIRM SOURCE OVERWRITING.",
				null,
				null,
				"",
				titlesBeforeUploading
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.importTxt("management", receivedPayload, paths, uploadResults);
		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void importTxt_NullUploadResults() {
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				true,
				null,
				null,
				"",
				null,
				null,
				"",
				titlesBeforeUploading
		);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PLEASE UPLOAD MINIMUM ONE PROPER FILE AND CONFIRM SOURCE OVERWRITING.",
				null,
				null,
				"",
				titlesBeforeUploading
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.importTxt("management", receivedPayload, paths, null);
		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void importTxt_Confirmed_NoUploadedFiles() {
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				true,
				null,
				null,
				"",
				null,
				null,
				"",
				titlesBeforeUploading
		);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PLEASE UPLOAD MINIMUM ONE PROPER FILE AND CONFIRM SOURCE OVERWRITING.",
				null,
				null,
				"",
				titlesBeforeUploading
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.importTxt("management", receivedPayload, paths, new long[]{0, 0});
		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}

	@Test
	void importTxt_Confirmed_ExistingUploadedFiles() {
		Payload receivedPayload = new Payload(
				formulas.getTitleManagement(),
				true,
				null,
				null,
				"",
				null,
				null,
				"",
				titlesBeforeUploading
		);

		when(processRecords.importTxtFiles(
				uploadedFiles,
				titleRepository,
				txtRepository,
				htmlRepository,
				fileOperations,
				formulas,
				extractors))
				.thenReturn(notImportedFiles);

		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titlesAfterUploading);

		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"FILE IMPORT RESULTS: 2 IMPORTED, 1 NOT IMPORTED, 3 TOTAL.",
				null,
				null,
				"",
				titlesAfterUploading
		);
		Map<String, Object> model = new HashMap<>();
		model.put("payload", expectedPayload);

		ModelAndView modelAndView = commonService.importTxt("management", receivedPayload, paths, uploadResults);
		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, model);
	}
}