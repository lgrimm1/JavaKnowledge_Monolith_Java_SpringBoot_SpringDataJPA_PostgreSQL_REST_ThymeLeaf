package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;

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

	public Model getRoot(Model model) {
		model.addAttribute("search_text", "");
		return model;
	}

	/**
	 * Finds titles which contain any word of the given text, furthermore titles of which their TXT content contains the whole given text.
	 * The search trims the given text and ignores case.
	 */
	public Model searchPages(String searchText, Model model) {
		if (searchText == null || searchText.isBlank()) {
			model.addAttribute(
					"titles",
					titleRepository.findAll().stream()
							.map(TitleEntity::getTitle)
							.sorted()
							.toList()
			);
		}
		else {
			//filter titles by search words and contents by text
			searchText = searchText.trim();
			Set<String> titles = new HashSet<>();
			List<String> words = new ArrayList<>(List.of(searchText.split(" ")));
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
		return model;
	}

	public Model getPage(Map<String, String> attributes, Model model) {
		//TODO getPage()
		return model;
	}

	public Model managePages(Map<String, String> attributes, Model model) {
		model.addAttribute(
				"titles",
				titleRepository.findAll().stream()
						.map(TitleEntity::getTitle)
						.sorted()
						.toList()
		);
		model.addAttribute("title_to_edit", "");
		model.addAttribute("message", "");
		return model;
	}

	public Model createSourcePage(Map<String, String> attributes, Model model) {
		return model;
	}

	public Model editSourcePage(Map<String, String> attributes, Model model) {
		return model;
	}

	public Model deletePages(Map<String, String> attributes, Model model) {
		return model;
	}

	public Model generatePages(Map<String, String> attributes, Model model) {
		return model;
	}

	public Model addFormula(String formulaName, Map<String, String> attributes, Model model) {
		return model;
	}

	public Model savePage(Map<String, String> attributes, Model model) {
		return model;
	}
}
