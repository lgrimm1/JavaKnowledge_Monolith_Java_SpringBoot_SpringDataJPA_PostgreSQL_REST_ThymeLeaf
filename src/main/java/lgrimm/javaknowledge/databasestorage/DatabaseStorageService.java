package lgrimm.javaknowledge.databasestorage;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

/**
 * @see #findTitleByTitle(String)
 * @see #findAllTitles()
 * @see #getAllTitles()
 * @see #saveTitle(TitleEntity)
 * @see #findTxtById(long)
 * @see #saveTxt(TxtEntity)
 * @see #findHtmlById(long)
 * @see #saveHtml(HtmlEntity)
 * @see #findTitlesBySearchText(String)
 * @see #deleteByTitles(List)
 */
@Component
public class DatabaseStorageService {

	private final TitleRepository titleRepository;
	private final TxtRepository txtRepository;
	private final HtmlRepository htmlRepository;

	@Autowired
	public DatabaseStorageService(TitleRepository titleRepository,
								  TxtRepository txtRepository,
								  HtmlRepository htmlRepository) {
		this.titleRepository = titleRepository;
		this.txtRepository = txtRepository;
		this.htmlRepository = htmlRepository;
	}

	public Optional<TitleEntity> findTitleByTitle(String title) {
		return titleRepository.findByTitle(title);
	}

	public List<TitleEntity> findAllTitles() {
		return titleRepository.findAll();
	}

	public List<String> getAllTitles() {
		return titleRepository.findAll().stream()
				.map(TitleEntity::getTitle)
				.sorted()
				.toList();
	}

	public TitleEntity saveTitle(TitleEntity titleEntity) {
		return titleRepository.save(titleEntity);
	}

	public Optional<TxtEntity> findTxtById(long id) {
		return txtRepository.findById(id);
	}

	public TxtEntity saveTxt(TxtEntity txtEntity) {
		return txtRepository.save(txtEntity);
	}

	public Optional<HtmlEntity> findHtmlById(long id) {
		return htmlRepository.findById(id);
	}

	public HtmlEntity saveHtml(HtmlEntity htmlEntity) {
		return htmlRepository.save(htmlEntity);
	}

	public List<String> findTitlesBySearchText(String searchText) {
		if (searchText == null) {
			return new ArrayList<>();
		}
		if (searchText.isBlank()) {
			return titleRepository.findAll().stream()
					.map(TitleEntity::getTitle)
					.sorted()
					.collect(Collectors.toList());
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
		return titles.stream()
				.sorted()
				.collect(Collectors.toList());
	}

	/**
	 * Returns the number of deleted titles.
	 * Relation between title and TXT records is 1:1.
	 * Based upon testing purposes, the number of records is counted via TitleRepository and TxtRepository as well.
	 */
	public long deleteByTitles(List<String> titles) {
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
}
