package lgrimm1.JavaKnowledge.Process;

import lgrimm1.JavaKnowledge.Title.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.when;

class ProcessPageTest {

	@Test
	void processTxt() {
		ProcessPage processPage = new ProcessPage();

		Extractors extractors = Mockito.mock(Extractors.class);
		Formulas formulas = Mockito.mock(Formulas.class);
		TitleRepository titleRepository = Mockito.mock(TitleRepository.class);
		HtmlGenerators htmlGenerators = Mockito.mock(HtmlGenerators.class);

		List<String> txtContent = new ArrayList<>(List.of(
				"Line 1 (TXT)",
				"Line 2 (TXT)"
		));

		when(htmlGenerators.changeToHtmlCharsInLine("Line 1 (TXT)", formulas))
				.thenReturn("Line 1 (TXT, HTML chars)");
		when(htmlGenerators.changeToHtmlCharsInLine("Line 2 (TXT)", formulas))
				.thenReturn("Line 2 (TXT, HTML chars)");

		when(htmlGenerators.generateFirstTags("Title Words", formulas))
				.thenReturn(new ArrayList<>(List.of(
						"First tags (HTML)"
				)));

		when(htmlGenerators.generateMainContent(new ArrayList<>(List.of(
				"Line 1 (TXT, HTML chars)",
				"Line 2 (TXT, HTML chars)"
		)), formulas, extractors, titleRepository))
				.thenReturn(new MainHtmlContentPayload(
						new ArrayList<>(List.of(
								"Main Content (HTML)"
						)), new ArrayList<>()
				));

		when(htmlGenerators.generateLastTags(formulas))
				.thenReturn(new ArrayList<>(List.of(
						"Last tags (HTML)"
				)));

		when(htmlGenerators.collectAndReferenceHeaders(new ArrayList<>(List.of(
				"First tags (HTML)",
				"Main Content (HTML)",
				"Last tags (HTML)"
		)), formulas))
				.thenReturn(new ArrayList<>(List.of(
						"Final HTML"
				)));

		List<String> expectedHtml = new ArrayList<>(List.of(
				"Final HTML"
		));

		MainHtmlContentPayload payload = processPage.processTxt(
				txtContent,
				"Title Words",
				titleRepository,
				formulas,
				extractors,
				htmlGenerators
		);

		Assertions.assertEquals(
				expectedHtml,
				payload.content());
		Assertions.assertTrue(payload.titles().isEmpty());
	}
}