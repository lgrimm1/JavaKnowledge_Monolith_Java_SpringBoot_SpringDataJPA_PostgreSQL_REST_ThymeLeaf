package lgrimm1.JavaKnowledge.Process;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;

import java.io.*;
import java.time.*;
import java.util.*;

/**
 * Handles database operations.
 * @see #searchBySearchText(String, TitleRepository, TxtRepository)
 * @see #deleteByTitles(List, TitleRepository, TxtRepository, HtmlRepository)
 * @see #getAllTitles(TitleRepository)
 * @see #importTxtFiles(List, TitleRepository, TxtRepository, HtmlRepository, FileOperations, Formulas, Extractors)
 * @see #generate(TitleRepository, TxtRepository, HtmlRepository, Formulas, ProcessPage, Extractors)
 * @see #publishHtml(TitleRepository, HtmlRepository, FileOperations)
 */
public class ProcessRecords {

	public Set<String> searchBySearchText(String searchText,
										  TitleRepository titleRepository,
										  TxtRepository txtRepository) {
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

	public void deleteByTitles(List<String> titles,
							   TitleRepository titleRepository,
							   TxtRepository txtRepository,
							   HtmlRepository htmlRepository) {
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
	}

	public List<String> getAllTitles(TitleRepository titleRepository) {
		return titleRepository.findAll().stream()
				.map(TitleEntity::getTitle)
				.sorted()
				.toList();
	}

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
				List<String> txt = fileOperations.readTextFile(file);
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
						TxtEntity txtEntity = txtRepository.save(new TxtEntity(txt.subList(3, txt.size())));
						HtmlEntity htmlEntity = htmlRepository.save(new HtmlEntity(new ArrayList<>()));
						titleRepository.save(new TitleEntity(title, fileOperations.generateFilename(title), txtEntity.getId(), htmlEntity.getId()));
					}
				}
			}
		}
		return notImportedFiles;
	}

	/**
	 * Returns int[] where 0th element is number of records, 1st element is needed time in seconds.
	 */
	public long[] generate(TitleRepository titleRepository,
						   TxtRepository txtRepository,
						   HtmlRepository htmlRepository,
						   Formulas formulas,
						   ProcessPage processPage,
						   Extractors extractors) {
		LocalTime startTime = LocalTime.now();
		List<TitleEntity> titleEntities = titleRepository.findAll();
		for (TitleEntity titleEntity : titleEntities) {
			Optional<TxtEntity> optionalTxtEntity = txtRepository.findById(titleEntity.getTxtId());
			if (optionalTxtEntity.isPresent()) {
				String title = titleEntity.getTitle();
				String filename = titleEntity.getFilename();
				long txtId = optionalTxtEntity.get().getId();
				List<String> html = processPage.processTxt(
						optionalTxtEntity.get().getContent(),
						title,
						formulas.TAB_IN_SPACES,
						formulas.TAB_IN_HTML,
						formulas.SUPERLINE,
						formulas.SUBLINE,
						formulas.ROOT_HTML_NAME,
						formulas.VERSIONS,
						extractors);
				htmlRepository.deleteById(titleEntity.getHtmlId());
				long htmlId = htmlRepository.save(new HtmlEntity(html)).getId();
				titleRepository.deleteById(titleEntity.getId());
				titleRepository.save(new TitleEntity(title, filename, txtId, htmlId));
			}
		}
		return new long[]{titleEntities.size(), Duration.between(startTime, LocalTime.now()).toSeconds()};
	}

	/**
	 * Returns the number of published HTMLs.
	 */
	public long publishHtml(TitleRepository titleRepository, HtmlRepository htmlRepository, FileOperations fileOperations) {
		return titleRepository.findAll().stream()
				.filter(titleEntity -> htmlRepository.findById(titleEntity.getHtmlId()).isPresent())
				.filter(titleEntity -> fileOperations.writeHtmlFile(
						new File(fileOperations.getStaticPath() +
								fileOperations.getOSFileSeparator() +
								titleEntity.getFilename() +
								".html"),
						htmlRepository.findById(titleEntity.getHtmlId()).get().getContent()))
				.count();
	}
}
