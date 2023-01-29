package lgrimm1.JavaKnowledge;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Txt.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JavaKnowledgeApplicationTests {

	private final TxtController txtController;
	private final HtmlController htmlController;

	@Autowired
	JavaKnowledgeApplicationTests(TxtController txtController, HtmlController htmlController) {
		this.txtController = txtController;
		this.htmlController = htmlController;
	}

	@Test
	void contextLoads() {
		Assertions.assertNotNull(txtController);
		Assertions.assertNotNull(htmlController);
	}

}
