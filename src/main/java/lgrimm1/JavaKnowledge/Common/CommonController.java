package lgrimm1.JavaKnowledge.Common;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import java.io.*;
import java.util.*;

@RestController
public class CommonController {

	private final CommonService commonService;

	@Autowired
	public CommonController(CommonService commonService) {
		this.commonService = commonService;
	}

	@GetMapping("/root")
	public ModelAndView getRoot() {
		return commonService.getRoot("root");
	}

	@PostMapping("/search")
	public ModelAndView searchPages(@ModelAttribute("search_text") String searchText) {
		return commonService.searchPages("list", searchText);
	}

	@PostMapping("/view")
	public ModelAndView getPage(@ModelAttribute("titles") List<String> titles) {
		return commonService.getPage("static", titles);
	}

	@PostMapping("management")
	public ModelAndView managePages(@ModelAttribute("search_text") String searchText) {
		return commonService.managePages("management");
	}

	@PostMapping("/source/new")
	public ModelAndView createSourcePage(@ModelAttribute("titles") List<String> titles,
										 @ModelAttribute("files") List<File> files,
										 @ModelAttribute("confirm") Boolean confirm,
										 @ModelAttribute("message") String message) {
		return commonService.createSourcePage("source");
	}

	@PostMapping("/source/edit")
	public ModelAndView editSourcePage(@ModelAttribute("titles") List<String> titles,
								 @ModelAttribute("files") List<File> files,
								 @ModelAttribute("confirm") Boolean confirm,
								 @ModelAttribute("message") String message) {
		return commonService.editSourcePage("source", titles);
	}

	@PostMapping("/delete")
	public ModelAndView deleteSourcePages(@ModelAttribute("titles") List<String> titles, @ModelAttribute("files") List<File> files, @ModelAttribute("confirm") Boolean confirm, @ModelAttribute("message") String message) {
		return commonService.deletePages("management", titles, confirm);
	}

	@PostMapping("/publish")
	public ModelAndView publishPages(@ModelAttribute("titles") List<String> titles, @ModelAttribute("files") List<File> files, @ModelAttribute("confirm") Boolean confirm, @ModelAttribute("message") String message) {
		return commonService.publishPages("management");
	}

	@PostMapping("/add/{formulaName}")
	public ModelAndView addFormula(@RequestParam("formula_name") String formulaName, @ModelAttribute("title") String title, @ModelAttribute("file_name") String fileName, @ModelAttribute("content") List<String> content, @ModelAttribute("edit_existing_page") Boolean editExistingPage, @ModelAttribute("message") String message) {
		return commonService.addFormula("source", formulaName, title, fileName, content, editExistingPage);
	}

	@PostMapping("/save")
	public ModelAndView savePage(@ModelAttribute("title") String title, @ModelAttribute("file_name") String fileName, @ModelAttribute("content") List<String> content, @ModelAttribute("edit_existing_page") Boolean editExistingPage, @ModelAttribute("message") String message) {
		return commonService.savePage("source", title, fileName, content, editExistingPage);
	}

	@PostMapping("/import")
	public ModelAndView importTxt(@ModelAttribute("titles") List<String> titles, @ModelAttribute("files") List<File> files, @ModelAttribute("confirm") Boolean confirm, @ModelAttribute("message") String message) {
		return commonService.importTxt("management", files, confirm);
	}

	@PostMapping("/generate")
	public ModelAndView generateHtml(@ModelAttribute("titles") List<String> titles, @ModelAttribute("files") List<File> files, @ModelAttribute("confirm") Boolean confirm, @ModelAttribute("message") String message) {
		return commonService.generateHtml("management", confirm);
	}
}
