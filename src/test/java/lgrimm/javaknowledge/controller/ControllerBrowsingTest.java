package lgrimm.javaknowledge.controller;

import lgrimm.javaknowledge.datamodels.Payload;
import lgrimm.javaknowledge.process.Formulas;
import lgrimm.javaknowledge.filestorage.FileStorageService;
import lgrimm.javaknowledge.services.BrowsingService;
import lgrimm.javaknowledge.services.EditingService;
import lgrimm.javaknowledge.services.ManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(Controller.class)
@AutoConfigureDataJpa
class ControllerBrowsingTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private BrowsingService browsingService;
	@MockBean
	private ManagementService managementService;
	@MockBean
	private EditingService editingService;
	@MockBean
	private FileStorageService fileStorageService;
	@MockBean
	private Formulas formulas;

    Payload payload, payload2;
    @BeforeEach
    void setUp() {
        payload = new Payload(
                "templateTitle1",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        payload2 = new Payload(
                "templateTitle2",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

	@Test
	void getFallback() throws Exception {
        when(browsingService.getRoot())
                .thenReturn(payload2);

		mockMvc
				.perform(
						get("/something")
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("root"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", payload2));
	}

	@Test
	void getRoot() throws Exception {
        when(browsingService.getRoot())
                .thenReturn(payload2);

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
	void searchPages_Success() throws Exception {
        when(browsingService.searchPages(payload))
                .thenReturn(payload2);

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
	void searchPages_Fail() throws Exception {
        when(browsingService.searchPages(payload))
                .thenThrow(new RuntimeException("message"));
        when(browsingService.getRoot())
                .thenReturn(payload2);

		mockMvc
				.perform(
						post("/search")
								.flashAttr("payload", payload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("root"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", payload2));
	}

	@Test
	void getPage_Success() throws Exception {
        when(browsingService.getPage(payload))
                .thenReturn(payload2);

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
	void getPage_Fail() throws Exception {
        when(browsingService.getPage(payload))
                .thenThrow(new RuntimeException("message"));
        when(browsingService.getRoot())
                .thenReturn(payload2);

        mockMvc
				.perform(
						post("/page")
								.flashAttr("payload", payload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("root"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", payload2));
	}
}