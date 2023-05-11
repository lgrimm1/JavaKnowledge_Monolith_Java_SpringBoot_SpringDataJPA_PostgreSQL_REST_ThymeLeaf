package lgrimm1.javaknowledge.process;

import lgrimm1.javaknowledge.databasestorage.*;
import org.springframework.stereotype.*;

import java.util.*;

/**
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
		List<String> html = htmlGenerators.generateFirstTags(title, formulas);
		MainHtmlContentPayload payload = htmlGenerators.generateMainContent(text, formulas, extractors);
		html.addAll(payload.content());
		html.addAll(htmlGenerators.generateLastTags(formulas));
		html = htmlGenerators.collectAndReferenceHeaders(html, formulas);
		return new MainHtmlContentPayload(html, payload.titles());
	}

}
