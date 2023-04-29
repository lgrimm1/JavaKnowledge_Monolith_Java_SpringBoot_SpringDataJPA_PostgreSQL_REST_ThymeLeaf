package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.FileStorage.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.util.*;
import org.springframework.web.multipart.*;

import java.util.*;
import java.util.stream.*;

@RestController
@ControllerAdvice
public class CommonController {

	private final CommonService commonService;
	private final FileStorageService fileStorageService;

	@Autowired
	public CommonController(CommonService commonService, FileStorageService fileStorageService) {
		this.commonService = commonService;
		this.fileStorageService = fileStorageService;
	}

	@RequestMapping("*")
	public ModelAndView getFallback(Model model) {
		model.asMap().clear();
		return commonService.getRoot("root");
	}

	@GetMapping("/")
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
		return commonService.getPage("page", payload);
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
//		return commonService.editSourcePage("source", payload.getTitles());
		return commonService.editSourcePage("source", payload);
	}

	@PostMapping("/rename")
	public ModelAndView renameSourcePage(@ModelAttribute("payload") Payload payload, Model model) {
		model.asMap().clear();
		return commonService.renameSourcePage("management", payload);
	}

	@PostMapping("/delete")
	public ModelAndView deleteSourcePages(@ModelAttribute("payload") Payload payload, Model model) {
		model.asMap().clear();
		return commonService.deletePages("management", payload);
	}

	@PostMapping("/import")
	public ModelAndView importTxt(@RequestParam("files") MultipartFile[] files,
								  @ModelAttribute("payload") Payload payload,
								  Model model) {
		model.asMap().clear();
		long[] uploadResults = fileStorageService.uploadFiles(payload, bindMultipartFileArrayToMultipartList(files));
		ModelAndView finalResults = commonService.importTxt("management", payload, fileStorageService.findAll(), uploadResults);
		fileStorageService.deleteAllFiles();
		return finalResults;
	}

	@PostMapping("/generate")
	public ModelAndView generateHtml(@ModelAttribute("payload") Payload payload, Model model) {
		model.asMap().clear();
		return commonService.generateHtml("management", payload);
	}

	@PostMapping("/add/{formulaName}")
	public ModelAndView addFormula(@PathVariable("formulaName") String formulaName,
								   @ModelAttribute("payload") Payload payload,
								   Model model) {
		model.asMap().clear();
		return commonService.addFormula("source", formulaName, payload);
	}

	@PostMapping("/save")
	public ModelAndView savePage(@ModelAttribute("payload") Payload payload, Model model) {
		model.asMap().clear();
		return commonService.savePage("source", payload);
/*
		return commonService.savePage("source",
				payload.getTitle(),
				payload.getFileName(),
				payload.getContent(),
				payload.getEditExistingPage());
*/
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ModelAndView handleMaxSizeException(MaxUploadSizeExceededException e, Model model) {
		model.asMap().clear();
		return fileStorageService.handleMaxSizeException("management");
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
