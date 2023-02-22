package lgrimm1.JavaKnowledge.Common;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import java.io.*;
import java.util.*;

@Controller
public class CommonController {

	private final CommonService commonService;

	@Autowired
	public CommonController(CommonService commonService) {
		this.commonService = commonService;
	}

	@GetMapping("/root")
	public ModelAndView getRoot(Model model) {
		return commonService.getRoot(new ModelAndView("root", model.asMap()));
	}

	@PostMapping("/search")
	public ModelAndView searchPages(@ModelAttribute("search_text") String searchText, Model model) {
		return commonService.searchPages(searchText, new ModelAndView("list", model.asMap()));
	}

	@PostMapping("/view")
	public ModelAndView getPage(@ModelAttribute("titles") List<String> titles, Model model) {
		return commonService.getPage(titles, new ModelAndView("static", model.asMap()));
	}

	@PostMapping("management")
	public ModelAndView managePages(@ModelAttribute("search_text") String searchText, Model model) {
		return commonService.managePages(new ModelAndView("management", model.asMap()));
	}

	@PostMapping("/source/new")
	public ModelAndView createSourcePage(@ModelAttribute("titles") List<String> titles,
										 @ModelAttribute("files") List<File> files,
										 @ModelAttribute("confirm") Boolean confirm,
										 @ModelAttribute("message") String message,
										 Model model) {
		return commonService.createSourcePage(new ModelAndView("source", model.asMap()));
	}

	@PostMapping("/source/edit")
	public ModelAndView editSourcePage(@ModelAttribute("titles") List<String> titles,
								 @ModelAttribute("files") List<File> files,
								 @ModelAttribute("confirm") Boolean confirm,
								 @ModelAttribute("message") String message,
								 Model model) {
		return commonService.editSourcePage(titles, new ModelAndView("source", model.asMap()));
	}

	@PostMapping("/delete")
	public ModelAndView deleteSourcePages(@ModelAttribute("titles") List<String> titles, @ModelAttribute("files") List<File> files, @ModelAttribute("confirm") Boolean confirm, @ModelAttribute("message") String message, Model model) {
		return commonService.deletePages(titles, confirm, new ModelAndView("management", model.asMap()));
	}

	@PostMapping("/publish")
	public String publishPages(@ModelAttribute("titles") List<String> titles, @ModelAttribute("files") List<File> files, @ModelAttribute("confirm") Boolean confirm, @ModelAttribute("message") String message, Model model) {
		commonService.publishPages(model);
		return "management";
	}

	@PostMapping("/add/{formulaName}")
	public String addFormula(@RequestParam("formula_name") String formulaName, @ModelAttribute("title") String title, @ModelAttribute("file_name") String fileName, @ModelAttribute("content") List<String> content, @ModelAttribute("edit_existing_page") Boolean editExistingPage, @ModelAttribute("message") String message, Model model) {
		commonService.addFormula(formulaName, title, fileName, content, editExistingPage, model);
		return "source";
	}

	@PostMapping("/save")
	public String savePage(@ModelAttribute("title") String title, @ModelAttribute("file_name") String fileName, @ModelAttribute("content") List<String> content, @ModelAttribute("edit_existing_page") Boolean editExistingPage, @ModelAttribute("message") String message, Model model) {
		commonService.savePage(title, fileName, content, editExistingPage, model);
		return "source";
	}

	@PostMapping("/import")
	public String importTxt(@ModelAttribute("titles") List<String> titles, @ModelAttribute("files") List<File> files, @ModelAttribute("confirm") Boolean confirm, @ModelAttribute("message") String message, Model model) {
		commonService.importTxt(files, confirm, model);
		return "management";
	}

	@PostMapping("/generate")
	public String generateHtml(@ModelAttribute("titles") List<String> titles, @ModelAttribute("files") List<File> files, @ModelAttribute("confirm") Boolean confirm, @ModelAttribute("message") String message, Model model) {
		commonService.generateHtml(confirm, model);
		return "management";
	}
}
