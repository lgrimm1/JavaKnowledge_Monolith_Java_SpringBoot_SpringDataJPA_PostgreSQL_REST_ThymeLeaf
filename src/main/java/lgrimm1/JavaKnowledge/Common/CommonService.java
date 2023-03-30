package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Process.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.lang.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
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
				null,
				null,
				payload.getSearchText(),
				null,
				null,
				titles
		);
		return new ModelAndView(initialView, "payload", payload2);
	}

	public ModelAndView getPage(String initialView, List<String> titles) {
		if (titles == null || titles.size() != 1 || titles.get(0) == null || titles.get(0).isBlank()) {
			Payload payload = new Payload(
					null,
					null,
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
			return new ModelAndView("list", "payload", payload);
		}
		Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTitle(titles.get(0));
		if (optionalTitleEntity.isEmpty()) {
			Payload payload = new Payload(
					null,
					null,
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
			return new ModelAndView("list", "payload", payload);
		}
		Optional<HtmlEntity> optionalHtmlEntity = htmlRepository.findById(optionalTitleEntity.get().getHtmlId());
		Payload payload = new Payload(
				null,
				null,
				null,
				null,
				null,
				null,
				optionalHtmlEntity.get().getTitleReferences(),
				null,
				optionalTitleEntity.get().getFilename() + ".html",
				optionalTitleEntity.get().getTitle(),
				null
		);
		return new ModelAndView(initialView, "payload", payload);
	}

	public ModelAndView managePages(String initialView) {
		ModelAndView modelAndView = new ModelAndView(initialView);
		modelAndView.addObject("titles", processRecords.getAllTitles(titleRepository));
		modelAndView.addObject("files", "");
		modelAndView.addObject("confirm", false);
		modelAndView.addObject("message", "");
		return modelAndView;
	}

	public ModelAndView createSourcePage(String initialView) {
		ModelAndView modelAndView = new ModelAndView(initialView);
		modelAndView.addObject("title", "");
		modelAndView.addObject("file_name", "");
		modelAndView.addObject("content", new ArrayList<>());
		modelAndView.addObject("edit_existing_page", false);
		modelAndView.addObject("message", "");
		return modelAndView;
	}

	public ModelAndView editSourcePage(String initialView, List<String> titles) {
		ModelAndView modelAndView = new ModelAndView(initialView);
		if (titles == null || titles.size() != 1 || titles.get(0) == null || titles.get(0).isBlank()) {
			modelAndView.addObject("titles", processRecords.getAllTitles(titleRepository));
			modelAndView.addObject("files", "");
			modelAndView.addObject("confirm", false);
			modelAndView.addObject("message", "Please select exactly one title for editing.");
			modelAndView.setViewName("management");
		}
		else {
			Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTitle(titles.get(0));
			if (optionalTitleEntity.isEmpty()) {
				modelAndView.addObject("titles", processRecords.getAllTitles(titleRepository));
				modelAndView.addObject("files", "");
				modelAndView.addObject("confirm", false);
				modelAndView.addObject("message",
						"Please select exactly one title for editing.");
				modelAndView.setViewName("management");
			}
			else {
				long txtId = optionalTitleEntity.get().getTxtId();
				Optional<TxtEntity> optionalTxtEntity = txtRepository.findById(txtId);
				if (optionalTxtEntity.isEmpty()) {
					modelAndView.addObject("titles", processRecords.getAllTitles(titleRepository));
					modelAndView.addObject("files", "");
					modelAndView.addObject("confirm", false);
					modelAndView.addObject("message",
							"Please select exactly one title for editing.");
					modelAndView.setViewName("management");
				}
				else {
					modelAndView.addObject("title", optionalTitleEntity.get().getTitle());
					modelAndView.addObject("file_name", optionalTitleEntity.get().getFilename());
					modelAndView.addObject("content", processRecords.stringToList(optionalTxtEntity.get().getContent()));
					modelAndView.addObject("edit_existing_page", true);
					modelAndView.addObject("message", "");
				}
			}
		}
		return modelAndView;
	}

	public ModelAndView deletePages(String initialView, List<String> titles, Boolean confirm) {
		ModelAndView modelAndView = new ModelAndView(initialView);
		if (titles == null || titles.size() == 0 || !confirm) {
			modelAndView.addObject("titles", processRecords.getAllTitles(titleRepository));
			modelAndView.addObject("files", "");
			modelAndView.addObject("confirm", false);
			if (!confirm) {
				modelAndView.addObject("message", "Please confirm deletion.");
			}
			else {
				modelAndView.addObject("message", "Please select titles you wish to delete.");
			}
		}
		else {
			titles = titles.stream()
					.filter(title -> title != null && !title.isBlank())
					.toList();
			if (titles.isEmpty()) {
				modelAndView.addObject("titles", processRecords.getAllTitles(titleRepository));
				modelAndView.addObject("files", "");
				modelAndView.addObject("confirm", false);
				modelAndView.addObject("message",
						"Please select existing titles you wish to delete.");
			}
			else {
				long numberOfGivenTitles = titles.size();
				modelAndView.addObject(
						"message",
						processRecords.deleteByTitles(
								titles,
								titleRepository,
								txtRepository,
								htmlRepository) +
								" of " + numberOfGivenTitles + " titles were deleted.");
				modelAndView.addObject("titles", processRecords.getAllTitles(titleRepository));
				modelAndView.addObject("files", "");
				modelAndView.addObject("confirm", false);
			}
		}
		return modelAndView;
	}

	public ModelAndView publishPages(String initialView) {
		ModelAndView modelAndView = new ModelAndView(initialView);
		modelAndView.addObject("titles", processRecords.getAllTitles(titleRepository));
		modelAndView.addObject("files", "");
		modelAndView.addObject("confirm", false);
		long[] publishResults = processRecords.publishHtml(titleRepository, htmlRepository, fileOperations);
		modelAndView.addObject("message",
				publishResults[0] + " HTML files were pre-deleted, " + publishResults[1] + " were published.");
		return modelAndView;
	}

	public ModelAndView addFormula(String initialView,
								   String formulaName,
								   String title,
								   String fileName,
								   List<String> content,
								   Boolean editExistingPage) {
		ModelAndView modelAndView = new ModelAndView(initialView);
		if (title == null || title.isBlank()) {
			title = "";
		}
		if (fileName == null || fileName.isBlank()) {
			fileName = "";
		}
		if (content == null) {
			content = new ArrayList<>();
		}
		if (editExistingPage == null) {
			editExistingPage = false;
		}
		String formula = formulas.getFormula(formulaName);
		if (formula.isEmpty()) {
			modelAndView.addObject("message", "Wrong formula name was asked.");
		}
		else {
			content.addAll(List.of(formula.split("\n")));
			modelAndView.addObject("message", "Formula was appended.");
		}
		modelAndView.addObject("title", title);
		modelAndView.addObject("file_name", fileName);
		modelAndView.addObject("content", content);
		modelAndView.addObject("edit_existing_page", editExistingPage);
		return modelAndView;
	}

	public ModelAndView savePage(String initialView,
								 String title,
								 String fileName,
								 List<String> content,
								 Boolean editExistingPage) {
		ModelAndView modelAndView = new ModelAndView(initialView);
		if (title == null || title.isBlank()) {
			title = "";
			modelAndView.addObject("message", "Please define a title.");
		}
		else if (fileName == null || fileName.isBlank()) {
			fileName = "";
			modelAndView.addObject("message", "Please define a file name.");
		}
		else {
			if (content == null) {
				content = new ArrayList<>();
			}
			if (editExistingPage == null) {
				editExistingPage = false;
			}
			Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTitle(title);
			if (editExistingPage) {
				if (optionalTitleEntity.isEmpty()) {
					modelAndView.addObject("message", "There is no existing page with this title.");
				}
				else {
					txtRepository.deleteById(optionalTitleEntity.get().getTxtId());
					htmlRepository.deleteById(optionalTitleEntity.get().getHtmlId());
					titleRepository.deleteById(optionalTitleEntity.get().getId());
					HtmlEntity htmlEntity = htmlRepository.save(new HtmlEntity(new ArrayList<>(), new ArrayList<>()));
					TxtEntity txtEntity = txtRepository.save(new TxtEntity(processRecords.listToString(content)));
					titleRepository.save(new TitleEntity(title, fileName, txtEntity.getId(), htmlEntity.getId()));
					modelAndView.addObject("message", "Source page has been overwritten.");
				}
			}
			else { //editExistingPage == false
				if (optionalTitleEntity.isPresent()) {
					modelAndView.addObject("message", "There is an existing page with this title.");
				}
				else {
					HtmlEntity htmlEntity = htmlRepository.save(new HtmlEntity(new ArrayList<>(), new ArrayList<>()));
					TxtEntity txtEntity = txtRepository.save(new TxtEntity(processRecords.listToString(content)));
					titleRepository.save(new TitleEntity(title, fileName, txtEntity.getId(), htmlEntity.getId()));
					editExistingPage = true;
					modelAndView.addObject("message", "Source page has been saved.");
				}
			}
		}
		modelAndView.addObject("title", title);
		modelAndView.addObject("file_name", fileName);
		modelAndView.addObject("content", content);
		modelAndView.addObject("edit_existing_page", editExistingPage);
		return modelAndView;
	}

	public ModelAndView importTxt(String initialView, String fileNames, Boolean confirm) {
		ModelAndView modelAndView = new ModelAndView(initialView);
		if (fileNames == null || fileNames.isBlank() || confirm == null || !confirm) {
			modelAndView.addObject("titles", processRecords.getAllTitles(titleRepository));
			modelAndView.addObject("files", "");
			modelAndView.addObject("confirm", false);
			modelAndView.addObject("message",
					"Please upload minimum one file and confirm source overwriting.");
		}
		else {
			List<File> files = Stream.of(fileNames.split(fileOperations.getOSPathSeparator()))
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
			modelAndView.addObject("titles", titles);
			modelAndView.addObject("files", "");
			modelAndView.addObject("confirm", false);
			modelAndView.addObject("message",
					notImportedFiles.size() + " of " + files.size() + " files were not imported.");
		}
		return modelAndView;
	}

	public ModelAndView generateHtml(String initialView, Boolean confirm) {
		ModelAndView modelAndView = new ModelAndView(initialView);
		if (confirm == null || !confirm) {
			modelAndView.addObject("titles", processRecords.getAllTitles(titleRepository));
			modelAndView.addObject("files", "");
			modelAndView.addObject("confirm", false);
			modelAndView.addObject("message", "Please confirm generating pages.");
		}
		else {
			long[] messageData = processRecords.generate(
					titleRepository,
					txtRepository,
					htmlRepository,
					formulas,
					processPage,
					extractors,
					htmlGenerators);
			modelAndView.addObject("titles", processRecords.getAllTitles(titleRepository));
			modelAndView.addObject("files", "");
			modelAndView.addObject("confirm", false);
			modelAndView.addObject("message",
					messageData[0] + " pages in " + messageData[1] + " seconds has been processed.");
		}
		return modelAndView;
	}
}

//TODO check all Stream operations for being parallel and its necessity.
//TODO exportTxt()
