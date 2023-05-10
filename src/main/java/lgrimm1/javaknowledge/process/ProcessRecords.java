package lgrimm1.javaknowledge.process;

import lgrimm1.javaknowledge.html.*;
import lgrimm1.javaknowledge.title.*;
import lgrimm1.javaknowledge.txt.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

/**
 * @see #searchBySearchText(String, TitleRepository, TxtRepository)
 * @see #deleteByTitles(List, TitleRepository, TxtRepository, HtmlRepository)
 * @see #getAllTitles(TitleRepository)
 * @see #importTxtFiles(List, TitleRepository, TxtRepository, HtmlRepository, FileOperations, Formulas, Extractors)
 * @see #generate(TitleRepository, TxtRepository, HtmlRepository, Formulas, ProcessPage, Extractors, HtmlGenerators)
 * @see #stringToList(String)
 * @see #listToString(List)
 */
@Component
public class ProcessRecords {

	public Set<String> searchBySearchText(String searchText,
										  TitleRepository titleRepository,
										  TxtRepository txtRepository) {
		if (searchText == null) {
			return new HashSet<>();
		}
		if (searchText.isBlank()) {
			return titleRepository.findAll().stream()
					.map(TitleEntity::getTitle)
					.collect(Collectors.toSet());
		}
		searchText = searchText.trim();
		Set<String> titles = new HashSet<>();
		List<String> words = new ArrayList<>(List.of(searchText.split(" ")));
		words = words.stream()
				.filter(word -> !word.trim().isBlank())
				.toList();
		for (String word : words) {
			titles.addAll(
					titleRepository.findByTitleContainingAllIgnoreCase(word).stream()
							.map(TitleEntity::getTitle)
							.toList()
			);
		}
		titles.addAll(
				txtRepository.findByContentContainingAllIgnoreCase(searchText).stream()
						.map(TxtEntity::getId)
						.map(titleRepository::findByTxtId)
						.filter(Optional::isPresent)
						.map(optional -> optional.get().getTitle())
						.toList()
		);
		return titles;
	}

	/**
	 * Returns the number of deleted titles.
	 * Relation between title and TXT records is 1:1.
	 * Based upon testing purposes, the number of records is counted via TitleRepository and TxtRepository as well.
	 */
	public long deleteByTitles(List<String> titles,
							   TitleRepository titleRepository,
							   TxtRepository txtRepository,
							   HtmlRepository htmlRepository) {
		if (titles == null || titles.isEmpty()) {
			return 0;
		}
		long originalCount = titleRepository.count();
		List<TitleEntity> titleEntities = titles.stream()
				.map(titleRepository::findByTitle)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.toList();
		List<Long> ids = titleEntities.stream()
				.map(TitleEntity::getTxtId)
				.toList();
		txtRepository.deleteAllById(ids);
		ids = titleEntities.stream()
				.map(TitleEntity::getHtmlId)
				.toList();
		htmlRepository.deleteAllById(ids);
		ids = titleEntities.stream()
				.map(TitleEntity::getId)
				.toList();
		titleRepository.deleteAllById(ids);
		return originalCount - txtRepository.count();
	}

	public List<String> getAllTitles(TitleRepository titleRepository) {
		return titleRepository.findAll().stream()
				.map(TitleEntity::getTitle)
				.sorted()
				.toList();
	}

	/**
	 * Returns List of not-imported files.
	 */
	public List<File> importTxtFiles(List<File> files,
									 TitleRepository titleRepository,
									 TxtRepository txtRepository,
									 HtmlRepository htmlRepository,
									 FileOperations fileOperations,
									 Formulas formulas,
									 Extractors extractors) {
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
						Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTitle(title);
						if (optionalTitleEntity.isPresent()) {
							txtRepository.deleteById(optionalTitleEntity.get().getTxtId());
							htmlRepository.deleteById(optionalTitleEntity.get().getHtmlId());
							titleRepository.deleteById(optionalTitleEntity.get().getId());
						}
						String content = this.listToString(txt.subList(3, txt.size()))
								.replaceAll("\r\n", "\n")
								.replaceAll("\r", "\n");
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
				htmlRepository.deleteById(titleEntity.getHtmlId());
				long htmlId = htmlRepository.save(new HtmlEntity(payload.content(), payload.titles())).getId();
				titleRepository.deleteById(titleEntity.getId());
				titleRepository.save(new TitleEntity(title, filename, txtId, htmlId));
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
