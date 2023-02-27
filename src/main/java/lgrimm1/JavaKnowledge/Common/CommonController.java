package lgrimm1.JavaKnowledge.Common;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import java.io.*;
import java.util.*;

@Controller
//@Controller
public class CommonController {

	private final CommonService commonService;

	@Autowired
	public CommonController(CommonService commonService) {
		this.commonService = commonService;
	}

	@GetMapping("/root")
//	public String getRoot(Model model) {
	public ModelAndView getRoot(ModelAndView modelAndView) {
		return commonService.getRoot("root", modelAndView);
/*
		ModelAndView modelAndView = commonService.getRoot("root");
		model.addAllAttributes(modelAndView.getModel());
		return modelAndView.getViewName();
*/
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
	public ModelAndView publishPages(@ModelAttribute("titles") List<String> titles, @ModelAttribute("files") List<File> files, @ModelAttribute("confirm") Boolean confirm, @ModelAttribute("message") String message, Model model) {
		return commonService.publishPages(new ModelAndView("management", model.asMap()));
	}

	@PostMapping("/add/{formulaName}")
	public ModelAndView addFormula(@RequestParam("formula_name") String formulaName, @ModelAttribute("title") String title, @ModelAttribute("file_name") String fileName, @ModelAttribute("content") List<String> content, @ModelAttribute("edit_existing_page") Boolean editExistingPage, @ModelAttribute("message") String message, Model model) {
		return commonService.addFormula(formulaName, title, fileName, content, editExistingPage, new ModelAndView("source", new HashMap<>()));
	}

	@PostMapping("/save")
	public ModelAndView savePage(@ModelAttribute("title") String title, @ModelAttribute("file_name") String fileName, @ModelAttribute("content") List<String> content, @ModelAttribute("edit_existing_page") Boolean editExistingPage, @ModelAttribute("message") String message, Model model) {
		return commonService.savePage(title, fileName, content, editExistingPage, new ModelAndView("source", new HashMap<>()));
	}

	@PostMapping("/import")
	public ModelAndView importTxt(@ModelAttribute("titles") List<String> titles, @ModelAttribute("files") List<File> files, @ModelAttribute("confirm") Boolean confirm, @ModelAttribute("message") String message, Model model) {
		return commonService.importTxt(files, confirm, new ModelAndView("management", new HashMap<>()));
	}

	@PostMapping("/generate")
	public ModelAndView generateHtml(@ModelAttribute("titles") List<String> titles, @ModelAttribute("files") List<File> files, @ModelAttribute("confirm") Boolean confirm, @ModelAttribute("message") String message, Model model) {
		return commonService.generateHtml(confirm, new ModelAndView("management", model.asMap()));
	}
}
