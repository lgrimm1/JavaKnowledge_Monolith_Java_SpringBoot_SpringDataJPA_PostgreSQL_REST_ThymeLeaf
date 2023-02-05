package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Service
public class CommonService {

	private final TitleRepository titleRepository;
	private final TxtRepository txtRepository;
	private final HtmlRepository htmlRepository;

	@Autowired
	public CommonService(TitleRepository titleRepository, TxtRepository txtRepository, HtmlRepository htmlRepository) {
		this.titleRepository = titleRepository;
		this.txtRepository = txtRepository;
		this.htmlRepository = htmlRepository;
	}

	public void getRoot(Model model) {
		model.addAttribute("search_text", "");
	}

	/**
	 * Finds titles which contain any word of the given text, furthermore titles of which their TXT content contains the whole given text.
	 * The search trims the given text and ignores case.
	 * In case there is no search text given, returns all titles.
	 */
	public void searchPages(String searchText, Model model) {
		if (searchText == null || searchText.isBlank()) {
			addAllTitlesToModel(model);
		}
		else {
			searchText = searchText.trim();
			Set<String> titles = new HashSet<>();
			List<String> words = new ArrayList<>(List.of(searchText.split(" ")));
			words = words.stream()
					.filter(word -> !word.trim().isBlank())
					.toList();
			for (String word : words) {
				titles.addAll(
						titleRepository.findByTitleContainingAllIgnoreCase(word).stream()
								.map(TitleEntity::getTitle)
								.toList()
				);
			}
			titles.addAll(
					titleRepository.findAllById(
							txtRepository.findByContentContainingAllIgnoreCase(searchText).stream()
									.map(TxtEntity::getTitleId)
									.toList()
					).stream()
							.map(TitleEntity::getTitle)
							.toList()
			);
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
		addAllTitlesToModel(model);
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
			addAllTitlesToModel(model);
			model.addAttribute("confirm", false);
			model.addAttribute("message", "Please select exactly one title for editing.");
		}
		else {
			Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTitle(titles.get(0));
			if (optionalTitleEntity.isEmpty()) {
				addAllTitlesToModel(model);
				model.addAttribute("confirm", false);
				model.addAttribute("message", "Please select exactly one title for editing.");
			}
			else {
				long txtId = optionalTitleEntity.get().getTxtId();
				Optional<TxtEntity> optionalTxtEntity = txtRepository.findByTitleId(txtId);
				if (optionalTxtEntity.isEmpty()) {
					addAllTitlesToModel(model);
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
			addAllTitlesToModel(model);
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
				addAllTitlesToModel(model);
				model.addAttribute("confirm", false);
				model.addAttribute("message", "Please select existing titles you wish to delete.");
			}
			else {
				long originalNumberOfTitles = titleRepository.count();
				long numberOfGivenTitles = titles.size();
				List<TitleEntity> titleEntities = titles.stream()
						.map(titleRepository::findByTitle)
						.filter(Optional::isPresent)
						.map(Optional::get)
						.toList();
				List<Long> ids = titleEntities.stream()
						.map(TitleEntity::getTxtId)
						.toList();
				txtRepository.deleteAllById(ids);
				ids = titleEntities.stream()
						.map(TitleEntity::getHtmlId)
						.toList();
				htmlRepository.deleteAllById(ids);
				ids = titleEntities.stream()
						.map(TitleEntity::getId)
						.toList();
				titleRepository.deleteAllById(ids);
				titles = titleRepository.findAll().stream()
						.map(TitleEntity::getTitle)
						.sorted()
						.toList();
				model.addAttribute("titles", titles);
				model.addAttribute("confirm", false);
				model.addAttribute("message", String.valueOf(originalNumberOfTitles - titles.size()) + " of " + numberOfGivenTitles + " titles were deleted.");
			}
		}
	}

	public void generatePages(Model model) {
		//TODO generate
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
		if (formulaName == null || formulaName.isBlank()) {
			model.addAttribute("message", "Wrong formula name was asked.");
		}
		else if (formulaName.equals("header1")) {
			content.add("=".repeat(81));
			content.add("<X. HEADER 1 TEXT>");
			content.add("=".repeat(81));
			model.addAttribute("message", "Header 1 formula was appended.");
		}
		else if (formulaName.equals("header2")) {
			content.add("<X.Y. HEADER 2 TEXT>");
			content.add("-".repeat(81));
			model.addAttribute("message", "Header 2 formula was appended.");
		}
		else if (formulaName.equals("table")) {
			content.add("||Header 1|Header 2|...||");
			content.add("||Cell 11|Cell 12|...||");
			content.add("||Cell 21|Cell 22|...||");
			content.add("||...|...|...||");
			model.addAttribute("message", "Table formula was appended.");
		}
		else if (formulaName.equals("example")) {
			content.add("EXAMPLE FOR <TEXT>");
			content.add("<code>");
			content.add("END OF EXAMPLE");
			model.addAttribute("message", "Example formula was appended.");
		}
		else if (formulaName.equals("reference")) {
			content.add("=><Page_file_name>[;HEADER]");
			model.addAttribute("message", "Reference formula was appended.");
		}
		else {
			model.addAttribute("message", "Wrong formula name was asked.");
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
					//TODO overwrite existing content
					//TODO overwrite existing file
					model.addAttribute("message", "Source page has been saved.");
				}
			}
			else {
				//TODO save content
				//TODO save file
			}
		}
		model.addAttribute("title", title);
		model.addAttribute("file_name", fileName);
		model.addAttribute("content", content);
		model.addAttribute("edit_existing_page", editExistingPage);
	}

	private void addAllTitlesToModel(Model model) {
		model.addAttribute(
				"titles",
				titleRepository.findAll().stream()
						.map(TitleEntity::getTitle)
						.sorted()
						.toList()
		);
	}
}
