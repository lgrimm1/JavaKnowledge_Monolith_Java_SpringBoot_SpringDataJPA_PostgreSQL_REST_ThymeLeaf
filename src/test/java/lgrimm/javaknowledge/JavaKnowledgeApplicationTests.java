package lgrimm.javaknowledge;

import lgrimm.javaknowledge.controller.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

@SpringBootTest
class JavaKnowledgeApplicationTests {

	private final Controller controller;

	@Autowired
	JavaKnowledgeApplicationTests(Controller controller) {
		this.controller = controller;
	}

	@Test
	void contextLoads() {
		Assertions.assertNotNull(controller);
	}
}
