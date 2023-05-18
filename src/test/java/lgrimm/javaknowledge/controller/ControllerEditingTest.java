package lgrimm.javaknowledge.controller;

import lgrimm.javaknowledge.datamodels.Payload;
import lgrimm.javaknowledge.filestorage.FileStorageService;
import lgrimm.javaknowledge.process.Formulas;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(Controller.class)
@AutoConfigureDataJpa
class ControllerEditingTest {

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
	void addFormula_Success() throws Exception {
		when(editingService.addFormula("formula_name", payload))
				.thenReturn(payload2);

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
	void addFormula_Fail() throws Exception {
        when(editingService.addFormula("formula_name", payload))
                .thenThrow(new RuntimeException("message"));
        when(managementService.managePages())
                .thenReturn(payload2);
        payload2.setMessage("message");

		mockMvc
				.perform(
						post("/add/formula_name")
								.flashAttr("payload", payload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("management"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", payload2));
	}

	@Test
	void savePage_Success_New() throws Exception {
		when(editingService.savePage(payload))
				.thenReturn(payload2);
        when(formulas.getTitleSource())
                .thenReturn("source");
        payload2.setTemplateTitle("MANAGEMENT");

		mockMvc
				.perform(
						post("/save")
								.flashAttr("payload", payload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("management"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", payload2));
	}

	@Test
	void savePage_Success_Edit() throws Exception {
		when(editingService.savePage(payload))
				.thenReturn(payload2);
        when(formulas.getTitleSource())
                .thenReturn("source");
        payload2.setTemplateTitle("SOURCE");

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
	void savePage_Success_Fail() throws Exception {
        when(editingService.savePage(payload))
                .thenThrow(new RuntimeException("message"));
        when(managementService.managePages())
                .thenReturn(payload2);
        payload2.setMessage("message");

		mockMvc
				.perform(
						post("/save")
								.flashAttr("payload", payload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("management"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", payload2));
	}
}