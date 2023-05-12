package lgrimm1.javaknowledge.services;

import lgrimm1.javaknowledge.databasestorage.*;
import lgrimm1.javaknowledge.datamodels.*;
import lgrimm1.javaknowledge.process.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/**
 * @see #managePages()
 * @see #createSourcePage()
 * @see #editSourcePage(Payload)
 * @see #renameSourcePage(Payload)
 * @see #deletePages(Payload)
 * @see #importTxt(Payload, Stream, long[])
 * @see #generateHtml(Payload)
 */
@Service
public class ManagementService {

private final DatabaseStorageService databaseStorageService;
	private final ProcessRecords processRecords;
	private final Formulas formulas;

	@Autowired
	public ManagementService(DatabaseStorageService databaseStorageService,
							 ProcessRecords processRecords,
							 Formulas formulas) {
		this.databaseStorageService = databaseStorageService;
		this.processRecords = processRecords;
		this.formulas = formulas;
	}

	public Payload managePages() {
		return new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"",
				null,
				null,
				"",
				databaseStorageService.getAllTitles()
		);
	}

	public Payload createSourcePage() {
		return new Payload(
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
	}

	public Payload editSourcePage(Payload payload) {
		if (payload == null ||
				payload.getTitles() == null ||
				payload.getTitles().size() != 1 ||
				payload.getTitles().get(0) == null ||
				payload.getTitles().get(0).isBlank()) {
			throw new RuntimeException("PLEASE SELECT EXACTLY ONE TITLE FOR EDITING.");
		}
		Optional<TitleEntity> optionalTitleEntity = databaseStorageService.findTitleByTitle(payload.getTitles().get(0));
		if (optionalTitleEntity.isEmpty()) {
			throw new RuntimeException("PLEASE SELECT EXACTLY ONE TITLE FOR EDITING.");
		}
		long txtId = optionalTitleEntity.get().getTxtId();
		Optional<TxtEntity> optionalTxtEntity = databaseStorageService.findTxtById(txtId);
		if (optionalTxtEntity.isEmpty()) {
			throw new RuntimeException("PLEASE SELECT EXACTLY ONE TITLE FOR EDITING.");
		}
		return new Payload(
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
	}

	public Payload renameSourcePage(Payload payload) {
		if (payload == null ||
				payload.getTitles() == null ||
				payload.getTitles().size() != 1 ||
				payload.getTitle() == null ||
				payload.getTitle().isBlank()) {
			throw new RuntimeException("PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.");
		}
		Optional<TitleEntity> originalTitleEntity = databaseStorageService.findTitleByTitle(payload.getTitles().get(0));
		if (originalTitleEntity.isEmpty()) {
			throw new RuntimeException("PLEASE SELECT EXACTLY ONE EXISTING TITLE AND DEFINE A NEW TITLE.");
		}
		Optional<TitleEntity> possibleTitleEntity = databaseStorageService.findTitleByTitle(payload.getTitle());
		if (possibleTitleEntity.isPresent()) {
			throw new RuntimeException("THE GIVEN NEW TITLE ALREADY EXISTS, PLEASE DEFINE AN OTHER ONE.");
		}
		TitleEntity entity = originalTitleEntity.get();
		entity.setTitle(payload.getTitle());
/*
		entity.setFilename(fileOperations.generateFilename(payload.getTitle(), titleRepository));
*/
		databaseStorageService.saveTitle(entity);
		return new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"PAGE HAS BEEN RENAMED.",
				null,
				null,
				"",
				databaseStorageService.getAllTitles()
		);
	}

	public Payload deletePages(Payload payload) {
		if (payload == null ||
				payload.getTitles() == null ||
				payload.getTitles().size() == 0 ||
				payload.getConfirm() == null ||
				!payload.getConfirm()) {
			throw new RuntimeException("PLEASE SELECT TITLES YOU WISH TO DELETE AND CONFIRM DELETION.");
		}
		List<String> titles = payload.getTitles();
		titles = titles.stream()
				.filter(title -> title != null && !title.isBlank())
				.toList();
		if (titles.isEmpty()) {
			throw new RuntimeException("PLEASE SELECT TITLES YOU WISH TO DELETE AND CONFIRM DELETION.");
		}
		long numberOfGivenTitles = titles.size();
		String message = databaseStorageService.deleteByTitles(titles) +
				" OF " + numberOfGivenTitles + " TITLES WERE DELETED.";
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

	public Payload importTxt(Payload payload, Stream<Path> paths, long[] uploadResults) {
		if (payload == null ||
				payload.getConfirm() == null ||
				!payload.getConfirm() ||
				uploadResults == null ||
				uploadResults.length != 2 ||
				uploadResults[1] == 0) {
			throw new RuntimeException("PLEASE UPLOAD MINIMUM ONE PROPER FILE AND CONFIRM SOURCE OVERWRITING.");
		}
		List<File> uploadedFiles = paths
				.map(Path::toFile)
				.toList();
		List<File> notImportedFiles = processRecords.importTxtFiles(
				uploadedFiles);
		String message = "FILE IMPORT RESULTS: " +
				(uploadResults[0] - notImportedFiles.size()) +
				" IMPORTED, " +
				notImportedFiles.size() +
				" NOT IMPORTED, " +
				uploadResults[0] +
				" TOTAL.";
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

	public Payload generateHtml(Payload payload) {
		if (payload == null || payload.getConfirm() == null || !payload.getConfirm()) {
			throw new RuntimeException("PLEASE CONFIRM GENERATING PAGES.");
		}
		long[] messageData = processRecords.generate();
		return new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				messageData[0] + " PAGES IN " + messageData[1] + " SECONDS HAS BEEN PROCESSED.",
				null,
				null,
				"",
				databaseStorageService.getAllTitles()
		);
	}
}
