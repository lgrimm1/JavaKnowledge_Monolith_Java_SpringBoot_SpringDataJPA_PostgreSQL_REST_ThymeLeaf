package lgrimm1.javaknowledge.txt;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.jdbc.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;

import java.util.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TxtRepositoryTest {

	@Autowired
	TxtRepository txtRepository;

	private boolean equalsWithoutId(TxtEntity entity1, TxtEntity entity2) {
		if (entity1 == null || entity2 == null) {
			return false;
		}
		return entity1.getContent().equals(entity2.getContent());
	}

	@BeforeEach
	void setUp() {
		TxtEntity entity1 = new TxtEntity("Line 11\nLine 12\n\n=>TITLE 2\n");
		TxtEntity entity2 = new TxtEntity("Line 21\nLine 22\n\n=>TITLE 3\n");
		TxtEntity entity3 = new TxtEntity("Line 31\nLine 32\n\n=>TITLE 1\n");
		txtRepository.saveAll(List.of(entity1, entity2, entity3));
	}

	@Test
	void helper_equalsWithoutId() {
		TxtEntity entity1;
		TxtEntity entity2;
		Assertions.assertFalse(this.equalsWithoutId(null, null));

		entity1 = new TxtEntity("Line 11\nLine 12\n");
		Assertions.assertFalse(this.equalsWithoutId(entity1, null));

		entity2 = new TxtEntity("Line 21\nLine 22\n");
		Assertions.assertFalse(this.equalsWithoutId(null, entity2));

		entity1 = new TxtEntity("Line 11\nLine 12\n");
		entity2 = new TxtEntity("Line 21\nLine 22\n");
		Assertions.assertFalse(this.equalsWithoutId(entity1, entity2));

		entity1 = new TxtEntity("Line 11\nLine 12\n");
		entity2 = new TxtEntity("Line 11\nLine 12\n");
		Assertions.assertTrue(this.equalsWithoutId(entity1, entity2));
	}

	@Test
	void findByContentContainingAllIgnoreCase_NoSuccess() {
		List<TxtEntity> list = txtRepository.findByContentContainingAllIgnoreCase("abc");
		Assertions.assertTrue(list.isEmpty());
	}

	@Test
	void findByContentContainingAllIgnoreCase_OneReturned() {
		TxtEntity expectedTxtEntity = new TxtEntity("Line 11\nLine 12\n\n=>TITLE 2\n");

		List<TxtEntity> list = txtRepository.findByContentContainingAllIgnoreCase("ne 1");
		Assertions.assertEquals(1, list.size());
		Assertions.assertTrue(equalsWithoutId(expectedTxtEntity, list.get(0)));
	}

	@Test
	void findByContentContainingAllIgnoreCase_MoreReturned() {
		TxtEntity expectedTxtEntity1 = new TxtEntity("Line 11\nLine 12\n\n=>TITLE 2\n");
		TxtEntity expectedTxtEntity2 = new TxtEntity("Line 21\nLine 22\n\n=>TITLE 3\n");
		TxtEntity expectedTxtEntity3 = new TxtEntity("Line 31\nLine 32\n\n=>TITLE 1\n");

		List<TxtEntity> list = txtRepository.findByContentContainingAllIgnoreCase("le");
		Assertions.assertEquals(3, list.size());
		Assertions.assertTrue(equalsWithoutId(expectedTxtEntity1, list.get(0)));
		Assertions.assertTrue(equalsWithoutId(expectedTxtEntity2, list.get(1)));
		Assertions.assertTrue(equalsWithoutId(expectedTxtEntity3, list.get(2)));
	}
}