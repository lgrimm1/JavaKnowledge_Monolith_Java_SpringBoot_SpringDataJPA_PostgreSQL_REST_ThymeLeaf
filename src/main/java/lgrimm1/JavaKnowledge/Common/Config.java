package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Process.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;
import org.springframework.boot.*;
import org.springframework.context.annotation.*;

import java.util.*;

@Configuration
public class Config {

	@Bean
	CommandLineRunner titleCommandLineRunner(TitleRepository repository) {
		return args -> {
			TitleEntity entity1 = new TitleEntity("Title 1", "title_1", 1L, 1L);
			TitleEntity entity2 = new TitleEntity("Title 2", "title_2", 2L, 2L);
			TitleEntity entity3 = new TitleEntity("Title 3", "title_3", 3L, 3L);
			repository.saveAll(List.of(entity1, entity2, entity3));
		};
	}
	
	@Bean
	CommandLineRunner txtCommandLineRunner(TxtRepository repository) {
		return args -> {
			TxtEntity entity1 = new TxtEntity("Line 11\nLine 12\n\n=>TITLE 2\n");
			TxtEntity entity2 = new TxtEntity("Line 21\nLine 22\n\n=>TITLE 3\n");
			TxtEntity entity3 = new TxtEntity("Line 31\nLine 32\n\n=>TITLE 1\n");
			repository.saveAll(List.of(entity1, entity2, entity3));
		};
	}

	@Bean
	CommandLineRunner htmlCommandLineRunner(HtmlRepository repository) {
		return args -> {
			Formulas formulas = new Formulas();
			List<String> content = new ArrayList<>();
			content.add("<!DOCTYPE HTML>");
			content.add("<html xmlns:th=\"http://www.thymeleaf.org\">");
			content.add("<head>");
			content.add("<title>Title 1</title>");
			content.add("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
			content.add("<link rel=\"stylesheet\" href=\"styles.css\">");
			content.add("</head>");
			content.add("<body>");
			content.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Line 11<br>");
			content.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Line 12<br>");
			content.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<br>");
			content.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<a href=\"TITLE 2.html\">TITLE 2</a><br>");
			content.add("</body>");
			content.add("</html>");
			HtmlEntity entity1 = new HtmlEntity(content, List.of("TITLE 2"));

			content.clear();
			content.add("<!DOCTYPE HTML>");
			content.add("<html xmlns:th=\"http://www.thymeleaf.org\">");
			content.add("<head>");
			content.add("<title>Title 1</title>");
			content.add("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
			content.add("<link rel=\"stylesheet\" href=\"styles.css\">");
			content.add("</head>");
			content.add("<body>");
			content.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Line 21<br>");
			content.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Line 22<br>");
			content.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<br>");
			content.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<a href=\"TITLE 3.html\">TITLE 3</a><br>");
			content.add("</body>");
			content.add("</html>");
			HtmlEntity entity2 = new HtmlEntity(content, List.of("TITLE 3"));

			content.clear();
			content.add("<!DOCTYPE HTML>");
			content.add("<html xmlns:th=\"http://www.thymeleaf.org\">");
			content.add("<head>");
			content.add("<title>Title 1</title>");
			content.add("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
			content.add("<link rel=\"stylesheet\" href=\"styles.css\">");
			content.add("</head>");
			content.add("<body>");
			content.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Line 21<br>");
			content.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "Line 22<br>");
			content.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<br>");
			content.add(formulas.getConstant(Formulas.ConstantName.TAB_IN_SPACES) + "<a href=\"TITLE 1.html\">TITLE 1</a><br>");
			content.add("</body>");
			content.add("</html>");
			HtmlEntity entity3 = new HtmlEntity(content, List.of("TITLE 1"));
			repository.saveAll(List.of(entity1, entity2, entity3));
		};
	}
}
