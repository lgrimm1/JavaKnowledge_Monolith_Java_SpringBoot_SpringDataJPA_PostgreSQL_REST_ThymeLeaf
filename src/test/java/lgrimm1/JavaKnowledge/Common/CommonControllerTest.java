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
		Payload sentPayload = new Payload("");
		ModelAndView modelAndView = new ModelAndView("root", "payload", sentPayload);
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
				.andExpect(model().attribute("payload", sentPayload));
	}

	@Test
	void searchPages() throws Exception {
		String searchText = "text to search";
		List<String> titles = List.of("Title 1", "Title 2");
		Payload receivedPayload = new Payload(searchText);
		Payload sentPayload = new Payload(searchText, titles);
		ModelAndView modelAndView = new ModelAndView("list", "payload", sentPayload);
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
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", sentPayload));
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
		Payload receivedPayload = new Payload(titles, message);
		Boolean confirm = false;
		String files = "";
		Payload sentPayload = new Payload(confirm, files, message, titles);
		ModelAndView modelAndView = new ModelAndView("management", "payload", sentPayload);
		when(commonService.managePages("management"))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/management")
								.flashAttr("payload", receivedPayload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("management"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", sentPayload));
	}

	@Test
	void createSourcePage() throws Exception {
		List<String> titles = List.of("Title 1", "Title 2");
		String files = "";
		Boolean confirm = false;
		String message = "message text";
		Payload receivedPayload = new Payload(confirm, files, message, titles);
		String title = "";
		String filename = "";
		Boolean editExistingPage = false;
		List<String> content = new ArrayList<>();
		String message2 = "message text 2";
		Payload sentPayload = new Payload(content, editExistingPage, filename, message2, title);
		ModelAndView modelAndView = new ModelAndView("source", "payload", sentPayload);
		when(commonService.createSourcePage("source"))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/source/new")
								.flashAttr("payload", receivedPayload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("source"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", sentPayload));
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
		Payload sentPayload = new Payload(content, editExistingPage, filename, message2, title);
		ModelAndView modelAndView = new ModelAndView("source", "payload", sentPayload);
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
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", sentPayload));
	}

	@Test
	void deleteSourcePages() throws Exception {
		List<String> titles = List.of("Title 1", "Title 2");
		String files = "";
		Boolean confirm = true;
		String message = "message text";
		Payload receivedPayload = new Payload(confirm, files, message, titles);
		List<String> titles2 = List.of("Title 4", "Title 3");
		Boolean confirm2 = false;
		String message2 = "message text 2";
		Payload sentPayload = new Payload(confirm2, files, message2, titles2);
		ModelAndView modelAndView = new ModelAndView("management", "payload", sentPayload);
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
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", sentPayload));
	}

	@Test
	void publishPages() throws Exception {
		List<String> titles = List.of("Title 1", "Title 2");
		String files = "";
		Boolean confirm = true;
		String message = "message text";
		Payload receivedPayload = new Payload(confirm, files, message, titles);
		List<String> titles2 = List.of("Title 1", "Title 2");
		Boolean confirm2 = false;
		String message2 = "message text 2";
		Payload sentPayload = new Payload(confirm2, files, message2, titles2);
		ModelAndView modelAndView = new ModelAndView("management", "payload", sentPayload);
		when(commonService.publishPages("management"))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/publish")
								.flashAttr("payload", receivedPayload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("management"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", sentPayload));
	}

	@Test
	void addFormula() throws Exception {
		String title = "Title 1";
		String filename = "title_1";
		Boolean editExistingPage = true;
		List<String> content = List.of("Line 1", "Line 2");
		String message = "message text";
		Payload receivedPayload = new Payload(content, editExistingPage, filename, message, title);
		List<String> content2 = List.of("Line 1", "Line 2", "Line 3");
		String message2 = "message text 2";
		Payload sentPayload = new Payload(content2, editExistingPage, filename, message2, title);
		ModelAndView modelAndView = new ModelAndView("source", "payload", sentPayload);
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
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", sentPayload));
	}

	@Test
	void savePage() throws Exception {
		String title = "Title 1";
		String filename = "title_1";
		Boolean editExistingPage = false;
		List<String> content = List.of("Line 1", "Line 2");
		String message = "message text";
		Payload receivedPayload = new Payload(content, editExistingPage, filename, message, title);
		Boolean editExistingPage2 = true;
		String message2 = "message text 2";
		Payload sentPayload = new Payload(content, editExistingPage2, filename, message2, title);
		ModelAndView modelAndView = new ModelAndView("source", "payload", sentPayload);
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
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", sentPayload));
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
		Payload sentPayload = new Payload(confirm2, files2, message2, titles2);
		ModelAndView modelAndView = new ModelAndView("management", "payload", sentPayload);
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
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", sentPayload));
	}

	@Test
	void generateHtml() throws Exception {
		List<String> titles = List.of("Title 1", "Title 2");
		String files = "";
		Boolean confirm = true;
		String message = "message text";
		Payload receivedPayload = new Payload(confirm, files, message, titles);
		Boolean confirm2 = false;
		String message2 = "message text 2";
		Payload sentPayload = new Payload(confirm2, files, message2, titles);
		ModelAndView modelAndView = new ModelAndView("management", "payload", sentPayload);
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
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", sentPayload));
	}
}