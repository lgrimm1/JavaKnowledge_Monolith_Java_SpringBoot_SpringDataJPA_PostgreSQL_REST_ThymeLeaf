package lgrimm1.javaknowledge.process;

import lgrimm1.javaknowledge.databasestorage.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

/**
 * @see #importTxtFiles(List, TitleRepository, TxtRepository, HtmlRepository, FileOperations, Formulas, Extractors)
 * @see #generate(TitleRepository, TxtRepository, HtmlRepository, Formulas, ProcessPage, Extractors, HtmlGenerators)
 * @see #stringToList(String)
 * @see #listToString(List)
 */
@Component
public class ProcessRecords {
	
	private final DatabaseStorageService databaseStorageService;
	private final FileOperations fileOperations;
	private final Extractors extractors;
	private final Formulas formulas;

	@Autowired
	public ProcessRecords(DatabaseStorageService databaseStorageService, FileOperations fileOperations, Extractors extractors, Formulas formulas) {
		this.databaseStorageService = databaseStorageService;
		this.fileOperations = fileOperations;
		this.extractors = extractors;
		this.formulas = formulas;
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
					String title = extractors.extractTitle(txt, formulas);
					if (title.isEmpty()) {
						notImportedFiles.add(file);
					}
					else {
						String content = this.listToString(txt.subList(3, txt.size()))
								.replaceAll("\r\n", "\n")
								.replaceAll("\r", "\n");
						TitleEntity titleEntity;
						TxtEntity txtEntity;
						Optional<TitleEntity> optionalTitleEntity = databaseStorageService.findTitleByTitle(title);
						if (optionalTitleEntity.isPresent()) {
							titleEntity = optionalTitleEntity.get();
							Optional<TxtEntity> optionalTxtEntity = databaseStorageService.findTxtById(titleEntity.getTxtId());
							if (optionalTxtEntity.isPresent()) {
								txtEntity = optionalTxtEntity.get();
								txtEntity.setContent(content);
								databaseStorageService.saveTxt(txtEntity);
							}
							else {
								long txtId = databaseStorageService.saveTxt(new TxtEntity(content)).getId();
								titleEntity.setTxtId(txtId);
							}
/*
							txtRepository.deleteById(optionalTitleEntity.get().getTxtId());
							htmlRepository.deleteById(optionalTitleEntity.get().getHtmlId());
							titleRepository.deleteById(optionalTitleEntity.get().getId());
*/
						}
						TxtEntity txtEntity = txtRepository.save(new TxtEntity(content));
						HtmlEntity htmlEntity = htmlRepository.save(new HtmlEntity(new ArrayList<>(), new ArrayList<>()));
						titleRepository.save(new TitleEntity(
								title,
								fileOperations.generateFilename(title, titleRepository),
								txtEntity.getId(),
								htmlEntity.getId()));
					}
				}
			}
		}
		return notImportedFiles;
	}

	/**
	 * Returns long[] where 0th element is number of records, 1st element is needed time in seconds.
	 */
	public long[] generate(TitleRepository titleRepository,
						   TxtRepository txtRepository,
						   HtmlRepository htmlRepository,
						   Formulas formulas,
						   ProcessPage processPage,
						   Extractors extractors,
						   HtmlGenerators htmlGenerators) {
		LocalTime startTime = LocalTime.now();
		List<TitleEntity> titleEntities = titleRepository.findAll();
		for (TitleEntity titleEntity : titleEntities) {
			Optional<TxtEntity> optionalTxtEntity = txtRepository.findById(titleEntity.getTxtId());
			if (optionalTxtEntity.isPresent()) {
				String title = titleEntity.getTitle();
				String filename = titleEntity.getFilename();
				long txtId = optionalTxtEntity.get().getId();
				MainHtmlContentPayload payload = processPage.processTxt(
						this.stringToList(optionalTxtEntity.get().getContent()),
						title,
						titleRepository,
						formulas,
						extractors,
						htmlGenerators);
				HtmlEntity htmlEntity;
				Optional<HtmlEntity> optionalHtmlEntity = htmlRepository.findById(titleEntity.getHtmlId());
				if (optionalHtmlEntity.isPresent()) {
					htmlEntity = optionalHtmlEntity.get();
					htmlEntity.setContent(payload.content());
					htmlEntity.setTitleReferences(payload.titles());
					htmlRepository.save(htmlEntity);
				}
				else {
					titleEntity.setHtmlId(
							htmlRepository.save(new HtmlEntity(payload.content(), payload.titles())).getId()
					);
					titleRepository.save(titleEntity);
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
