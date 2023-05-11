package lgrimm1.javaknowledge.process;

import lgrimm1.javaknowledge.databasestorage.*;
import lgrimm1.javaknowledge.datamodels.HtmlContentAndReferences;
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
	public ProcessRecords(DatabaseStorageService databaseStorageService, FileOperations fileOperations, Extractors extractors, ProcessPage processPage) {
		this.databaseStorageService = databaseStorageService;
		this.fileOperations = fileOperations;
		this.extractors = extractors;
		this.processPage = processPage;
	}

/*
	public List<String> getAllTitles(TitleRepository titleRepository) {
		return titleRepository.findAll().stream()
				.map(TitleEntity::getTitle)
				.sorted()
				.toList();
	}
*/

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
					else {
						String content = this.listToString(txt.subList(3, txt.size()))
								.replaceAll("\r\n", "\n")
								.replaceAll("\r", "\n");
						Optional<TitleEntity> optionalTitleEntity = databaseStorageService.findTitleByTitle(title);
						if (optionalTitleEntity.isPresent()) {
							TitleEntity titleEntity = optionalTitleEntity.get();
							boolean titleEntityModified = false;
							Optional<TxtEntity> optionalTxtEntity = databaseStorageService.findTxtById(titleEntity.getTxtId());
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
							Optional<HtmlEntity> optionalHtmlEntity = databaseStorageService.findHtmlById(titleEntity.getHtmlId());
							if (optionalHtmlEntity.isEmpty()) {
								long htmlId = databaseStorageService.saveHtml(new HtmlEntity(new ArrayList<>(), new ArrayList<>())).getId();
								titleEntity.setHtmlId(htmlId);
								titleEntityModified = true;
							}
							if (titleEntityModified) {
								databaseStorageService.saveTitle(titleEntity);
							}
/*
							txtRepository.deleteById(optionalTitleEntity.get().getTxtId());
							htmlRepository.deleteById(optionalTitleEntity.get().getHtmlId());
							titleRepository.deleteById(optionalTitleEntity.get().getId());
*/
						}
						else { //optionalTitleEntity.isEmpty()
							databaseStorageService.saveTitle(new TitleEntity(
									title,
									"",
									databaseStorageService.saveTxt(new TxtEntity(content)).getId(),
									databaseStorageService.saveHtml(new HtmlEntity(new ArrayList<>(), new ArrayList<>())).getId()
							));
						}
/*
						TxtEntity txtEntity = txtRepository.save(new TxtEntity(content));
						HtmlEntity htmlEntity = htmlRepository.save(new HtmlEntity(new ArrayList<>(), new ArrayList<>()));
						titleRepository.save(new TitleEntity(
								title,
								fileOperations.generateFilename(title, titleRepository),
								txtEntity.getId(),
								htmlEntity.getId()));
*/
					}
				}
			}
		}
		return notImportedFiles;
	}

	/**
	 * Returns long[] where 0th element is number of records, 1st element is needed time in seconds.
	 */
	public long[] generate() {
		LocalTime startTime = LocalTime.now();
		List<TitleEntity> titleEntities = databaseStorageService.findAllTitles();
		for (TitleEntity titleEntity : titleEntities) {
			Optional<TxtEntity> optionalTxtEntity = databaseStorageService.findTxtById(titleEntity.getTxtId());
			if (optionalTxtEntity.isPresent()) {
				String title = titleEntity.getTitle();
/*
				String filename = titleEntity.getFilename();
				long txtId = optionalTxtEntity.get().getId();
*/
				HtmlContentAndReferences payload = processPage.processTxt(
						this.stringToList(optionalTxtEntity.get().getContent()),
						title
				);
				HtmlEntity htmlEntity;
				Optional<HtmlEntity> optionalHtmlEntity = databaseStorageService.findHtmlById(titleEntity.getHtmlId());
				if (optionalHtmlEntity.isPresent()) {
					htmlEntity = optionalHtmlEntity.get();
					htmlEntity.setContent(payload.content());
					htmlEntity.setTitleReferences(payload.titles());
					databaseStorageService.saveHtml(htmlEntity);
				}
				else {
					titleEntity.setHtmlId(
							databaseStorageService.saveHtml(new HtmlEntity(payload.content(), payload.titles()))
									.getId()
					);
					databaseStorageService.saveTitle(titleEntity);
				}
/*
				htmlRepository.deleteById(titleEntity.getHtmlId());
				long htmlId = htmlRepository.save(new HtmlEntity(payload.content(), payload.titles())).getId();
				titleRepository.deleteById(titleEntity.getId());
				titleRepository.save(new TitleEntity(title, filename, txtId, htmlId));
*/
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
