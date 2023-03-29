package lgrimm1.JavaKnowledge.Process;

import lgrimm1.JavaKnowledge.Title.*;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Translates a TXT content to an HTML content, forming a knowledge page.
 * @see #processTxt(List, String, TitleRepository, Formulas, Extractors, HtmlGenerators)
 */
@Component
public class ProcessPage {

	/**
	 * Processes the original text file lines and generates html code.
	 * Also, extracts page titles as references.
	 * Both Lists of String are wrapped up into a record.
	 */
	public MainHtmlContentPayload processTxt(List<String> text,
											 String title,
											 TitleRepository titleRepository,
											 Formulas formulas,
											 Extractors extractors,
											 HtmlGenerators htmlGenerators) {
/*
		text = text.stream()
				.map(line -> htmlGenerators.changeToHtmlCharsInLine(line, formulas))
				.toList();
*/
		List<String> html = htmlGenerators.generateFirstTags(title, formulas);
		MainHtmlContentPayload payload = htmlGenerators.generateMainContent(text, formulas, extractors, titleRepository);
		html.addAll(payload.content());
		html.addAll(htmlGenerators.generateLastTags(formulas));
		html = htmlGenerators.collectAndReferenceHeaders(html, formulas);
		return new MainHtmlContentPayload(html, payload.titles());
	}

}
