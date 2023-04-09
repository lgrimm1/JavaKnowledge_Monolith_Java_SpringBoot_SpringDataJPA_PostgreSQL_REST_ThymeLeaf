package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Process.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.lang.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

@Service
public class CommonService {

	private final TitleRepository titleRepository;
	private final TxtRepository txtRepository;
	private final HtmlRepository htmlRepository;
	private final Formulas formulas;
	private final ProcessRecords processRecords;
	private final FileOperations fileOperations;
	private final Extractors extractors;
	private final ProcessPage processPage;
	private final HtmlGenerators htmlGenerators;

	@Autowired
	public CommonService(TitleRepository titleRepository,
						 TxtRepository txtRepository,
						 HtmlRepository htmlRepository,
						 Formulas formulas,
						 ProcessRecords processRecords,
						 FileOperations fileOperations,
						 Extractors extractors,
						 ProcessPage processPage,
						 HtmlGenerators htmlGenerators) {
		this.titleRepository = titleRepository;
		this.txtRepository = txtRepository;
		this.htmlRepository = htmlRepository;
		this.formulas = formulas;
		this.processRecords = processRecords;
		this.fileOperations = fileOperations;
		this.extractors = extractors;
		this.processPage = processPage;
		this.htmlGenerators = htmlGenerators;
	}

	public ModelAndView getRoot(String initialView) {
		Payload payload = new Payload(
				null,
				null,
				null,
				null,
				null,
				"",
				null,
				null,
				null
		);
		return new ModelAndView(initialView, "payload", payload);
	}

