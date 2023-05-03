package lgrimm1.javaknowledge;

import lgrimm1.javaknowledge.common.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

@SpringBootTest
class JavaKnowledgeApplicationTests {

	private final CommonController commonController;

	@Autowired
	JavaKnowledgeApplicationTests(CommonController commonController) {
		this.commonController = commonController;
	}

	@Test
	void contextLoads() {
		Assertions.assertNotNull(commonController);
	}
}