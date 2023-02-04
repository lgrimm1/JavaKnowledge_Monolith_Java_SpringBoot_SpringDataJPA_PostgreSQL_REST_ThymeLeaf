package lgrimm1.JavaKnowledge.Common;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class CommonController {

	private final CommonService commonService;

	@Autowired
	public CommonController(CommonService commonService) {
		this.commonService = commonService;
	}

	@GetMapping("/root")
	public String getRoot(Model model) {
		model = commonService.getRoot(model);
		return "root";
	}

	@PostMapping("/search")
	public String searchPages(@ModelAttribute("search_text") String searchText, Model model) {
		model = commonService.searchPages(searchText, model);
		return "list";
	}

	@PostMapping("/view")
	public String getPage(@ModelAttribute Map<String, String> attributes, Model model) {
		model = commonService.getPage(attributes, model);
		return "static";
	}

	@PostMapping("management")
	public String managePages(@ModelAttribute Map<String, String> attributes, Model model) {
		model = commonService.managePages(attributes, model);
		return "management";
	}

	@PostMapping("/source/new")
	public String createSourcePage(@ModelAttribute Map<String, String>attributes, Model model) {
		model = commonService.createSourcePage(attributes, model);
		return "source";
	}

	@PostMapping("/source/edit")
	public String editSourcePage(@ModelAttribute Map<String, String> attributes, Model model) {
		model = commonService.editSourcePage(attributes, model);
		return "source";
	}

	@PostMapping("/delete")
	public String deleteSourcePages(@ModelAttribute Map<String, String> attributes, Model model) {
		model = commonService.deletePages(attributes, model);
		return "management";
	}

	@PostMapping("/generate")
	public String generatePages(@ModelAttribute Map<String, String> attributes, Model model) {
		model = commonService.generatePages(attributes, model);
		return "management";
	}

	@PostMapping("/add/{formulaName}")
	public String addFormula(@RequestParam("formula_name") String formulaName, @ModelAttribute Map<String, String> attributes, Model model) {
		model = commonService.addFormula(formulaName, attributes, model);
		return "source";
	}

	@PostMapping("/save")
	public String savePage(@ModelAttribute Map<String, String> attributes, Model model) {
		model = commonService.savePage(attributes, model);
		return "source";
	}
}
