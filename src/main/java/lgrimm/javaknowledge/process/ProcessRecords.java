package lgrimm.javaknowledge.process;

import lgrimm.javaknowledge.databasestorage.*;
import lgrimm.javaknowledge.datamodels.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

/**
 * @see #importTxtFiles(List)
 * @see #generate()
 * @see #stringToList(String)
 * @see #listToString(List)
 */
@Component
public class ProcessRecords {
	
	private final DatabaseStorageService databaseStorageService;
	private final FileOperations fileOperations;
	private final Extractors extractors;
	private final ProcessPage processPage;

	@Autowired
	public ProcessRecords(DatabaseStorageService databaseStorageService,
						  FileOperations fileOperations,
						  Extractors extractors,
						  ProcessPage processPage) {
		this.databaseStorageService = databaseStorageService;
		this.fileOperations = fileOperations;
		this.extractors = extractors;
		this.processPage = processPage;
	}

	/**
	 * Returns List of not-imported files.
	 */
	public List<File> importTxtFiles(List<File> files) {
		List<File> notImportedFiles = new ArrayList<>();
		for (File file : files) {
			if (!file.exists() || !file.isFile() || !file.canRead()) {
				notImportedFiles.add(file);
			}
			else {
				List<String> txt = fileOperations.readFile(file);
				if (txt.isEmpty()) {
					notImportedFiles.add(file);
				}
				else {
					String title = extractors.extractTitle(txt);
					if (title.isEmpty()) {
						notImportedFiles.add(file);
					}
					else { //exists, file, readable, not-empty, existing title
						String content = this.listToString(txt.subList(3, txt.size()))
								.replaceAll("\r\n", "\n")
								.replaceAll("\r", "\n");
						Optional<TitleEntity> optionalTitleEntity = databaseStorageService.findTitleByTitle(title);
						if (optionalTitleEntity.isPresent()) {
							TitleEntity titleEntity = optionalTitleEntity.get();
							boolean titleEntityModified = false;
							Optional<TxtEntity> optionalTxtEntity =
									databaseStorageService.findTxtById(titleEntity.getTxtId());
							if (optionalTxtEntity.isPresent()) {
								TxtEntity txtEntity = optionalTxtEntity.get();
								txtEntity.setContent(content);
								databaseStorageService.saveTxt(txtEntity);
							}
							else {
								long txtId = databaseStorageService.saveTxt(new TxtEntity(content)).getId();
								titleEntity.setTxtId(txtId);
								titleEntityModified = true;
							}
							Optional<HtmlEntity> optionalHtmlEntity =
									databaseStorageService.findHtmlById(titleEntity.getHtmlId());
							if (optionalHtmlEntity.isEmpty()) {
								long htmlId =
										databaseStorageService.saveHtml(new HtmlEntity("", ""))
												.getId();
								titleEntity.setHtmlId(htmlId);
								titleEntityModified = true;
							}
							if (titleEntityModified) {
								databaseStorageService.saveTitle(titleEntity);
							}
						}
						else { //optionalTitleEntity.isEmpty()
							databaseStorageService.saveTitle(new TitleEntity(
									title,
									databaseStorageService.saveTxt(new TxtEntity(content)).getId(),
									databaseStorageService.saveHtml(new HtmlEntity("", "")).getId()
							));
						}
					}
				}
			}
		}
		return notImportedFiles;
	}

	/**
	 * Returns long[] where 0th element is number of found title records, 1st element is needed time in seconds.
	 */
	public long[] generate() {
		LocalTime startTime = LocalTime.now();
		List<TitleEntity> titleEntities = databaseStorageService.findAllTitles();
		for (TitleEntity titleEntity : titleEntities) {
			Optional<TxtEntity> optionalTxtEntity = databaseStorageService.findTxtById(titleEntity.getTxtId());
			if (optionalTxtEntity.isPresent()) {
				String title = titleEntity.getTitle();
				HtmlContentAndReferences contentAndReferences = processPage.processTxt(
						this.stringToList(optionalTxtEntity.get().getContent()),
						title
				);
				HtmlEntity htmlEntity;
				Optional<HtmlEntity> optionalHtmlEntity = databaseStorageService.findHtmlById(titleEntity.getHtmlId());
				if (optionalHtmlEntity.isPresent()) {
					htmlEntity = optionalHtmlEntity.get();
					htmlEntity.setContent(this.listToString(contentAndReferences.content()));
					htmlEntity.setTitleReferences(this.listToString(contentAndReferences.titles()));
					databaseStorageService.saveHtml(htmlEntity);
				}
				else {
					titleEntity.setHtmlId(
							databaseStorageService.saveHtml(new HtmlEntity(this.listToString(contentAndReferences.content()), this.listToString(contentAndReferences.titles())))
									.getId()
					);
					databaseStorageService.saveTitle(titleEntity);
				}
			}
		}
		return new long[]{
				titleEntities.size(),
				Duration.between(startTime, LocalTime.now().plusSeconds(1)).toSeconds()
		};
	}

	public List<String> stringToList(String string) {
		if (string == null || string.isBlank()) {
			return new ArrayList<>();
		}
		return Arrays.stream(string.split("\n")).collect(Collectors.toList());
	}

	public String listToString(List<String> list) {
		if (list == null || list.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String line : list) {
			if (line == null || line.isBlank()) {
				sb.append("\n");
			}
			else {
				sb.append(line).append("\n");
			}
		}
		return sb.toString();
	}
}
