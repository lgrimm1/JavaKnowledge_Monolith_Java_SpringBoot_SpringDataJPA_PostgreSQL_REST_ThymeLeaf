package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Process.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;
import org.springframework.web.servlet.*;

import java.util.*;

/**
 * @see #addFormula(String, String, Payload, ProcessRecords, Formulas)
 * @see #savePage(String, Payload, TitleRepository, TxtRepository, HtmlRepository, FileOperations, ProcessRecords)
 */
public class EditingService {
	public static ModelAndView addFormula(String initialView,
										  String formulaName,
										  Payload payload,
										  ProcessRecords processRecords,
										  Formulas formulas) {
		if (payload == null) {
			Payload payload2 = new Payload(
					null,
					"",
					false,
					null,
					"THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.",
					null,
					null,
					"",
					null
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		String title = payload.getTitle();
		if (title == null || title.isBlank()) {
			title = "";
		}
		String content = payload.getContent();
		List<String> contentList = processRecords.stringToList(content);
		Boolean editExistingPage = payload.getEditExistingPage();
		if (editExistingPage == null) {
			editExistingPage = false;
		}
		List<String> formula = formulas.getFormula(formulaName);
		String message;
		if (formula.isEmpty()) {
			message = "WRONG FORMULA NAME WAS ASKED.";
		}
		else {
			contentList.addAll(formula);
			message = "FORMULA WAS APPENDED.";
		}
		Payload payload2 = new Payload(
				null,
				processRecords.listToString(contentList),
				editExistingPage,
				null,
				message,
				null,
				null,
				title,
				null
		);
		return new ModelAndView(initialView, "payload", payload2);
	}

	public static ModelAndView savePage(String initialView,
										Payload payload,
										TitleRepository titleRepository,
										TxtRepository txtRepository,
										HtmlRepository htmlRepository,
										FileOperations fileOperations,
										ProcessRecords processRecords) {
		if (payload == null) {
			Payload payload2 = new Payload(
					null,
					"",
					false,
					null,
					"THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.",
					null,
					null,
					"",
					null
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		String title = payload.getTitle();
		String content = payload.getContent();
		Boolean editExistingPage = payload.getEditExistingPage();
		if (content == null) {
			content = "";
		}
		if (editExistingPage == null) {
			editExistingPage = false;
		}
		if (title == null || title.isBlank()) {
			Payload payload2 = new Payload(
					null,
					content,
					editExistingPage,
					null,
					"PLEASE DEFINE A TITLE.",
					null,
					null,
					"",
					null
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		String message;
		Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTitle(title);
		if (editExistingPage) {
			if (optionalTitleEntity.isEmpty()) {
				message = "THERE IS NO EXISTING PAGE WITH THIS TITLE.";
			}
			else {
				txtRepository.deleteById(optionalTitleEntity.get().getTxtId());
				htmlRepository.deleteById(optionalTitleEntity.get().getHtmlId());
				titleRepository.deleteById(optionalTitleEntity.get().getId());
				HtmlEntity htmlEntity = htmlRepository.save(new HtmlEntity(new ArrayList<>(), new ArrayList<>()));
				TxtEntity txtEntity = txtRepository.save(new TxtEntity(content));
				titleRepository.save(new TitleEntity(
						title,
						fileOperations.generateFilename(title, titleRepository),
						txtEntity.getId(),
						htmlEntity.getId()));
				message = "SOURCE PAGE HAS BEEN OVERWRITTEN.";
			}
			Payload payload2 = new Payload(
					null,
					content,
					true,
					null,
					message,
					null,
					null,
					title,
					null
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		else { //editExistingPage == false
			if (optionalTitleEntity.isPresent()) {
				message = "THERE IS AN EXISTING PAGE WITH THIS TITLE.";
				Payload payload2 = new Payload(
						null,
						content,
						false,
						null,
						message,
						null,
						null,
						title,
						null
				);
				return new ModelAndView(initialView, "payload", payload2);
			}
			else {
				HtmlEntity htmlEntity = htmlRepository.save(new HtmlEntity(new ArrayList<>(), new ArrayList<>()));
				TxtEntity txtEntity = txtRepository.save(new TxtEntity(content));
				titleRepository.save(new TitleEntity(
						title,
						fileOperations.generateFilename(title, titleRepository),
						txtEntity.getId(),
						htmlEntity.getId()));
				message = "SOURCE PAGE HAS BEEN SAVED.";
			}
			Payload payload2 = new Payload(
					false,
					null,
					null,
					"",
					message,
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView("management", "payload", payload2);
		}
	}
}
