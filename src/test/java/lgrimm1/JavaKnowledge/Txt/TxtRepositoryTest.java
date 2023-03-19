package lgrimm1.JavaKnowledge.Txt;

import lgrimm1.JavaKnowledge.Title.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.jdbc.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.transaction.annotation.*;

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
		StringBuilder sb1 = new StringBuilder();
		for (String line : entity1.getContent()) {
			sb1.append(line).append("\n");
		}
		StringBuilder sb2 = new StringBuilder();
		for (String line : entity2.getContent()) {
			sb2.append(line).append("\n");
		}
		return sb1.toString().equals(sb2.toString());
	}

	@BeforeEach
	void setUp() {
		TxtEntity entity1 = new TxtEntity(new ArrayList<>(List.of("Line 11", "Line 12", "", "=>TITLE 2")));
		TxtEntity entity2 = new TxtEntity(new ArrayList<>(List.of("Line 21", "Line 22", "", "=>TITLE 3")));
		TxtEntity entity3 = new TxtEntity(new ArrayList<>(List.of("Line 31", "Line 32", "", "=>TITLE 1")));
		txtRepository.saveAll(List.of(entity1, entity2, entity3));
	}

	@Test
	void findEntityByItsPartiallyContainedText_Success() {
		TxtEntity expectedTxtEntity = new TxtEntity(new ArrayList<>(List.of("Line 21", "Line 22", "", "=>TITLE 3")));

		List<TxtEntity> list = txtRepository.findEntityByItsPartiallyContainedText("lInE 2");
		Assertions.assertEquals(1, list.size());
		Assertions.assertTrue(equalsWithoutId(expectedTxtEntity, list.get(0)));
	}

	@Test
	void findEntityByItsPartiallyContainedText_NoSuccess() {
		List<TxtEntity> list = txtRepository.findEntityByItsPartiallyContainedText("abc");
		Assertions.assertTrue(list.isEmpty());
	}
}