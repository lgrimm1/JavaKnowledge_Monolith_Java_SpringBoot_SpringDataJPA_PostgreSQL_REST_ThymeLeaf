package lgrimm.javaknowledge.process;

import lgrimm.javaknowledge.datamodels.HtmlContentAndReferences;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

class ProcessPageTest {

	@Test
	void processTxt() {
		HtmlGenerators htmlGenerators = Mockito.mock(HtmlGenerators.class);
		ProcessPage processPage = new ProcessPage(htmlGenerators);

		List<String> txtContent = new ArrayList<>(List.of(
				"Line 1 (TXT)",
				"Line 2 (TXT)"
		));

		when(htmlGenerators.generateFirstTags("Title Words"))
				.thenReturn(new ArrayList<>(List.of(
						"First tags (HTML)"
				)));

		when(htmlGenerators.generateMainContent(new ArrayList<>(List.of(
				"Line 1 (TXT)",
				"Line 2 (TXT)"
		))))
				.thenReturn(new HtmlContentAndReferences(
						new ArrayList<>(List.of(
								"Main Content (HTML)"
						)), new ArrayList<>()
				));

		when(htmlGenerators.generateLastTags())
				.thenReturn(new ArrayList<>(List.of(
						"Last tags (HTML)"
				)));

		when(htmlGenerators.collectAndReferenceHeaders(new ArrayList<>(List.of(
				"First tags (HTML)",
				"Main Content (HTML)",
				"Last tags (HTML)"
		))))
				.thenReturn(new ArrayList<>(List.of(
						"Final HTML"
				)));

		List<String> expectedHtml = new ArrayList<>(List.of(
				"Final HTML"
		));

		HtmlContentAndReferences payload = processPage.processTxt(
				txtContent,
				"Title Words");

		Assertions.assertEquals(
				expectedHtml,
				payload.content());
		Assertions.assertTrue(payload.titles().isEmpty());
	}
}