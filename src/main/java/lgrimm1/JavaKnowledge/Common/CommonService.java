package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Process.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;

import java.io.*;
import java.util.*;

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

	public void getRoot(Model model) {
		model.addAttribute("search_text", "");
	}

	/**
	 * Finds titles which contain any word of the given text, furthermore
	 * titles of which their TXT content contains the whole given text.
	 * The search trims the given text and ignores case.
	 * In case there is no search text given, returns all titles.
	 */
	public void searchPages(String searchText, Model model) {
		if (searchText == null || searchText.isBlank()) {
			model.addAttribute("titles", processRecords.getAllTitles(titleRepository));
		}
		else {
			Set<String> titles = processRecords.searchBySearchText(searchText, titleRepository, txtRepository);
			model.addAttribute(
					"titles",
					titles.stream()
							.sorted()
							.toList()
			);
		}
	}

	public void getPage(List<String> titles, Model model) {
		//TODO open one or more static pages
	}

	public void managePages(Model model) {
		model.addAttribute("titles", processRecords.getAllTitles(titleRepository));
		model.addAttribute("files", new ArrayList<File>());
		model.addAttribute("confirm", false);
		model.addAttribute("message", "");
	}

	public void createSourcePage(Model model) {
		model.addAttribute("title", "");
		model.addAttribute("file_name", "");
		model.addAttribute("content", new ArrayList<String>());
		model.addAttribute("edit_existing_page", false);
		model.addAttribute("message", "");
	}

	public void editSourcePage(List<String> titles, Model model) {
		if (titles == null || titles.size() != 1 || titles.get(0) == null || titles.get(0).isBlank()) {
			model.addAttribute("titles", processRecords.getAllTitles(titleRepository));
			model.addAttribute("files", new ArrayList<File>());
			model.addAttribute("confirm", false);
			model.addAttribute("message", "Please select exactly one title for editing.");
		}
		else {
			Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTitle(titles.get(0));
			if (optionalTitleEntity.isEmpty()) {
				model.addAttribute("titles", processRecords.getAllTitles(titleRepository));
				model.addAttribute("files", new ArrayList<File>());
				model.addAttribute("confirm", false);
				model.addAttribute("message", "Please select exactly one title for editing.");
			}
			else {
				long txtId = optionalTitleEntity.get().getTxtId();
				Optional<TxtEntity> optionalTxtEntity = txtRepository.findById(txtId);
				if (optionalTxtEntity.isEmpty()) {
					model.addAttribute("titles", processRecords.getAllTitles(titleRepository));
					model.addAttribute("files", new ArrayList<File>());
					model.addAttribute("confirm", false);
					model.addAttribute("message", "Please select exactly one title for editing.");
				}
				else {
					model.addAttribute("title", optionalTitleEntity.get().getTitle());
					model.addAttribute("file_name", optionalTitleEntity.get().getFilename());
					model.addAttribute("content", optionalTxtEntity.get().getContent());
					model.addAttribute("edit_existing_page", true);
					model.addAttribute("message", "");
				}
			}
		}
	}

	public void deletePages(List<String> titles, Boolean confirm, Model model) {
		if (titles == null || titles.size() == 0 || !confirm) {
			model.addAttribute("titles", processRecords.getAllTitles(titleRepository));
			model.addAttribute("files", new ArrayList<File>());
			model.addAttribute("confirm", false);
			if (!confirm) {
				model.addAttribute("message", "Please confirm deletion.");
			}
			else {
				model.addAttribute("message", "Please select titles you wish to delete.");
			}
		}
		else {
			titles = titles.stream()
					.filter(title -> title != null && !title.isBlank())
					.toList();
			if (titles.isEmpty()) {
				model.addAttribute("titles", processRecords.getAllTitles(titleRepository));
				model.addAttribute("files", new ArrayList<File>());
				model.addAttribute("confirm", false);
				model.addAttribute("message", "Please select existing titles you wish to delete.");
			}
			else {
				long numberOfGivenTitles = titles.size();
				titles = processRecords.getAllTitles(titleRepository);
				model.addAttribute("titles", titles);
				model.addAttribute("files", new ArrayList<File>());
				model.addAttribute("confirm", false);
				model.addAttribute(
						"message",
						processRecords.deleteByTitles(
								titles,
								titleRepository,
								txtRepository,
								htmlRepository) +
								" of " + numberOfGivenTitles + " titles were deleted.");
			}
		}
	}

	public void publishPages(Model model) {
		model.addAttribute("titles", processRecords.getAllTitles(titleRepository));
		model.addAttribute("files", new ArrayList<File>());
		model.addAttribute("confirm", false);
		long[] publishResults = processRecords.publishHtml(titleRepository, htmlRepository, fileOperations);
		model.addAttribute("message",  publishResults[0] + " HTML files were pre-deleted, " + publishResults[1] + " were published.");
	}

	public void addFormula(String formulaName, String title, String fileName, List<String> content, Boolean editExistingPage, Model model) {
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
			model.addAttribute("message", "Wrong formula name was asked.");
		}
		else {
			content.add(formula);
			model.addAttribute("message", "Formula was appended.");
		}
		model.addAttribute("title", title);
		model.addAttribute("file_name", fileName);
		model.addAttribute("content", content);
		model.addAttribute("edit_existing_page", editExistingPage);
	}

	public void savePage(String title, String fileName, List<String> content, Boolean editExistingPage, Model model) {
		if (title == null || title.isBlank()) {
			title = "";
			model.addAttribute("message", "Please define a title.");
		}
		else if (fileName == null || fileName.isBlank()) {
			fileName = "";
			model.addAttribute("message", "Please define a file name.");
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
					model.addAttribute("message", "There is no existing page with this title.");
				}
				else {
					txtRepository.deleteById(optionalTitleEntity.get().getTxtId());
					htmlRepository.deleteById(optionalTitleEntity.get().getHtmlId());
					titleRepository.deleteById(optionalTitleEntity.get().getId());
					HtmlEntity htmlEntity = htmlRepository.save(new HtmlEntity(new ArrayList<>()));
					TxtEntity txtEntity = txtRepository.save(new TxtEntity(content));
					titleRepository.save(new TitleEntity(title, fileName, txtEntity.getId(), htmlEntity.getId()));
					model.addAttribute("message", "Source page has been overwritten.");
				}
			}
			else {
				if (optionalTitleEntity.isPresent()) {
					model.addAttribute("message", "There is an existing page with this title.");
				}
				else {
					HtmlEntity htmlEntity = htmlRepository.save(new HtmlEntity(new ArrayList<>()));
					TxtEntity txtEntity = txtRepository.save(new TxtEntity(content));
					titleRepository.save(new TitleEntity(title, fileName, txtEntity.getId(), htmlEntity.getId()));
					model.addAttribute("message", "Source page has been saved.");
				}
			}
		}
		model.addAttribute("title", title);
		model.addAttribute("file_name", fileName);
		model.addAttribute("content", content);
		model.addAttribute("edit_existing_page", editExistingPage);
	}

	public void importTxt(List<File> files, Boolean confirm, Model model) {
		if (files == null || files.isEmpty() || confirm == null || !confirm) {
			model.addAttribute("titles", processRecords.getAllTitles(titleRepository));
			model.addAttribute("files", new ArrayList<File>());
			model.addAttribute("confirm", false);
			model.addAttribute("message", "Please upload minimum one file and confirm overwriting.");
		}
		else {
			List<File> notImportedFiles = processRecords.importTxtFiles(files, titleRepository, txtRepository, htmlRepository, fileOperations, formulas, extractors);
			List<String> titles = processRecords.getAllTitles(titleRepository);
			model.addAttribute("titles", titles);
			model.addAttribute("files", new ArrayList<File>());
			model.addAttribute("confirm", false);
			model.addAttribute("message", notImportedFiles.size() + " of " + files.size() + " files were not imported.");
		}
	}

	public void generateHtml(Boolean confirm, Model model) {
		if (confirm == null || !confirm) {
			model.addAttribute("titles", processRecords.getAllTitles(titleRepository));
			model.addAttribute("files", new ArrayList<File>());
			model.addAttribute("confirm", false);
			model.addAttribute("message", "Please confirm generating pages.");
		}
		else {
			long[] messageData = processRecords.generate(titleRepository, txtRepository, htmlRepository, formulas, processPage, extractors, htmlGenerators);
			model.addAttribute("titles", processRecords.getAllTitles(titleRepository));
			model.addAttribute("files", new ArrayList<File>());
			model.addAttribute("confirm", false);
			model.addAttribute("message", messageData[0] + " pages in " + messageData[1] + " seconds has been processed.");
		}
	}
}

//TODO check all Stream operations for being parallel and its necessity.
