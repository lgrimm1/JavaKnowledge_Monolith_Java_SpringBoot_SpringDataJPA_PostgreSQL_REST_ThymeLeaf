package lgrimm1.javaknowledge.services;

import lgrimm1.javaknowledge.databasestorage.*;
import lgrimm1.javaknowledge.datamodels.*;
import lgrimm1.javaknowledge.process.Formulas;
import lgrimm1.javaknowledge.process.ProcessRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @see #getRoot()
 * @see #searchPages(Payload)
 * @see #getPage(Payload)
 */
@Service
public class BrowsingService {
/*
	private final TitleRepository titleRepository;
	private final TxtRepository txtRepository;
	private final HtmlRepository htmlRepository;
*/
	private final DatabaseStorageService databaseStorageService;
	private final ProcessRecords processRecords;
	private final Formulas formulas;

	@Autowired
/*
	public BrowsingService(TitleRepository titleRepository,
						   TxtRepository txtRepository,
						   HtmlRepository htmlRepository,
						   DatabaseStorageService databaseStorageService, ProcessRecords processRecords,
						   Formulas formulas) {
		this.titleRepository = titleRepository;
		this.txtRepository = txtRepository;
		this.htmlRepository = htmlRepository;
*/
	public BrowsingService(DatabaseStorageService databaseStorageService,
						   ProcessRecords processRecords,
						   Formulas formulas) {
		this.databaseStorageService = databaseStorageService;
		this.processRecords = processRecords;
		this.formulas = formulas;
	}

	public Payload getRoot() {
		return new Payload(
				formulas.getTitleRoot(),
				null,
				null,
				null,
				null,
				"",
				null,
				null,
				null
		);
	}

	/**
	 * Finds titles which contain any word of the given text, furthermore
	 * titles of which their TXT content contains the whole given text.
	 * The search trims the given text and ignores case.
	 * In case there is no search text given, returns all titles.
	 */
	public Payload searchPages(Payload payload) {
		if (payload == null || payload.getSearchText() == null) {
			throw new RuntimeException("THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.");
		}
/*
		List<String> titles = processRecords.searchBySearchText(
						payload.getSearchText().trim(),
						titleRepository,
						txtRepository)
				.stream()
				.sorted()
				.toList();
*/
		return new Payload(
				formulas.getTitleList(),
				null,
				null,
				null,
				null,
				payload.getSearchText().trim(),
				null,
				null,
				databaseStorageService.findTitlesBySearchText(payload.getSearchText().trim())
		);
	}

	public Payload getPage(Payload payload) {
		if (payload == null ||
				payload.getTitles() == null ||
				payload.getTitles().size() != 1 ||
				payload.getTitles().get(0) == null ||
				payload.getTitles().get(0).isBlank()) {
			throw new RuntimeException("THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.");
		}
		List<String> titles = payload.getTitles();
		Optional<TitleEntity> optionalTitleEntity = databaseStorageService.findTitleByTitle(titles.get(0));
/*
		Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTitle(titles.get(0));
*/
		if (optionalTitleEntity.isEmpty()) {
			throw new RuntimeException("THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.");
		}
		Optional<HtmlEntity> optionalHtmlEntity = databaseStorageService.findHtmlById(optionalTitleEntity.get().getHtmlId());
		if (optionalHtmlEntity.isEmpty()) {
			throw new RuntimeException("THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.");
		}
		return new Payload(
				formulas.getTitlePage(),
				null,
				null,
				null,
				null,
				null,
				processRecords.listToString(optionalHtmlEntity.get().getContent()),
				null,
				optionalHtmlEntity.get().getTitleReferences()
		);
	}
}