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
		ModelAndView modelAndView = new ModelAndView("root", "test_text", "text");
		when(commonService.getRoot("root"))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						get("/root")
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("root"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("test_text", "text"));
	}

	@Test
	void searchPages() throws Exception {
		String searchText = "text to search";
		List<String> titles = List.of("Title 1", "Title 2");
		Payload receivedPayload = new Payload(searchText);

		ModelAndView modelAndView = new ModelAndView("list");
		modelAndView.addObject("search_text", searchText);
		modelAndView.addObject("titles", titles);
		when(commonService.searchPages("list", searchText))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/search")
								.flashAttr("payload", receivedPayload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("list"))
				.andExpect(model().size(2))
				.andExpect(model().attribute("search_text", searchText))
				.andExpect(model().attribute("titles", titles));
	}

/*
	@Test
	void getPage() throws Exception {
		//TODO controller test on getPage
	}
*/

	@Test
	void managePages() throws Exception {
		String message = "message text";
		List<String> titles = List.of("Title 1", "Title 2");
		Boolean confirm = false;
		String files = "";
		ModelAndView modelAndView = new ModelAndView("management");
		modelAndView.addObject("titles", titles);
		modelAndView.addObject("files", files);
		modelAndView.addObject("confirm", confirm);
		modelAndView.addObject("message", message);
		when(commonService.managePages("management"))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/management")
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("management"))
				.andExpect(model().size(4))
				.andExpect(model().attribute("titles", titles))
				.andExpect(model().attribute("files", files))
				.andExpect(model().attribute("confirm", confirm))
				.andExpect(model().attribute("message", message));
	}

	@Test
	void createSourcePage() throws Exception {
		String title = "";
		String filename = "";
		Boolean editExistingPage = false;
		List<String> content = new ArrayList<>();
		String message = "message text";
		ModelAndView modelAndView = new ModelAndView("source");
		modelAndView.addObject("title", title);
		modelAndView.addObject("file_name", filename);
		modelAndView.addObject("edit_existing_page", editExistingPage);
		modelAndView.addObject("content", content);
		modelAndView.addObject("message", message);
		when(commonService.createSourcePage("source"))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/source/new")
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("source"))
				.andExpect(model().size(5))
				.andExpect(model().attribute("title", title))
				.andExpect(model().attribute("file_name", filename))
				.andExpect(model().attribute("edit_existing_page", editExistingPage))
				.andExpect(model().attribute("content", content))
				.andExpect(model().attribute("message", message));
	}

	@Test
	void editSourcePage() throws Exception {
		List<String> titles = List.of("Title 1");
		String files = "";
		Boolean confirm = false;
		String message = "message text";
		Payload receivedPayload = new Payload(confirm, files, message, titles);

		String title = "Title 1";
		String filename = "title_1";
		Boolean editExistingPage = true;
		List<String> content = List.of("Line 1", "Line 2");
		String message2 = "message text 2";
		ModelAndView modelAndView = new ModelAndView("source");
		modelAndView.addObject("title", title);
		modelAndView.addObject("file_name", filename);
		modelAndView.addObject("edit_existing_page", editExistingPage);
		modelAndView.addObject("content", content);
		modelAndView.addObject("message", message2);
		when(commonService.editSourcePage("source", titles))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/source/edit")
								.flashAttr("payload", receivedPayload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("source"))
				.andExpect(model().size(5))
				.andExpect(model().attribute("title", title))
				.andExpect(model().attribute("file_name", filename))
				.andExpect(model().attribute("edit_existing_page", editExistingPage))
				.andExpect(model().attribute("content", content))
				.andExpect(model().attribute("message", message2));
	}

	@Test
	void deleteSourcePages() throws Exception {
		List<String> titles = List.of("Title 1", "Title 2");
		String files = "";
		Boolean confirm = true;
		String message = "message text";
		Payload receivedPayload = new Payload(confirm, files, message, titles);

		List<String> titles2 = List.of("Title 4", "Title 3");
		String files2 = "";
		Boolean confirm2 = false;
		String message2 = "message text 2";
		ModelAndView modelAndView = new ModelAndView("management");
		modelAndView.addObject("titles", titles2);
		modelAndView.addObject("files", files2);
		modelAndView.addObject("confirm", confirm2);
		modelAndView.addObject("message", message2);
		when(commonService.deletePages("management", titles, confirm))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/delete")
								.flashAttr("payload", receivedPayload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("management"))
				.andExpect(model().size(4))
				.andExpect(model().attribute("titles", titles2))
				.andExpect(model().attribute("files", files2))
				.andExpect(model().attribute("confirm", confirm2))
				.andExpect(model().attribute("message", message2));
	}

	@Test
	void publishPages() throws Exception {
		List<String> titles = List.of("Title 1", "Title 2");
		String files = "";
		Boolean confirm = true;
		String message = "message text";
		ModelAndView modelAndView = new ModelAndView("management");
		modelAndView.addObject("titles", titles);
		modelAndView.addObject("files", files);
		modelAndView.addObject("confirm", confirm);
		modelAndView.addObject("message", message);
		when(commonService.publishPages("management"))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/publish")
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("management"))
				.andExpect(model().size(4))
				.andExpect(model().attribute("titles", titles))
				.andExpect(model().attribute("files", files))
				.andExpect(model().attribute("confirm", confirm))
				.andExpect(model().attribute("message", message));
	}

	@Test
	void addFormula() throws Exception {
		String title = "Title 1";
		String filename = "title_1";
		List<String> content = List.of("Line 1", "Line 2");
		Boolean editExistingPage = true;
		String message = "message text";
		Payload receivedPayload = new Payload(content, editExistingPage, filename, message, title);

		String title2 = "Title 1";
		String filename2 = "title_1";
		List<String> content2 = List.of("Line 1", "Line 2", "Line 3");
		Boolean editExistingPage2 = true;
		String message2 = "message text 2";
		ModelAndView modelAndView = new ModelAndView("source");
		modelAndView.addObject("title", title2);
		modelAndView.addObject("file_name", filename2);
		modelAndView.addObject("edit_existing_page", editExistingPage2);
		modelAndView.addObject("content", content2);
		modelAndView.addObject("message", message2);
		when(commonService.addFormula("source", "formula", title, filename, content, editExistingPage))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/add/formula")
								.flashAttr("payload", receivedPayload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("source"))
				.andExpect(model().size(5))
				.andExpect(model().attribute("title", title2))
				.andExpect(model().attribute("file_name", filename2))
				.andExpect(model().attribute("edit_existing_page", editExistingPage2))
				.andExpect(model().attribute("content", content2))
				.andExpect(model().attribute("message", message2));
	}

	@Test
	void savePage() throws Exception {
		String title = "Title 1";
		String filename = "title_1";
		Boolean editExistingPage = false;
		List<String> content = List.of("Line 1", "Line 2");
		String message = "message text";
		Payload receivedPayload = new Payload(content, editExistingPage, filename, message, title);

		String title2 = "Title 1";
		String filename2 = "title_1";
		List<String> content2 = List.of("Line 1", "Line 2");
		Boolean editExistingPage2 = true;
		String message2 = "message text 2";
		ModelAndView modelAndView = new ModelAndView("source");
		modelAndView.addObject("title", title2);
		modelAndView.addObject("file_name", filename2);
		modelAndView.addObject("edit_existing_page", editExistingPage2);
		modelAndView.addObject("content", content2);
		modelAndView.addObject("message", message2);
		when(commonService.savePage("source", title, filename, content, editExistingPage))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/save")
								.flashAttr("payload", receivedPayload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("source"))
				.andExpect(model().size(5))
				.andExpect(model().attribute("title", title2))
				.andExpect(model().attribute("file_name", filename2))
				.andExpect(model().attribute("edit_existing_page", editExistingPage2))
				.andExpect(model().attribute("content", content2))
				.andExpect(model().attribute("message", message2));
	}

	@Test
	void importTxt() throws Exception {
		List<String> titles = List.of("Title 1", "Title 2");
		String files = "file_3;file_4";
		Boolean confirm = true;
		String message = "message text";
		Payload receivedPayload = new Payload(confirm, files, message, titles);

		List<String> titles2 = List.of("Title 1", "Title 2", "Title 3", "Title 4");
		String files2 = "";
		Boolean confirm2 = false;
		String message2 = "message text 2";
		ModelAndView modelAndView = new ModelAndView("management");
		modelAndView.addObject("titles", titles2);
		modelAndView.addObject("files", files2);
		modelAndView.addObject("confirm", confirm2);
		modelAndView.addObject("message", message2);
		when(commonService.importTxt("management", files, confirm))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/import")
								.flashAttr("payload", receivedPayload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("management"))
				.andExpect(model().size(4))
				.andExpect(model().attribute("titles", titles2))
				.andExpect(model().attribute("files", files2))
				.andExpect(model().attribute("confirm", confirm2))
				.andExpect(model().attribute("message", message2));
	}

	@Test
	void generateHtml() throws Exception {
		List<String> titles = List.of("Title 1", "Title 2");
		String files = "";
		Boolean confirm = true;
		String message = "message text";
		Payload receivedPayload = new Payload(confirm, files, message, titles);

		List<String> titles2 = List.of("Title 1", "Title 2");
		String files2 = "";
		Boolean confirm2 = false;
		String message2 = "message text 2";
		ModelAndView modelAndView = new ModelAndView("management");
		modelAndView.addObject("titles", titles2);
		modelAndView.addObject("files", files2);
		modelAndView.addObject("confirm", confirm2);
		modelAndView.addObject("message", message2);
		when(commonService.generateHtml("management", confirm))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/generate")
								.flashAttr("payload", receivedPayload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("management"))
				.andExpect(model().size(4))
				.andExpect(model().attribute("titles", titles2))
				.andExpect(model().attribute("files", files2))
				.andExpect(model().attribute("confirm", confirm2))
				.andExpect(model().attribute("message", message2));
	}
}