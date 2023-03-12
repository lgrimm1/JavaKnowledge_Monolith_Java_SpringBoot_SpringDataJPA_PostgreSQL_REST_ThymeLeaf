package lgrimm1.JavaKnowledge.Common;

import org.springframework.beans.factory.annotation.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import java.util.*;

@RestController
public class CommonController {

	private final CommonService commonService;

	@Autowired
	public CommonController(CommonService commonService) {
		this.commonService = commonService;
	}

	@GetMapping("/root")
	public ModelAndView getRoot(Model model) {
		model.asMap().clear();
		return commonService.getRoot("root");
	}

	@PostMapping("/search")
	public ModelAndView searchPages(@ModelAttribute("payload") Payload payload, Model model) {
		model.asMap().clear();
		return commonService.searchPages("list", payload);
	}

	@PostMapping("/page")
	public ModelAndView getPage(@ModelAttribute("payload") Payload payload, Model model) {
		model.asMap().clear();
		return commonService.getPage("page", payload.getTitles());
	}

	@PostMapping("/management")
	public ModelAndView managePages(Model model) {
		model.asMap().clear();
		return commonService.managePages("management");
	}

	@PostMapping("/source/new")
	public ModelAndView createSourcePage(Model model) {
		model.asMap().clear();
		return commonService.createSourcePage("source");
	}

	@PostMapping("/source/edit")
	public ModelAndView editSourcePage(@ModelAttribute("payload") Payload payload, Model model) {
		model.asMap().clear();
		return commonService.editSourcePage("source", payload.getTitles());
	}

	@PostMapping("/delete")
	public ModelAndView deleteSourcePages(@ModelAttribute("payload") Payload payload, Model model) {
		model.asMap().clear();
		return commonService.deletePages("management", payload.getTitles(), payload.getConfirm());
	}

	@PostMapping("/publish")
	public ModelAndView publishPages(Model model) {
		model.asMap().clear();
		return commonService.publishPages("management");
	}

	@PostMapping("/add/{formulaName}")
	public ModelAndView addFormula(@PathVariable("formulaName") String formulaName,
								   @ModelAttribute("payload") Payload payload,
								   Model model) {
		model.asMap().clear();
		return commonService.addFormula("source",
				formulaName,
				payload.getTitle(),
				payload.getFileName(),
				payload.getContent(),
				payload.getEditExistingPage());
	}

	@PostMapping("/save")
	public ModelAndView savePage(@ModelAttribute("payload") Payload payload, Model model) {
		model.asMap().clear();
		return commonService.savePage("source",
				payload.getTitle(),
				payload.getFileName(),
				payload.getContent(),
				payload.getEditExistingPage());
	}

	@PostMapping("/import")
	public ModelAndView importTxt(@ModelAttribute("payload") Payload payload, Model model) {
		model.asMap().clear();
		return commonService.importTxt("management", payload.getFiles(), payload.getConfirm());
	}

	@PostMapping("/generate")
	public ModelAndView generateHtml(@ModelAttribute("payload") Payload payload, Model model) {
		model.asMap().clear();
		return commonService.generateHtml("management", payload.getConfirm());
	}
}
