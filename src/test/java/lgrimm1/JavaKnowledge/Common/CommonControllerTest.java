package lgrimm1.JavaKnowledge.Common;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.*;
import org.springframework.web.servlet.*;

import java.util.*;

@WebMvcTest(CommonController.class)
@AutoConfigureDataJpa
class CommonControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CommonService commonService;

	@Test
	void getRoot() throws Exception {
		String searchText = "text";
		Payload payload2 = new Payload(
				null,
				null,
				null,
				null,
				null,
				searchText,
				null,
				null,
				null
		);
		ModelAndView modelAndView = new ModelAndView("root", "payload", payload2);
		when(commonService.getRoot("root"))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						get("/")
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("root"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", payload2));
	}

	@Test
	void searchPages() throws Exception {
		String searchText = "text";
		Payload payload = new Payload(
				null,
				null,
				null,
				null,
				null,
				searchText,
				null,
				null,
				null
		);

		List<String> titles = List.of("Title 1", "Title 2");
		Payload payload2 = new Payload(
				null,
				null,
				null,
				null,
				null,
				searchText,
				null,
				null,
				titles
		);
		ModelAndView modelAndView = new ModelAndView("list", "payload", payload2);
		when(commonService.searchPages("list", payload))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/search")
								.flashAttr("payload", payload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("list"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", payload2));
	}

	@Test
	void getPage() throws Exception {
		String searchText = "text";
		List<String> titles = List.of("Title 1");
		Payload payload = new Payload(
				null,
				null,
				null,
				null,
				null,
				searchText,
				null,
				null,
				titles
		);

		String message = "message";
		String fileName = "title_1";
		List<String> titleReferences = List.of("TITLE 2");
		Payload payload2 = new Payload(
				null,
				null,
				null,
				null,
				message,
				null,
				fileName + ".html",
				null,
				titleReferences
		);
		ModelAndView modelAndView = new ModelAndView("page", "payload", payload2);
		when(commonService.getPage("page", payload))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/page")
								.flashAttr("payload", payload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("page"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", payload2));
	}

	@Test
	void managePages() throws Exception {
		String searchText = "text";
		Payload payload = new Payload(
				null,
				null,
				null,
				null,
				null,
				searchText,
				null,
				null,
				null
		);

		Boolean confirm = false;
		String files = "";
		String message = "message text";
		List<String> titles = List.of("Title 1", "Title 2");
		Payload payload2 = new Payload(
				confirm,
				null,
				null,
				files,
				message,
				null,
				null,
				null,
				titles
		);

		ModelAndView modelAndView = new ModelAndView("management", "payload", payload2);
		when(commonService.managePages("management"))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/management")
								.flashAttr("payload", payload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("management"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", payload2));
	}

	@Test
	void createSourcePage() throws Exception {
		Boolean confirm = false;
		String files = "";
		String message = "message text";
		List<String> titles = List.of("Title 1", "Title 2");
		Payload payload = new Payload(
				confirm,
				null,
				null,
				files,
				message,
				null,
				null,
				null,
				titles
		);

		String content = "";
		Boolean editExistingPage = false;
		String message2 = "message text";
		String title = "";
		Payload payload2 = new Payload(
				null,
				content,
				editExistingPage,
				null,
				message2,
				null,
				null,
				title,
				null
		);

		ModelAndView modelAndView = new ModelAndView("source", "payload", payload2);
		when(commonService.createSourcePage("source"))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/source/new")
								.flashAttr("payload", payload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("source"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", payload2));
	}

	@Test
	void editSourcePage() throws Exception {
		Boolean confirm = false;
		String files = "";
		String message = "message text";
		List<String> titles = List.of("Title 1", "Title 2");
		Payload payload = new Payload(
				confirm,
				null,
				null,
				files,
				message,
				null,
				null,
				null,
				titles
		);

		String content = "Line 1\nLine 2\n";
		Boolean editExistingPage = true;
		String message2 = "message text";
		String title = "Title 1";
		Payload payload2 = new Payload(
				null,
				content,
				editExistingPage,
				null,
				message2,
				null,
				null,
				title,
				null
		);

		ModelAndView modelAndView = new ModelAndView("source", "payload", payload2);
		when(commonService.editSourcePage("source", payload))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/source/edit")
								.flashAttr("payload", payload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("source"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", payload2));
	}

	@Test
	void deleteSourcePages() throws Exception {
		List<String> titles = List.of("Title 1", "Title 2");
		String files = "";
		Boolean confirm = true;
		String message = "message text";
		Payload payload = new Payload(
				confirm,
				null,
				null,
				files,
				message,
				null,
				null,
				null,
				titles
		);

		Boolean confirm2 = false;
		String message2 = "message text 2";
		List<String> titles2 = List.of("Title 3", "Title 4");
		Payload payload2 = new Payload(
				confirm2,
				null,
				null,
				files,
				message2,
				null,
				null,
				null,
				titles2
		);

		ModelAndView modelAndView = new ModelAndView("management", "payload", payload2);
		when(commonService.deletePages("management", payload))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/delete")
								.flashAttr("payload", payload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("management"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", payload2));
	}

	@Test
	void publishPages() throws Exception {
		List<String> titles = List.of("Title 1");
		String files = "";
		Boolean confirm = true;
		String message = "message text";
		Payload payload = new Payload(
				confirm,
				null,
				null,
				files,
				message,
				null,
				null,
				null,
				titles
		);

		List<String> titles2 = List.of("Title 1", "Title 2");
		Boolean confirm2 = false;
		String message2 = "message text 2";
		Payload payload2 = new Payload(
				confirm2,
				null,
				null,
				files,
				message2,
				null,
				null,
				null,
				titles2
		);

		ModelAndView modelAndView = new ModelAndView("management", "payload", payload2);
		when(commonService.publishPages("management", payload))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/publish")
								.flashAttr("payload", payload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("management"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", payload2));
	}

	@Test
	void addFormula() throws Exception {
		String title = "Title 1";
		String content = "Line 1\nLine 2\n";
		Boolean editExistingPage = true;
		String message = "message text";
		Payload payload = new Payload(
				null,
				content,
				editExistingPage,
				null,
				message,
				null,
				null,
				title,
				null
		);

		String formulaName = "formula_name";
		String message2 = "message text";
		Payload payload2 = new Payload(
				null,
				content + formulaName,
				editExistingPage,
				null,
				message2,
				null,
				null,
				title,
				null
		);

		ModelAndView modelAndView = new ModelAndView("source", "payload", payload2);
		when(commonService.addFormula("source", formulaName, payload))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/add/formula_name")
								.flashAttr("payload", payload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("source"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", payload2));
	}

	@Test
	void savePage() throws Exception {
		String title = "Title 1";
		Boolean editExistingPage = true;
		String content = "Line 1\nLine 2\n";
		String message = "message text";
		Payload payload = new Payload(
				null,
				content,
				editExistingPage,
				null,
				message,
				null,
				null,
				title,
				null
		);

		String message2 = "message text";
		Payload payload2 = new Payload(
				null,
				content,
				editExistingPage,
				null,
				message2,
				null,
				null,
				title,
				null
		);

		ModelAndView modelAndView = new ModelAndView("source", "payload", payload2);
		when(commonService.savePage("source", payload))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/save")
								.flashAttr("payload", payload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("source"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", payload2));
	}

	@Test
	void importTxt() throws Exception {
		List<String> titles = List.of("Title 1");
		String files = "file_3;file_4";
		Boolean confirm = true;
		String message = "message text";
		Payload payload = new Payload(
				confirm,
				null,
				null,
				files,
				message,
				null,
				null,
				null,
				titles
		);

		List<String> titles2 = List.of("Title 1", "Title 2", "Title 3", "Title 4");
		Boolean confirm2 = false;
		String message2 = "message text 2";
		Payload payload2 = new Payload(
				confirm2,
				null,
				null,
				"",
				message2,
				null,
				null,
				null,
				titles2
		);

		ModelAndView modelAndView = new ModelAndView("management", "payload", payload2);
		when(commonService.importTxt("management", payload))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/import")
								.flashAttr("payload", payload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("management"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", payload2));
	}

	@Test
	void generateHtml() throws Exception {
		List<String> titles = List.of("Title 1");
		String files = "";
		Boolean confirm = true;
		String message = "message text";
		Payload payload = new Payload(
				confirm,
				null,
				null,
				files,
				message,
				null,
				null,
				null,
				titles
		);

		List<String> titles2 = List.of("Title 1", "Title 2");
		Boolean confirm2 = false;
		String message2 = "message text 2";
		Payload payload2 = new Payload(
				confirm2,
				null,
				null,
				"",
				message2,
				null,
				null,
				null,
				titles2
		);

		ModelAndView modelAndView = new ModelAndView("management", "payload", payload2);
		when(commonService.generateHtml("management", payload))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/generate")
								.flashAttr("payload", payload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("management"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", payload2));
	}
}