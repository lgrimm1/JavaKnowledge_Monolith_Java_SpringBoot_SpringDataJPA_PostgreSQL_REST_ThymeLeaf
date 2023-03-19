package lgrimm1.JavaKnowledge.Title;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.jdbc.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TitleRepositoryTest {

	@Autowired
	TitleRepository titleRepository;

	private boolean equalsWithoutId(TitleEntity entity1, TitleEntity entity2) {
		if (entity1 == null || entity2 == null) {
			return false;
		}
		return entity1.getTitle().equals(entity2.getTitle()) &&
				entity1.getFilename().equals(entity2.getFilename()) &&
				entity1.getTxtId() == entity2.getTxtId() &&
				entity1.getHtmlId() == entity2.getHtmlId();
	}

	@BeforeEach
	void setUp() {
		TitleEntity entity1 = new TitleEntity(1L, "Title 1", "title_1", 1L, 1L);
		TitleEntity entity2 = new TitleEntity(2L, "Title 2", "title_2", 2L, 2L);
		TitleEntity entity3 = new TitleEntity(3L, "Title 3", "title_3", 3L, 3L);
		titleRepository.saveAll(List.of(entity1, entity2, entity3));
	}

	@Test
	void findByTitle_Success() {
		TitleEntity expectedTitleEntity = new TitleEntity("Title 2", "title_2", 2L, 2L);

		Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTitle("Title 2");
		Assertions.assertTrue(optionalTitleEntity.isPresent());
		Assertions.assertTrue(equalsWithoutId(expectedTitleEntity, optionalTitleEntity.get()));
	}

	@Test
	void findByTitle_NoSuccess() {
		Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTitle("Title 4");
		Assertions.assertTrue(optionalTitleEntity.isEmpty());
	}

	@Test
	void findByTitleContainingAllIgnoreCase_OneReturned() {
		TitleEntity expectedTitleEntity = new TitleEntity("Title 3", "title_3", 3L, 3L);

		List<TitleEntity> list = titleRepository.findByTitleContainingAllIgnoreCase("3");
		Assertions.assertEquals(1, list.size());
		Assertions.assertTrue(equalsWithoutId(expectedTitleEntity, list.get(0)));
	}

	@Test
	void findByTitleContainingAllIgnoreCase_MoreReturned() {
		TitleEntity expectedTitleEntity1 = new TitleEntity("Title 1", "title_1", 1L, 1L);
		TitleEntity expectedTitleEntity2 = new TitleEntity("Title 2", "title_2", 2L, 2L);
		TitleEntity expectedTitleEntity3 = new TitleEntity("Title 3", "title_3", 3L, 3L);

		List<TitleEntity> list = titleRepository.findByTitleContainingAllIgnoreCase("LE");
		Assertions.assertEquals(3, list.size());
		Assertions.assertTrue(equalsWithoutId(expectedTitleEntity1, list.get(0)));
		Assertions.assertTrue(equalsWithoutId(expectedTitleEntity2, list.get(1)));
		Assertions.assertTrue(equalsWithoutId(expectedTitleEntity3, list.get(2)));
	}

	@Test
	void findByTitleContainingAllIgnoreCase_NoSuccess() {
		List<TitleEntity> list = titleRepository.findByTitleContainingAllIgnoreCase("abc");
		Assertions.assertTrue(list.isEmpty());
	}

	@Test
	void findByFilename_Success() {
		TitleEntity expectedTitleEntity = new TitleEntity("Title 2", "title_2", 2L, 2L);

		Optional<TitleEntity> optionalTitleEntity = titleRepository.findByFilename("title_2");
		Assertions.assertTrue(optionalTitleEntity.isPresent());
		Assertions.assertTrue(equalsWithoutId(expectedTitleEntity, optionalTitleEntity.get()));
	}

	@Test
	void findByFilename_NoSuccess() {
		Optional<TitleEntity> optionalTitleEntity = titleRepository.findByFilename("abc");
		Assertions.assertTrue(optionalTitleEntity.isEmpty());
	}

	@Test
	void findByTxtId_Success() {
		TitleEntity expectedTitleEntity = new TitleEntity("Title 2", "title_2", 2L, 2L);

		Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTxtId(2L);
		Assertions.assertTrue(optionalTitleEntity.isPresent());
		Assertions.assertTrue(equalsWithoutId(expectedTitleEntity, optionalTitleEntity.get()));
	}

	@Test
	void findByTxtId_NoSuccess() {
		Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTxtId(5L);
		Assertions.assertTrue(optionalTitleEntity.isEmpty());
	}
}