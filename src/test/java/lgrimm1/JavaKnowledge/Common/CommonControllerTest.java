package lgrimm1.JavaKnowledge.Common;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
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
		ModelAndView modelAndView = new ModelAndView("root");
		modelAndView.addObject("search_text", "");
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
				.andExpect(model().attribute("search_text", ""));
	}

	@Test
	void searchPages() throws Exception {
		List<String> titles = List.of("Title 1", "Title 2");
		ModelAndView modelAndView = new ModelAndView("list");
		modelAndView.addObject("titles", titles);
		when(commonService.searchPages("list", "text"))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/search")
								.param("search_text", "text")
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("list"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("titles", titles));
	}

/*
	@Test
	void getPage() {
	}

	@Test
	void managePages() {
	}

	@Test
	void createSourcePage() {
	}

	@Test
	void editSourcePage() {
	}

	@Test
	void deleteSourcePages() {
	}

	@Test
	void publishPages() {
	}

	@Test
	void addFormula() {
	}

	@Test
	void savePage() {
	}

	@Test
	void importTxt() {
	}

	@Test
	void generateHtml() {
	}
*/
}