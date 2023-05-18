package lgrimm.javaknowledge.controller;

import lgrimm.javaknowledge.datamodels.*;
import lgrimm.javaknowledge.filestorage.*;
import lgrimm.javaknowledge.process.*;
import lgrimm.javaknowledge.services.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.ui.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import org.springframework.web.servlet.*;

import java.util.*;
import java.util.stream.*;

/**
 * @see #getFallback(Model)
 * @see #getRoot(Model)
 * @see #searchPages(Payload, Model)
 * @see #getPage(Payload, Model)
 * @see #managePages(Model)
 * @see #createSourcePage(Model)
 * @see #editSourcePage(Payload, Model)
 * @see #renameSourcePage(Payload, Model)
 * @see #deleteSourcePages(Payload, Model)
 * @see #importTxt(MultipartFile[], Payload, Model)
 * @see #generateHtml(Payload, Model)
 * @see #addFormula(String, Payload, Model)
 * @see #savePage(Payload, Model)
 * @see #handleMaxSizeException(MaxUploadSizeExceededException, Model)
 * @see #bindMultipartFileToMultipart(MultipartFile)
 * @see #bindMultipartFileArrayToMultipartList(MultipartFile[])
 */
@RestController
@ControllerAdvice
public class Controller {

	private final BrowsingService browsingService;
	private final EditingService editingService;
	private final ManagementService managementService;
	private final FileStorageService fileStorageService;
	private final Formulas formulas;

	@Autowired
	public Controller(BrowsingService browsingService,
					  EditingService editingService,
					  ManagementService managementService,
					  FileStorageService fileStorageService,
					  Formulas formulas) {
		this.browsingService = browsingService;
		this.editingService = editingService;
		this.managementService = managementService;
		this.fileStorageService = fileStorageService;
		this.formulas = formulas;
	}

	@RequestMapping("*")
	public ModelAndView getFallback(Model model) {
		model.asMap().clear();
		return new ModelAndView("root", "payload", browsingService.getRoot());
	}

	@GetMapping("/")
	public ModelAndView getRoot(Model model) {
		model.asMap().clear();
		return new ModelAndView("root", "payload", browsingService.getRoot());
	}

	@PostMapping("/search")
	public ModelAndView searchPages(@ModelAttribute("payload") Payload payload, Model model) {
		model.asMap().clear();
		try {
			return new ModelAndView("list", "payload", browsingService.searchPages(payload));
		}
		catch (Exception e) {
			return new ModelAndView("root", "payload", browsingService.getRoot());
		}
	}

	@PostMapping("/page")
	public ModelAndView getPage(@ModelAttribute("payload") Payload payload, Model model) {
		model.asMap().clear();
		try {
			return new ModelAndView("page", "payload", browsingService.getPage(payload));
		}
		catch (Exception e) {
			return new ModelAndView("root", "payload", browsingService.getRoot());
		}
	}

	@PostMapping("/management")
	public ModelAndView managePages(Model model) {
		model.asMap().clear();
		return new ModelAndView("management", "payload", managementService.managePages());
	}

	@PostMapping("/source/new")
	public ModelAndView createSourcePage(Model model) {
		model.asMap().clear();
		return new ModelAndView("source", "payload", managementService.createSourcePage());
	}

	@PostMapping("/source/edit")
	public ModelAndView editSourcePage(@ModelAttribute("payload") Payload payload, Model model) {
		model.asMap().clear();
		try {
			return new ModelAndView("source", "payload", managementService.editSourcePage(payload));
		}
		catch (Exception e) {
			Payload payload2 = managementService.managePages();
			payload2.setMessage(e.getMessage());
			return new ModelAndView("management", "payload", payload2);
		}
	}

	@PostMapping("/rename")
	public ModelAndView renameSourcePage(@ModelAttribute("payload") Payload payload, Model model) {
		model.asMap().clear();
		try {
			return new ModelAndView(
					"management",
					"payload",
					managementService.renameSourcePage(payload));
		}
		catch (Exception e) {
			Payload payload2 = managementService.managePages();
			payload2.setMessage(e.getMessage());
			return new ModelAndView("management", "payload", payload2);
		}
	}

	@PostMapping("/delete")
	public ModelAndView deleteSourcePages(@ModelAttribute("payload") Payload payload, Model model) {
		model.asMap().clear();
		try {
			return new ModelAndView("management", "payload", managementService.deletePages(payload));
		}
		catch (Exception e) {
			Payload payload2 = managementService.managePages();
			payload2.setMessage(e.getMessage());
			return new ModelAndView("management", "payload", payload2);
		}
	}

	@PostMapping("/import")
	public ModelAndView importTxt(@RequestParam("files") MultipartFile[] files,
								  @ModelAttribute("payload") Payload payload,
								  Model model) {
		model.asMap().clear();
		long[] uploadResults = fileStorageService.uploadFiles(payload, bindMultipartFileArrayToMultipartList(files));
		Payload payload2;
		try {
			payload2 = managementService.importTxt(payload, fileStorageService.findAll(), uploadResults);
		}
		catch (Exception e) {
			payload2 = managementService.managePages();
			payload2.setMessage(e.getMessage());
		}
		fileStorageService.deleteAllFiles();
		return new ModelAndView("management", "payload", payload2);
	}

	@PostMapping("/generate")
	public ModelAndView generateHtml(@ModelAttribute("payload") Payload payload, Model model) {
		model.asMap().clear();
		try {
			return new ModelAndView("management", "payload", managementService.generateHtml(payload));
		}
		catch (Exception e) {
			Payload payload2 = managementService.managePages();
			payload2.setMessage(e.getMessage());
			return new ModelAndView("management", "payload", payload2);
		}
	}

	@PostMapping("/add/{formulaName}")
	public ModelAndView addFormula(@PathVariable("formulaName") String formulaName,
								   @ModelAttribute("payload") Payload payload,
								   Model model) {
		model.asMap().clear();
		try {
			return new ModelAndView(
					"source",
					"payload",
					editingService.addFormula(formulaName, payload));
		}
		catch (Exception e) {
			Payload payload2 = managementService.managePages();
			payload2.setMessage(e.getMessage());
			return new ModelAndView("management", "payload", payload2);
		}
	}

	@PostMapping("/save")
	public ModelAndView savePage(@ModelAttribute("payload") Payload payload, Model model) {
		model.asMap().clear();
		try {
			Payload payload2 = editingService.savePage(payload);
			return payload2.getTemplateTitle().equalsIgnoreCase(formulas.getTitleSource()) ?
					new ModelAndView("source", "payload", payload2) :
					new ModelAndView("management", "payload", payload2);
		}
		catch (Exception e) {
			Payload payload2 = managementService.managePages();
			payload2.setMessage(e.getMessage());
			return new ModelAndView("management", "payload", payload2);
		}
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ModelAndView handleMaxSizeException(MaxUploadSizeExceededException e, Model model) {
		model.asMap().clear();
		Payload payload = fileStorageService.handleMaxSizeException();
		return new ModelAndView("management", "payload", payload);
	}

	private Multipart bindMultipartFileToMultipart(MultipartFile file) {
		try {
			return new Multipart(file.getName(),
					StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())),
					Objects.requireNonNull(file.getContentType()),
					file.getBytes());
		}
		catch (Exception e) {
			return null;
		}
	}

	private List<Multipart> bindMultipartFileArrayToMultipartList(MultipartFile[] files) {
		if (files == null) {
			return null;
		}
		return Arrays.stream(files)
				.map(this::bindMultipartFileToMultipart)
				.collect(Collectors.toList());
	}
}
