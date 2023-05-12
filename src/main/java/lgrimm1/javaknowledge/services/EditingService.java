package lgrimm1.javaknowledge.services;

import lgrimm1.javaknowledge.databasestorage.*;
import lgrimm1.javaknowledge.datamodels.*;
import lgrimm1.javaknowledge.process.Formulas;
import lgrimm1.javaknowledge.process.ProcessRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @see #addFormula(String, Payload)
 * @see #savePage(Payload)
 */
@Service
public class EditingService {

private final DatabaseStorageService databaseStorageService;
	private final ProcessRecords processRecords;
	private final Formulas formulas;

	@Autowired
	public EditingService(DatabaseStorageService databaseStorageService,
						  ProcessRecords processRecords,
						  Formulas formulas) {
		this.databaseStorageService = databaseStorageService;
		this.processRecords = processRecords;
		this.formulas = formulas;
	}

	public Payload addFormula(String formulaName, Payload payload) {
		if (payload == null || formulaName == null) {
			throw new RuntimeException("THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.");
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
		return new Payload(
				formulas.getTitleSource(),
				null,
				processRecords.listToString(contentList),
				editExistingPage,
				message,
				null,
				null,
				title,
				null
		);
	}

	public Payload savePage(Payload payload) {
		if (payload == null) {
			throw new RuntimeException("THERE WAS A COMMUNICATION ERROR BETWEEN THE BROWSER AND THE SERVER.");
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
			return new Payload(
					formulas.getTitleSource(),
					null,
					content,
					editExistingPage,
					"PLEASE DEFINE A TITLE.",
					null,
					null,
					"",
					null
			);
		}
		String message;
		Optional<TitleEntity> optionalTitleEntity = databaseStorageService.findTitleByTitle(title);
		if (editExistingPage) {
			if (optionalTitleEntity.isEmpty()) {
				message = "THERE IS NO EXISTING PAGE WITH THIS TITLE.";
			}
			else {
				content = content
						.replaceAll("\r\n", "\n")
						.replaceAll("\r", "\n");
				Optional<TxtEntity> optionalTxtEntity =
						databaseStorageService.findTxtById(optionalTitleEntity.get().getTxtId());
				if (optionalTxtEntity.isPresent()) {
					TxtEntity txtEntity = optionalTxtEntity.get();
					txtEntity.setContent(content);
					databaseStorageService.saveTxt(txtEntity);
				}
				else {
					long txtId = databaseStorageService.saveTxt(new TxtEntity(content)).getId();
					TitleEntity titleEntity = optionalTitleEntity.get();
					titleEntity.setTxtId(txtId);
					databaseStorageService.saveTitle(titleEntity);
				}
				message = "SOURCE PAGE HAS BEEN OVERWRITTEN.";
			}
			return new Payload(
					formulas.getTitleSource(),
					null,
					content,
					true,
					message,
					null,
					null,
					title,
					null
			);
		}
		else { //editExistingPage == false
			if (optionalTitleEntity.isPresent()) {
				message = "THERE IS AN EXISTING PAGE WITH THIS TITLE.";
				return new Payload(
						formulas.getTitleSource(),
						null,
						content,
						false,
						message,
						null,
						null,
						title,
						null
				);
			}
			else {
				content = content
						.replaceAll("\r\n", "\n")
						.replaceAll("\r", "\n");
				long htmlId = databaseStorageService.saveHtml(new HtmlEntity(
						new ArrayList<>(),
						new ArrayList<>())).getId();
				long txtId = databaseStorageService.saveTxt(new TxtEntity(content)).getId();
				databaseStorageService.saveTitle(new TitleEntity(
						title,
						"",
						txtId,
						htmlId));
				message = "SOURCE PAGE HAS BEEN SAVED.";
			}
			return new Payload(
					formulas.getTitleManagement(),
					false,
					null,
					null,
					message,
					null,
					null,
					"",
					databaseStorageService.getAllTitles()
			);
		}
	}
}
