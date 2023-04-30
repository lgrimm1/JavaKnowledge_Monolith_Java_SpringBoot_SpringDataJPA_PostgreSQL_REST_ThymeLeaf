package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Process.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;
import org.springframework.web.servlet.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/**
 * @see #managePages(String, ProcessRecords, TitleRepository, Formulas)
 * @see #createSourcePage(String, Formulas)
 * @see #editSourcePage(String, Payload, ProcessRecords, TitleRepository, TxtRepository, Formulas)
 * @see #renameSourcePage(String, Payload, ProcessRecords, TitleRepository, FileOperations, Formulas)
 * @see #deletePages(String, Payload, ProcessRecords, TitleRepository, TxtRepository, HtmlRepository, Formulas)
 * @see #importTxt(String, Payload, ProcessRecords, TitleRepository, FileOperations, TxtRepository, HtmlRepository, Formulas, Extractors, Stream, long[])
 * @see #generateHtml(String, Payload, ProcessRecords, TitleRepository, TxtRepository, HtmlRepository, Formulas, ProcessPage, Extractors, HtmlGenerators)
 */
public class ManagementService {
	public static ModelAndView managePages(String initialView,
										   ProcessRecords processRecords,
										   TitleRepository titleRepository,
										   Formulas formulas) {
		Payload payload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"",
				null,
				null,
				"",
				processRecords.getAllTitles(titleRepository)
		);
		return new ModelAndView(initialView, "payload", payload);
	}

	public static ModelAndView createSourcePage(String initialView, Formulas formulas) {
		Payload payload = new Payload(
				formulas.getTitleSource(),
				null,
				"",
				false,
				"",
				null,
				null,
				"",
				null
		);
		return new ModelAndView(initialView, "payload", payload);
	}

	public static ModelAndView editSourcePage(String initialView,
											  Payload payload,
											  ProcessRecords processRecords,
											  TitleRepository titleRepository,
											  TxtRepository txtRepository,
											  Formulas formulas) {
		if (payload == null ||
				payload.getTitles() == null ||
				payload.getTitles().size() != 1 ||
				payload.getTitles().get(0) == null ||
				payload.getTitles().get(0).isBlank()) {
			Payload payload2 = new Payload(
					formulas.getTitleManagement(),
					false,
					null,
					null,
					"PLEASE SELECT EXACTLY ONE TITLE FOR EDITING.",
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView("management", "payload", payload2);
		}
		Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTitle(payload.getTitles().get(0));
		if (optionalTitleEntity.isEmpty()) {
			Payload payload2 = new Payload(
					formulas.getTitleManagement(),
					false,
					null,
					null,
					"PLEASE SELECT EXACTLY ONE TITLE FOR EDITING.",
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView("management", "payload", payload2);
		}
		long txtId = optionalTitleEntity.get().getTxtId();
		Optional<TxtEntity> optionalTxtEntity = txtRepository.findById(txtId);
		if (optionalTxtEntity.isEmpty()) {
			Payload payload2 = new Payload(
					formulas.getTitleManagement(),
					false,
					null,
					null,
					"PLEASE SELECT EXACTLY ONE TITLE FOR EDITING.",
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView("management", "payload", payload2);
		}
		Payload payload2 = new Payload(
				formulas.getTitleSource(),
				null,
				optionalTxtEntity.get().getContent(),
				true,
				"",
				null,
				null,
				optionalTitleEntity.get().getTitle(),
				null
		);
		return new ModelAndView(initialView, "payload", payload2);
	}

	public static ModelAndView renameSourcePage(String initialView,
												Payload payload,
												ProcessRecords processRecords,
												TitleRepository titleRepository,
												FileOperations fileOperations,
												Formulas formulas) {
		if (payload == null ||
				payload.getTitles() == null ||
				payload.getTitles().size() != 1 ||
				payload.getTitle() == null ||
				payload.getTitle().isBlank()) {
			Payload payload2 = new Payload(
					formulas.getTitleManagement(),
					false,
					null,
					null,
					"PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.",
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		Optional<TitleEntity> originalTitleEntity = titleRepository.findByTitle(payload.getTitles().get(0));
		if (originalTitleEntity.isEmpty()) {
			Payload payload2 = new Payload(
					formulas.getTitleManagement(),
					false,
					null,
					null,
					"PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.",
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		Optional<TitleEntity> newTitleEntity = titleRepository.findByTitle(payload.getTitle());
		if (newTitleEntity.isPresent()) {
			Payload payload2 = new Payload(
					formulas.getTitleManagement(),
					false,
					null,
					null,
					"THE GIVEN NEW TITLE ALREADY EXISTS, PLEASE DEFINE AN OTHER ONE.",
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		String fileName = fileOperations.generateFilename(payload.getTitle(), titleRepository);
		long txtId = originalTitleEntity.get().getTxtId();
		long htmlId = originalTitleEntity.get().getHtmlId();
		titleRepository.deleteById(originalTitleEntity.get().getId());
		titleRepository.save(new TitleEntity(payload.getTitle(), fileName, txtId, htmlId));
		Payload payload2 = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PAGE HAS BEEN RENAMED.",
				null,
				null,
				"",
				processRecords.getAllTitles(titleRepository)
		);
		return new ModelAndView(initialView, "payload", payload2);
	}

	public static ModelAndView deletePages(String initialView,
										   Payload payload,
										   ProcessRecords processRecords,
										   TitleRepository titleRepository,
										   TxtRepository txtRepository,
										   HtmlRepository htmlRepository,
										   Formulas formulas) {
		if (payload == null ||
				payload.getTitles() == null ||
				payload.getTitles().size() == 0 ||
				payload.getConfirm() == null ||
				!payload.getConfirm()) {
			String message;
			if (payload == null) {
				message = "PLEASE SELECT TITLES YOU WISH TO DELETE.";
			}
			else {
				message = (payload.getConfirm() == null || !payload.getConfirm()) ?
						"PLEASE CONFIRM DELETION." :
						"PLEASE SELECT TITLES YOU WISH TO DELETE.";
			}
			Payload payload2 = new Payload(
					formulas.getTitleManagement(),
					false,
					null,
					null,
					message,
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		List<String> titles = payload.getTitles();
		titles = titles.stream()
				.filter(title -> title != null && !title.isBlank())
				.toList();
		if (titles.isEmpty()) {
			Payload payload2 = new Payload(
					formulas.getTitleManagement(),
					false,
					null,
					null,
					"PLEASE SELECT EXISTING TITLES YOU WISH TO DELETE.",
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		long numberOfGivenTitles = titles.size();
		String message = processRecords.deleteByTitles(
				titles,
				titleRepository,
				txtRepository,
				htmlRepository) +
				" OF " + numberOfGivenTitles + " TITLES WERE DELETED.";
		Payload payload2 = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				message,
				null,
				null,
				"",
				processRecords.getAllTitles(titleRepository)
		);
		return new ModelAndView(initialView, "payload", payload2);
	}

	public static ModelAndView importTxt(String initialView,
										 Payload payload,
										 ProcessRecords processRecords,
										 TitleRepository titleRepository,
										 FileOperations fileOperations,
										 TxtRepository txtRepository,
										 HtmlRepository htmlRepository,
										 Formulas formulas,
										 Extractors extractors,
										 Stream<Path> paths,
										 long[] uploadResults) {
		if (payload == null ||
				payload.getConfirm() == null ||
				!payload.getConfirm() ||
				uploadResults == null ||
				uploadResults.length != 2 ||
				uploadResults[1] == 0) {
			Payload payload2 = new Payload(
					formulas.getTitleManagement(),
					false,
					null,
					null,
					"PLEASE UPLOAD MINIMUM ONE PROPER FILE AND CONFIRM SOURCE OVERWRITING.",
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		List<File> uploadedFiles = paths
				.map(Path::toFile)
				.toList();
		List<File> notImportedFiles = processRecords.importTxtFiles(
				uploadedFiles,
				titleRepository,
				txtRepository,
				htmlRepository,
				fileOperations,
				formulas,
				extractors);
		String message = "FILE IMPORT RESULTS: " +
				(uploadResults[0] - notImportedFiles.size()) +
				" IMPORTED, " +
				notImportedFiles.size() +
				" NOT IMPORTED, " +
				uploadResults[0] +
				" TOTAL.";
		Payload payload2 = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				message,
				null,
				null,
				"",
				processRecords.getAllTitles(titleRepository)
		);
		return new ModelAndView(initialView, "payload", payload2);
	}

	public static ModelAndView generateHtml(String initialView,
									Payload payload,
									ProcessRecords processRecords,
									TitleRepository titleRepository,
									TxtRepository txtRepository,
									HtmlRepository htmlRepository,
									Formulas formulas,
									ProcessPage processPage,
									Extractors extractors,
									HtmlGenerators htmlGenerators) {
		if (payload == null || payload.getConfirm() == null || !payload.getConfirm()) {
			Payload payload2 = new Payload(
					formulas.getTitleManagement(),
					false,
					null,
					null,
					"PLEASE CONFIRM GENERATING PAGES.",
					null,
					null,
					"",
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		long[] messageData = processRecords.generate(
				titleRepository,
				txtRepository,
				htmlRepository,
				formulas,
				processPage,
				extractors,
				htmlGenerators);
		Payload payload2 = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				messageData[0] + " PAGES IN " + messageData[1] + " SECONDS HAS BEEN PROCESSED.",
				null,
				null,
				"",
				processRecords.getAllTitles(titleRepository)
		);
		return new ModelAndView(initialView, "payload", payload2);
	}
}