	/**
	 * Finds titles which contain any word of the given text, furthermore
	 * titles of which their TXT content contains the whole given text.
	 * The search trims the given text and ignores case.
	 * In case there is no search text given, returns all titles.
	 */
	public ModelAndView searchPages(String initialView, Payload payload) {
		if (payload == null || payload.getSearchText() == null || payload.getSearchText().isBlank()) {
			Payload payload2 = new Payload(
					null,
					null,
					null,
					null,
					null,
					"<all titles>",
					null,
					null,
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		List<String> titles = processRecords.searchBySearchText(
						payload.getSearchText(),
						titleRepository,
						txtRepository)
				.stream()
				.sorted()
				.toList();
		Payload payload2 = new Payload(
				null,
				null,
				null,
				null,
				null,
				payload.getSearchText(),
				null,
				null,
				titles
		);
		return new ModelAndView(initialView, "payload", payload2);
	}

	public ModelAndView getPage(String initialView, Payload payload) {
		if (payload == null || payload.getTitles() == null || payload.getTitles().size() != 1 || payload.getTitles().get(0) == null || payload.getTitles().get(0).isBlank()) {
			Payload payload2 = new Payload(
					null,
					null,
					null,
					null,
					null,
					"<all titles>",
					null,
					null,
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView("list", "payload", payload2);
		}
		List<String> titles = payload.getTitles();
		Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTitle(titles.get(0));
		if (optionalTitleEntity.isEmpty()) {
			Payload payload2 = new Payload(
					null,
					null,
					null,
					null,
					null,
					"<all titles>",
					null,
					null,
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView("list", "payload", payload2);
		}
		Optional<HtmlEntity> optionalHtmlEntity = htmlRepository.findById(optionalTitleEntity.get().getHtmlId());
		if (optionalHtmlEntity.isEmpty()) {
			Payload payload2 = new Payload(
					null,
					null,
					null,
					null,
					null,
					"<all titles>",
					null,
					null,
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView("list", "payload", payload2);
		}
		Payload payload2 = new Payload(
				null,
				null,
				null,
				null,
				null,
				null,
				optionalTitleEntity.get().getFilename() + ".html",
				null,
				optionalHtmlEntity.get().getTitleReferences()
		);
		return new ModelAndView(initialView, "payload", payload2);
	}

	public ModelAndView managePages(String initialView) {
		Payload payload = new Payload(
				false,
				null,
				null,
				"",
				"",
				null,
				null,
				"",
				processRecords.getAllTitles(titleRepository)
		);
		return new ModelAndView(initialView, "payload", payload);
	}

	public ModelAndView createSourcePage(String initialView) {
		Payload payload = new Payload(
				null,
				"",
				false,
				null,
				"",
				null,
				null,
				"",
				null
		);
		return new ModelAndView(initialView, "payload", payload);
	}

	public ModelAndView editSourcePage(String initialView, Payload payload) {
		if (payload == null || payload.getTitles() == null || payload.getTitles().size() != 1 || payload.getTitles().get(0) == null || payload.getTitles().get(0).isBlank()) {
			Payload payload2 = new Payload(
					false,
					null,
					null,
					"",
					"PLEASE SELECT EXACTLY ONE TITLE FOR EDITING.",
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView("management", "payload", payload2);
		}
		Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTitle(payload.getTitles().get(0));
		if (optionalTitleEntity.isEmpty()) {
			Payload payload2 = new Payload(
					false,
					null,
					null,
					"",
					"PLEASE SELECT EXACTLY ONE TITLE FOR EDITING.",
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView("management", "payload", payload2);
		}
		long txtId = optionalTitleEntity.get().getTxtId();
		Optional<TxtEntity> optionalTxtEntity = txtRepository.findById(txtId);
		if (optionalTxtEntity.isEmpty()) {
			Payload payload2 = new Payload(
					false,
					null,
					null,
					"",
					"PLEASE SELECT EXACTLY ONE TITLE FOR EDITING.",
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView("management", "payload", payload2);
		}
		Payload payload2 = new Payload(
				null,
				optionalTxtEntity.get().getContent(),
				true,
				null,
				"",
				null,
				null,
				optionalTitleEntity.get().getTitle(),
				null
		);
		return new ModelAndView(initialView, "payload", payload2);
	}

//	public ModelAndView deletePages(String initialView, List<String> titles, Boolean confirm) {
	public ModelAndView deletePages(String initialView, Payload payload) {
		if (payload == null || payload.getTitles() == null || payload.getTitles().size() == 0 || payload.getConfirm() == null || !payload.getConfirm()) {
			String message;
			if (payload == null) {
				message = "PLEASE SELECT TITLES YOU WISH TO DELETE.";
			}
			else {
				message = (payload.getConfirm() == null || !payload.getConfirm()) ? "PLEASE CONFIRM DELETION." : "PLEASE SELECT TITLES YOU WISH TO DELETE.";
			}
			Payload payload2 = new Payload(
					false,
					null,
					null,
					"",
					message,
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		List<String> titles = payload.getTitles();
		titles = titles.stream()
				.filter(title -> title != null && !title.isBlank())
				.toList();
		if (titles.isEmpty()) {
			Payload payload2 = new Payload(
					false,
					null,
					null,
					"",
					"PLEASE SELECT EXISTING TITLES YOU WISH TO DELETE.",
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		long numberOfGivenTitles = titles.size();
		String message = processRecords.deleteByTitles(
				titles,
				titleRepository,
				txtRepository,
				htmlRepository) +
				" OF " + numberOfGivenTitles + " TITLES WERE DELETED.";
		Payload payload2 = new Payload(
				false,
				null,
				null,
				"",
				message,
				null,
				null,
				"",
				processRecords.getAllTitles(titleRepository)
		);
		return new ModelAndView(initialView, "payload", payload2);
	}

	public ModelAndView publishPages(String initialView, Payload payload) {
		if (payload == null || payload.getConfirm() == null || !payload.getConfirm()) {
			Payload payload2 = new Payload(
					false,
					null,
					null,
					"",
					"PLEASE CONFIRM PUBLISHING PAGES.",
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		long[] publishResults = processRecords.publishHtml(titleRepository, htmlRepository, fileOperations);
		Payload payload2 = new Payload(
				false,
				null,
				null,
				"",
				publishResults[0] + " HTML FILES WERE PRE-DELETED, " + publishResults[1] + " WERE PUBLISHED.",
				null,
				null,
				"",
				processRecords.getAllTitles(titleRepository)
		);
		return new ModelAndView(initialView, "payload", payload2);
	}

	public ModelAndView addFormula(String initialView, String formulaName, Payload payload) {
		if (payload == null) {
			Payload payload2 = new Payload(
					null,
					"",
					false,
					null,
					"THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.",
					null,
					null,
					"",
					null
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		String title = payload.getTitle();
		if (title == null || title.isBlank()) {
			title = "";
		}
		String content = payload.getContent();
		List<String> contentList = processRecords.stringToList(content);
		Boolean editExistingPage = payload.getEditExistingPage();
		if (editExistingPage == null) {
			editExistingPage = false;
		}
		List<String> formula = formulas.getFormula(formulaName);
		String message;
		if (formula.isEmpty()) {
			message = "WRONG FORMULA NAME WAS ASKED.";
		}
		else {
			contentList.addAll(formula);
			message = "FORMULA WAS APPENDED.";
		}
		Payload payload2 = new Payload(
				null,
				processRecords.listToString(contentList),
				editExistingPage,
				null,
				message,
				null,
				null,
				title,
				null
		);
		return new ModelAndView(initialView, "payload", payload2);
	}

	public ModelAndView savePage(String initialView, Payload payload) {
		if (payload == null) {
			Payload payload2 = new Payload(
					null,
					"",
					false,
					null,
					"THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.",
					null,
					null,
					"",
					null
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		String title = payload.getTitle();
		String content = payload.getContent();
		Boolean editExistingPage = payload.getEditExistingPage();
		if (content == null) {
			content = "";
		}
		if (editExistingPage == null) {
			editExistingPage = false;
		}
		if (title == null || title.isBlank()) {
			Payload payload2 = new Payload(
					null,
					content,
					editExistingPage,
					null,
					"PLEASE DEFINE A TITLE.",
					null,
					null,
					"",
					null
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		String message;
		Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTitle(title);
		if (editExistingPage) {
			if (optionalTitleEntity.isEmpty()) {
				message = "THERE IS NO EXISTING PAGE WITH THIS TITLE.";
			}
			else {
				txtRepository.deleteById(optionalTitleEntity.get().getTxtId());
				htmlRepository.deleteById(optionalTitleEntity.get().getHtmlId());
				titleRepository.deleteById(optionalTitleEntity.get().getId());
				HtmlEntity htmlEntity = htmlRepository.save(new HtmlEntity(new ArrayList<>(), new ArrayList<>()));
				TxtEntity txtEntity = txtRepository.save(new TxtEntity(content));
				titleRepository.save(new TitleEntity(title, fileOperations.generateFilename(title, titleRepository), txtEntity.getId(), htmlEntity.getId()));
				message = "SOURCE PAGE HAS BEEN OVERWRITTEN.";
			}
			Payload payload2 = new Payload(
					null,
					content,
					editExistingPage,
					null,
					message,
					null,
					null,
					title,
					null
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		else { //editExistingPage == false
			if (optionalTitleEntity.isPresent()) {
				message = "THERE IS AN EXISTING PAGE WITH THIS TITLE.";
				Payload payload2 = new Payload(
						null,
						content,
						editExistingPage,
						null,
						message,
						null,
						null,
						title,
						null
				);
				return new ModelAndView(initialView, "payload", payload2);
			}
			else {
				HtmlEntity htmlEntity = htmlRepository.save(new HtmlEntity(new ArrayList<>(), new ArrayList<>()));
				TxtEntity txtEntity = txtRepository.save(new TxtEntity(content));
				titleRepository.save(new TitleEntity(title, fileOperations.generateFilename(title, titleRepository), txtEntity.getId(), htmlEntity.getId()));
				message = "SOURCE PAGE HAS BEEN SAVED.";
			}
			Payload payload2 = new Payload(
					false,
					null,
					null,
					"",
					message,
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView("management", "payload", payload2);
		}
	}

	public ModelAndView importTxt(String initialView, Payload payload) {
		if (payload == null || payload.getFiles() == null || payload.getFiles().isBlank() || payload.getConfirm() == null || !payload.getConfirm()) {
			Payload payload2 = new Payload(
					false,
					null,
					null,
					"",
					"PLEASE UPLOAD MINIMUM ONE FILE AND CONFIRM SOURCE OVERWRITING.",
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		List<File> files = Stream.of(payload.getFiles().split(fileOperations.getOSPathSeparator()))
				.map(File::new)
				.toList();
		List<File> notImportedFiles = processRecords.importTxtFiles(
				files,
				titleRepository,
				txtRepository,
				htmlRepository,
				fileOperations,
				formulas,
				extractors);
		List<String> titles = processRecords.getAllTitles(titleRepository);
		Payload payload2 = new Payload(
				false,
				null,
				null,
				"",
				notImportedFiles.size() + " OF " + files.size() + " FILES WERE NOT IMPORTED.",
				null,
				null,
				"",
				titles
		);
		return new ModelAndView(initialView, "payload", payload2);
	}

	public ModelAndView generateHtml(String initialView, Payload payload) {
		if (payload == null || payload.getConfirm() == null || !payload.getConfirm()) {
			Payload payload2 = new Payload(
					false,
					null,
					null,
					"",
					"PLEASE CONFIRM GENERATING PAGES.",
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		long[] messageData = processRecords.generate(
				titleRepository,
				txtRepository,
				htmlRepository,
				formulas,
				processPage,
				extractors,
				htmlGenerators);
		Payload payload2 = new Payload(
				false,
				null,
				null,
				"",
				messageData[0] + " PAGES IN " + messageData[1] + " SECONDS HAS BEEN PROCESSED.",
				null,
				null,
				"",
				processRecords.getAllTitles(titleRepository)
		);
		return new ModelAndView(initialView, "payload", payload2);
	}

	public ModelAndView renameSourcePage(String initialView, Payload payload) {
		if (payload == null ||
				payload.getTitles() == null ||
				payload.getTitles().size() != 1 ||
				payload.getTitle() == null ||
				payload.getTitle().isBlank()) {
			Payload payload2 = new Payload(
					false,
					null,
					null,
					"",
					"PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.",
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		Optional<TitleEntity> originalTitleEntity = titleRepository.findByTitle(payload.getTitles().get(0));
		if (originalTitleEntity.isEmpty()) {
			Payload payload2 = new Payload(
					false,
					null,
					null,
					"",
					"PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.",
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		Optional<TitleEntity> newTitleEntity = titleRepository.findByTitle(payload.getTitle());
		if (newTitleEntity.isPresent()) {
			Payload payload2 = new Payload(
					false,
					null,
					null,
					"",
					"THE GIVEN NEW TITLE ALREADY EXISTS, PLEASE DEFINE AN OTHER ONE.",
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		String fileName = fileOperations.generateFilename(payload.getTitle(), titleRepository);
		long txtId = originalTitleEntity.get().getTxtId();
		long htmlId = originalTitleEntity.get().getHtmlId();
		titleRepository.deleteById(originalTitleEntity.get().getId());
		titleRepository.save(new TitleEntity(payload.getTitle(), fileName, txtId, htmlId));
		Payload payload2 = new Payload(
				false,
				null,
				null,
				"",
				"PAGE HAS BEEN RENAMED.",
				null,
				null,
				"",
				processRecords.getAllTitles(titleRepository)
		);
		return new ModelAndView(initialView, "payload", payload2);
	}
}

//TODO check all Stream operations for being parallel and its necessity.
//TODO exportTxt()
