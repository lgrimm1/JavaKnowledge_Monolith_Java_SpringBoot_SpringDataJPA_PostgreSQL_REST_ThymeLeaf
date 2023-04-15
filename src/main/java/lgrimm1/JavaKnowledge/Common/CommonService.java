package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Process.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.lang.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

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

	public ModelAndView getRoot(String initialView) {
		Payload payload = new Payload(
				formulas.getTitleRoot(),
				null,
				null,
				null,
				null,
				null,
				"",
				null,
				null,
				null
		);
		return new ModelAndView(initialView, "payload", payload);
	}

	/**
	 * Finds titles which contain any word of the given text, furthermore
	 * titles of which their TXT content contains the whole given text.
	 * The search trims the given text and ignores case.
	 * In case there is no search text given, returns all titles.
	 */
	public ModelAndView searchPages(String initialView, Payload payload) {
		return BrowsingService.searchPages(initialView, payload, processRecords, titleRepository, txtRepository, formulas);
	}

	public ModelAndView getPage(String initialView, Payload payload) {
		return BrowsingService.getPage(initialView, payload, processRecords, titleRepository, htmlRepository, formulas);
	}

	public ModelAndView managePages(String initialView) {
		return ManagementService.managePages(initialView, processRecords, titleRepository, formulas);
	}

	public ModelAndView createSourcePage(String initialView) {
		return ManagementService.createSourcePage(initialView, formulas);
	}

	public ModelAndView editSourcePage(String initialView, Payload payload) {
		return ManagementService.editSourcePage(
				initialView,
				payload,
				processRecords,
				titleRepository,
				txtRepository,
				formulas);
	}

	public ModelAndView renameSourcePage(String initialView, Payload payload) {
		return ManagementService.renameSourcePage(
				initialView,
				payload,
				processRecords,
				titleRepository,
				fileOperations,
				formulas);
	}

	public ModelAndView deletePages(String initialView, Payload payload) {
		return ManagementService.deletePages(
				initialView,
				payload,
				processRecords,
				titleRepository,
				txtRepository,
				htmlRepository,
				formulas);
	}

	public ModelAndView importTxt(String initialView, Payload payload) {
		return ManagementService.importTxt(
				initialView,
				payload,
				processRecords,
				titleRepository,
				fileOperations,
				txtRepository,
				htmlRepository,
				formulas,
				extractors);
	}

	public ModelAndView generateHtml(String initialView, Payload payload) {
		return ManagementService.generateHtml(
				initialView,
				payload,
				processRecords,
				titleRepository,
				txtRepository,
				htmlRepository,
				formulas,
				processPage,
				extractors,
				htmlGenerators);
	}

	public ModelAndView addFormula(String initialView, String formulaName, Payload payload) {
		return EditingService.addFormula(
				initialView,
				formulaName,
				payload,
				processRecords,
				formulas);
	}

	public ModelAndView savePage(String initialView, Payload payload) {
		return EditingService.savePage(
				initialView,
				payload,
				titleRepository,
				txtRepository,
				htmlRepository,
				fileOperations,
				processRecords,
				formulas);
	}
}

//TODO check all Stream operations for being parallel and its necessity.
//TODO exportTxt()
