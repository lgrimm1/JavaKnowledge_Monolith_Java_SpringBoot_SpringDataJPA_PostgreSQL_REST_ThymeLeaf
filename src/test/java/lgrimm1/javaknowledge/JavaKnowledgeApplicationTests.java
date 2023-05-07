package lgrimm1.javaknowledge;

import lgrimm1.javaknowledge.controller.*;
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
