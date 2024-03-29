package lgrimm.javaknowledge.databasestorage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

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
				entity1.getTxtId() == entity2.getTxtId() &&
				entity1.getHtmlId() == entity2.getHtmlId();
	}

	@BeforeEach
	void setUp() {
		TitleEntity entity1 = new TitleEntity(1L, "Title 1", 1L, 1L);
		TitleEntity entity2 = new TitleEntity(2L, "Title 2", 2L, 2L);
		TitleEntity entity3 = new TitleEntity(3L, "Title 3", 3L, 3L);
		titleRepository.saveAll(List.of(entity1, entity2, entity3));
	}

	@Test
	void helper_equalsWithoutId() {
		TitleEntity entity1;
		TitleEntity entity2;
		Assertions.assertFalse(this.equalsWithoutId(null, null));

		entity1 = new TitleEntity("Title 1", 1L, 11L);
		Assertions.assertFalse(this.equalsWithoutId(entity1, null));

		entity2 = new TitleEntity("Title 2", 2L, 22L);
		Assertions.assertFalse(this.equalsWithoutId(null, entity2));

		entity1 = new TitleEntity("Title 1", 1L, 11L);
		entity2 = new TitleEntity("Title 2", 2L, 22L);
		Assertions.assertFalse(this.equalsWithoutId(entity1, entity2));

		entity1 = new TitleEntity("Title 1", 1L, 11L);
		entity2 = new TitleEntity("Title 1", 2L, 22L);
		Assertions.assertFalse(this.equalsWithoutId(entity1, entity2));

		entity1 = new TitleEntity("Title 1", 1L, 11L);
		entity2 = new TitleEntity("Title 2", 2L, 22L);
		Assertions.assertFalse(this.equalsWithoutId(entity1, entity2));

		entity1 = new TitleEntity("Title 1", 1L, 11L);
		entity2 = new TitleEntity("Title 2", 1L, 22L);
		Assertions.assertFalse(this.equalsWithoutId(entity1, entity2));

		entity1 = new TitleEntity("Title 1", 1L, 11L);
		entity2 = new TitleEntity("Title 2", 2L, 11L);
		Assertions.assertFalse(this.equalsWithoutId(entity1, entity2));

		entity1 = new TitleEntity("Title 1", 1L, 11L);
		entity2 = new TitleEntity("Title 1", 1L, 11L);
		Assertions.assertTrue(this.equalsWithoutId(entity1, entity2));
	}

	@Test
	void findByTitle_NoSuccess() {
		Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTitle("Title 4");
		Assertions.assertTrue(optionalTitleEntity.isEmpty());
	}

	@Test
	void findByTitle_Success() {
		TitleEntity expectedTitleEntity = new TitleEntity("Title 2", 2L, 2L);

		Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTitle("Title 2");
		Assertions.assertTrue(optionalTitleEntity.isPresent());
		Assertions.assertTrue(equalsWithoutId(expectedTitleEntity, optionalTitleEntity.get()));
	}

	@Test
	void findByTitleContainingAllIgnoreCase_NoSuccess() {
		List<TitleEntity> list = titleRepository.findByTitleContainingAllIgnoreCase("abc");
		Assertions.assertTrue(list.isEmpty());
	}

	@Test
	void findByTitleContainingAllIgnoreCase_OneReturned() {
		TitleEntity expectedTitleEntity = new TitleEntity("Title 3", 3L, 3L);

		List<TitleEntity> list = titleRepository.findByTitleContainingAllIgnoreCase("3");
		Assertions.assertEquals(1, list.size());
		Assertions.assertTrue(equalsWithoutId(expectedTitleEntity, list.get(0)));
	}

	@Test
	void findByTitleContainingAllIgnoreCase_MoreReturned() {
		TitleEntity expectedTitleEntity1 = new TitleEntity("Title 1", 1L, 1L);
		TitleEntity expectedTitleEntity2 = new TitleEntity("Title 2", 2L, 2L);
		TitleEntity expectedTitleEntity3 = new TitleEntity("Title 3", 3L, 3L);

		List<TitleEntity> list = titleRepository.findByTitleContainingAllIgnoreCase("LE");
		Assertions.assertEquals(3, list.size());
		Assertions.assertTrue(equalsWithoutId(expectedTitleEntity1, list.get(0)));
		Assertions.assertTrue(equalsWithoutId(expectedTitleEntity2, list.get(1)));
		Assertions.assertTrue(equalsWithoutId(expectedTitleEntity3, list.get(2)));
	}

	@Test
	void findByTxtId_Success() {
		TitleEntity expectedTitleEntity = new TitleEntity("Title 2", 2L, 2L);

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