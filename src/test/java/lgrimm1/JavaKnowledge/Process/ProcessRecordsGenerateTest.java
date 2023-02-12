package lgrimm1.JavaKnowledge.Process;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;

class ProcessRecordsGenerateTest {

	ProcessRecords processRecords = new ProcessRecords();
	TitleRepository titleRepository;
	TxtRepository txtRepository;
	HtmlRepository htmlRepository;
	FileOperations fileOperations;
	Formulas formulas;
	Extractors extractors;
	ProcessPage processPage;
	HtmlGenerators htmlGenerators;

	@BeforeEach
	void setUp() {
		titleRepository = Mockito.mock(TitleRepository.class);
		txtRepository = Mockito.mock(TxtRepository.class);
		htmlRepository = Mockito.mock(HtmlRepository.class);
		fileOperations = Mockito.mock(FileOperations.class);
		formulas = Mockito.mock(Formulas.class);
		extractors = Mockito.mock(Extractors.class);
		processPage = Mockito.mock(ProcessPage.class);
		htmlGenerators = Mockito.mock(HtmlGenerators.class);
	}

	@Test
	void generate() {
		when(titleRepository.findAll())
				.thenReturn(List.of(
						new TitleEntity(1L, "Title 1", "title_1", 1L, 1L),
						new TitleEntity(2L, "Title 2", "title_2", 2L, 2L),
						new TitleEntity(3L, "Title 3", "title_3", 3L, 3L),
						new TitleEntity(4L, "Title 4", "title_4", 4L, 4L),
						new TitleEntity(5L, "Title 5", "title_5", 5L, 5L)
				));
		when(txtRepository.findById(1L))
				.thenReturn(Optional.of(new TxtEntity(1L, List.of(
						"Line 11",
						"Line 12"
				))));
		when(txtRepository.findById(2L))
				.thenReturn(Optional.of(new TxtEntity(2L, List.of(
						"Line 21",
						"Line 22"
				))));
		when(txtRepository.findById(3L))
				.thenReturn(Optional.of(new TxtEntity(3L, List.of(
						"Line 31",
						"Line 32"
				))));
		when(txtRepository.findById(4L))
				.thenReturn(Optional.of(new TxtEntity(4L, List.of(
						"Line 41",
						"Line 42"
				))));
		when(txtRepository.findById(5L))
				.thenReturn(Optional.of(new TxtEntity(5L, List.of(
						"Line 51",
						"Line 52"
				))));
		when(processPage.processTxt(
				List.of(
						"Line 11",
						"Line 12"
				),
				"Title 1",
				titleRepository,
				formulas,
				extractors,
				htmlGenerators))
				.thenReturn(List.of(
						"New Line 11",
						"New Line 12"
				));
		when(processPage.processTxt(
				List.of(
						"Line 21",
						"Line 22"
				),
				"Title 2",
				titleRepository,
				formulas,
				extractors,
				htmlGenerators))
				.thenReturn(List.of(
						"New Line 21",
						"New Line 22"
				));
		when(processPage.processTxt(
				List.of(
						"Line 31",
						"Line 32"
				),
				"Title 3",
				titleRepository,
				formulas,
				extractors,
				htmlGenerators))
				.thenReturn(List.of(
						"New Line 31",
						"New Line 32"
				));
		when(processPage.processTxt(
				List.of(
						"Line 41",
						"Line 42"
				),
				"Title 4",
				titleRepository,
				formulas,
				extractors,
				htmlGenerators))
				.thenReturn(List.of(
						"New Line 41",
						"New Line 42"
				));
		when(processPage.processTxt(
				List.of(
						"Line 51",
						"Line 52"
				),
				"Title 5",
				titleRepository,
				formulas,
				extractors,
				htmlGenerators))
				.thenReturn(List.of(
						"New Line 51",
						"New Line 52"
				));
		when(htmlRepository.save(new HtmlEntity(List.of(
				"New Line 11",
				"New Line 12"
		))))
				.thenReturn(new HtmlEntity(11L, List.of(
						"New Line 11",
						"New Line 12"
				)));
		when(htmlRepository.save(new HtmlEntity(List.of(
				"New Line 21",
				"New Line 22"
		))))
				.thenReturn(new HtmlEntity(12L, List.of(
						"New Line 21",
						"New Line 22"
				)));
		when(htmlRepository.save(new HtmlEntity(List.of(
				"New Line 31",
				"New Line 32"
		))))
				.thenReturn(new HtmlEntity(13L, List.of(
						"New Line 31",
						"New Line 32"
				)));
		when(htmlRepository.save(new HtmlEntity(List.of(
				"New Line 41",
				"New Line 42"
		))))
				.thenReturn(new HtmlEntity(14L, List.of(
						"New Line 41",
						"New Line 42"
				)));
		when(htmlRepository.save(new HtmlEntity(List.of(
				"New Line 51",
				"New Line 52"
		))))
				.thenReturn(new HtmlEntity(15L, List.of(
						"New Line 51",
						"New Line 52"
				)));

		long[] result = processRecords.generate(titleRepository, txtRepository, htmlRepository, formulas, processPage, extractors, htmlGenerators);
		Assertions.assertEquals(5, result[0]);
		Assertions.assertTrue(result[1] > 0);
	}
}