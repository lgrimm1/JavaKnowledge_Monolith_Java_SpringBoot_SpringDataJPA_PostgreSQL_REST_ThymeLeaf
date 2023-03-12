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
import java.util.*;
import java.util.stream.*;

import static org.mockito.Mockito.*;

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
	void importTxtNullFiles() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", "");
		map.put("confirm", false);
		map.put("message", "Please upload minimum one file and confirm source overwriting.");

		ModelAndView modelAndView = commonService.importTxt("management", null, true);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void importTxtBlankFiles() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", "");
		map.put("confirm", false);
		map.put("message", "Please upload minimum one file and confirm source overwriting.");

		ModelAndView modelAndView = commonService.importTxt("management", "  ", true);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void importTxtNullConfirm() {
		String fileNames = "file_1;file_2";
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", "");
		map.put("confirm", false);
		map.put("message", "Please upload minimum one file and confirm source overwriting.");

		ModelAndView modelAndView = commonService.importTxt("management", fileNames, null);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void importTxtNotConfirmed() {
		String fileNames = "file_1;file_2";
		List<String> titles = List.of("Title 1", "Title 2");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", "");
		map.put("confirm", false);
		map.put("message", "Please upload minimum one file and confirm source overwriting.");

		ModelAndView modelAndView = commonService.importTxt("management", fileNames, false);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}

	@Test
	void importTxtConfirmed() {

		when(fileOperations.getOSPathSeparator())
				.thenReturn(";");
		String notImportedFiles = "file_1;file_2;file_3";
		String allFiles = notImportedFiles + ";file_4;file_5";

		List<File> notImported = Stream.of(notImportedFiles.split(";"))
				.sorted()
				.map(File::new)
				.toList();
		List<File> all = Stream.of(allFiles.split(";"))
				.sorted()
				.map(File::new)
				.toList();
		when(processRecords.importTxtFiles(
				all,
				titleRepository,
				txtRepository,
				htmlRepository,
				fileOperations,
				formulas,
				extractors))
				.thenReturn(notImported);

		List<String> titles = List.of("Title 4", "Title 5");
		when(processRecords.getAllTitles(titleRepository))
				.thenReturn(titles);

		Map<String, Object> map = new HashMap<>();
		map.put("titles", titles);
		map.put("files", "");
		map.put("confirm", false);
		map.put("message", "3 of 5 files were not imported.");

		ModelAndView modelAndView = commonService.importTxt("management", allFiles, true);

		ModelAndViewAssert.assertViewName(modelAndView, "management");
		ModelAndViewAssert.assertModelAttributeValues(modelAndView, map);
	}
}