package lgrimm1.javaknowledge.common;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lgrimm1.javaknowledge.filestorage.*;
import lgrimm1.javaknowledge.process.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.*;
import org.springframework.web.servlet.*;

import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.springframework.http.*;
import org.springframework.mock.web.*;

import java.nio.file.*;
import java.util.stream.*;

@WebMvcTest(CommonController.class)
@AutoConfigureDataJpa
class CommonControllerTest {

/*
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

	@Test
	void getFallback() throws Exception {
		String searchText = "text";
		Payload payload2 = new Payload(
				formulas.getTitleRoot(),
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
		when(browsingService.getRoot("root"))
				.thenReturn(modelAndView);
		when(formulas.getTitleRoot())
				.thenReturn("ROOTTITLE");

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
		String searchText = "text";
		Payload payload2 = new Payload(
				formulas.getTitleRoot(),
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
		when(browsingService.getRoot("root"))
				.thenReturn(modelAndView);
		when(formulas.getTitleRoot())
				.thenReturn("ROOTTITLE");

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
		when(formulas.getTitleRoot())
				.thenReturn("ROOTTITLE");
		Payload payload = new Payload(
				formulas.getTitleRoot(),
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
		when(formulas.getTitleRoot())
				.thenReturn("LISTTITLE");
		Payload payload2 = new Payload(
				formulas.getTitleList(),
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
		when(browsingService.searchPages("list", payload))
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
		when(formulas.getTitleList())
				.thenReturn("LISTTITLE");
		Payload payload = new Payload(
				formulas.getTitleList(),
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
		String htmlContent = "Html Content";
		List<String> titleReferences = List.of("TITLE 2");
		when(formulas.getTitlePage())
				.thenReturn("PAGETITLE");
		Payload payload2 = new Payload(
				formulas.getTitlePage(),
				null,
				null,
				null,
				message,
				null,
				htmlContent,
				null,
				titleReferences
		);
		ModelAndView modelAndView = new ModelAndView("page", "payload", payload2);
		when(browsingService.getPage("page", payload))
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
		when(formulas.getTitleRoot())
				.thenReturn("ROOTTITLE");
		Payload payload = new Payload(
				formulas.getTitleRoot(),
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
		String message = "message text";
		String title = "";
		List<String> titles = List.of("Title 1", "Title 2");
		when(formulas.getTitleManagement())
				.thenReturn("MANAGEMENTTITLE");
		Payload payload2 = new Payload(
				formulas.getTitleManagement(),
				confirm,
				null,
				null,
				message,
				null,
				null,
				title,
				titles
		);

		ModelAndView modelAndView = new ModelAndView("management", "payload", payload2);
		when(browsingService.managePages("management"))
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
		String message = "message text";
		String title = "";
		List<String> titles = List.of("Title 1", "Title 2");
		when(formulas.getTitleManagement())
				.thenReturn("MANAGEMENTTITLE");
		Payload payload = new Payload(
				formulas.getTitleManagement(),
				confirm,
				null,
				null,
				message,
				null,
				null,
				title,
				titles
		);

		String content = "";
		Boolean editExistingPage = false;
		String message2 = "message text";
		String title2 = "";
		when(formulas.getTitleSource())
				.thenReturn("SOURCETITLE");
		Payload payload2 = new Payload(
				formulas.getTitleSource(),
				null,
				content,
				editExistingPage,
				message2,
				null,
				null,
				title2,
				null
		);

		ModelAndView modelAndView = new ModelAndView("source", "payload", payload2);
		when(browsingService.createSourcePage("source"))
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
		String message = "message text";
		String title = "";
		List<String> titles = List.of("Title 1", "Title 2");
		when(formulas.getTitleManagement())
				.thenReturn("MANAGEMENTTITLE");
		Payload payload = new Payload(
				formulas.getTitleManagement(),
				confirm,
				null,
				null,
				message,
				null,
				null,
				title,
				titles
		);

		String content = "Line 1\nLine 2\n";
		Boolean editExistingPage = true;
		String message2 = "message text";
		String title2 = "Title 1";
		when(formulas.getTitleSource())
				.thenReturn("SOURCETITLE");
		Payload payload2 = new Payload(
				formulas.getTitleSource(),
				null,
				content,
				editExistingPage,
				message2,
				null,
				null,
				title2,
				null
		);

		ModelAndView modelAndView = new ModelAndView("source", "payload", payload2);
		when(browsingService.editSourcePage("source", payload))
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
	void renameSourcePage() throws Exception {
		Boolean confirm = true;
		String message = "message text";
		String title = "Title 2";
		List<String> titles = List.of("Title 1");
		when(formulas.getTitleManagement())
				.thenReturn("MANAGEMENTTITLE");
		Payload payload = new Payload(
				formulas.getTitleManagement(),
				confirm,
				null,
				null,
				message,
				null,
				null,
				title,
				titles
		);

		Boolean confirm2 = false;
		String message2 = "message text 2";
		String title2 = "";
		List<String> titles2 = List.of("Title 2");
		Payload payload2 = new Payload(
				formulas.getTitleManagement(),
				confirm2,
				null,
				null,
				message2,
				null,
				null,
				title2,
				titles2
		);

		ModelAndView modelAndView = new ModelAndView("management", "payload", payload2);
		when(browsingService.renameSourcePage("management", payload))
				.thenReturn(modelAndView);

		mockMvc
				.perform(
						post("/rename")
								.flashAttr("payload", payload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("management"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", payload2));
	}

	@Test
	void deleteSourcePages() throws Exception {
		Boolean confirm = true;
		String message = "message text";
		String title = "";
		List<String> titles = List.of("Title 1", "Title 2");
		when(formulas.getTitleManagement())
				.thenReturn("MANAGEMENTTITLE");
		Payload payload = new Payload(
				formulas.getTitleManagement(),
				confirm,
				null,
				null,
				message,
				null,
				null,
				title,
				titles
		);

		Boolean confirm2 = false;
		String message2 = "message text 2";
		List<String> titles2 = List.of("Title 3", "Title 4");
		Payload payload2 = new Payload(
				formulas.getTitleManagement(),
				confirm2,
				null,
				null,
				message2,
				null,
				null,
				title,
				titles2
		);

		ModelAndView modelAndView = new ModelAndView("management", "payload", payload2);
		when(browsingService.deletePages("management", payload))
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
	void importTxt() throws Exception {
		Boolean confirm = true;
		String message = "message text";
		String title = "";
		List<String> titles = List.of("Title 1");
		when(formulas.getTitleManagement())
				.thenReturn("MANAGEMENTTITLE");
		Payload payload = new Payload(
				formulas.getTitleManagement(),
				confirm,
				null,
				null,
				message,
				null,
				null,
				title,
				titles
		);

		String filename1 = "file1.txt";
		String filename2 = "file2.txt";
		String content1 = "content1";
		String content2 = "content2";
		MockMultipartFile file1 = new MockMultipartFile(
				"files",
				filename1,
				MediaType.TEXT_PLAIN_VALUE,
				content1.getBytes()
		);
		MockMultipartFile file2 = new MockMultipartFile(
				"files",
				filename2,
				MediaType.TEXT_PLAIN_VALUE,
				content2.getBytes()
		);

		Multipart multipartOfFiles1 = new Multipart("files", filename1, MediaType.TEXT_PLAIN_VALUE, content1.getBytes());
		Multipart multipartOfFiles2 = new Multipart("files", filename2, MediaType.TEXT_PLAIN_VALUE, content2.getBytes());
		List<Multipart> multiparts = List.of(multipartOfFiles1, multipartOfFiles2);
		long[] uploadResults = new long[]{2, 2};
		when(fileStorageService.uploadFiles(payload, multiparts))
				.thenReturn(uploadResults);

		Path path1 = Path.of(filename1);
		Path path2 = Path.of(filename2);
		Stream<Path> paths = Stream.of(path1, path2);
		when(fileStorageService.findAll())
				.thenReturn(paths);

		Boolean confirm2 = false;
		String message2 = "message text 2";
		List<String> titles2 = List.of("Title 1", "Title 2", "Title 3", "Title 4");
		Payload payload2 = new Payload(
				formulas.getTitleManagement(),
				confirm2,
				null,
				null,
				message2,
				null,
				null,
				title,
				titles2
		);
		ModelAndView modelAndView = new ModelAndView("management", "payload", payload2);
		when(browsingService.importTxt("management", payload, paths, uploadResults))
				.thenReturn(modelAndView);

		when(fileStorageService.deleteAllFiles())
				.thenReturn(new long[]{2, 2});

		mockMvc
				.perform(
						multipart("/import")
								.file(file1)
								.file(file2)
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
		Boolean confirm = true;
		String message = "message text";
		String title = "";
		List<String> titles = List.of("Title 1");
		when(formulas.getTitleManagement())
				.thenReturn("MANAGEMENTTITLE");
		Payload payload = new Payload(
				formulas.getTitleManagement(),
				confirm,
				null,
				null,
				message,
				null,
				null,
				title,
				titles
		);

		Boolean confirm2 = false;
		String message2 = "message text 2";
		List<String> titles2 = List.of("Title 1", "Title 2");
		Payload payload2 = new Payload(
				formulas.getTitleManagement(),
				confirm2,
				null,
				null,
				message2,
				null,
				null,
				title,
				titles2
		);

		ModelAndView modelAndView = new ModelAndView("management", "payload", payload2);
		when(browsingService.generateHtml("management", payload))
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

	@Test
	void addFormula() throws Exception {
		String content = "Line 1\nLine 2\n";
		Boolean editExistingPage = true;
		String message = "message text";
		String title = "Title 1";
		when(formulas.getTitleSource())
				.thenReturn("SOURCETITLE");
		Payload payload = new Payload(
				formulas.getTitleSource(),
				null,
				content,
				editExistingPage,
				message,
				null,
				null,
				title,
				null
		);

		String formulaName = "formula_name";
		String message2 = "message text";
		Payload payload2 = new Payload(
				formulas.getTitleSource(),
				null,
				content + formulaName,
				editExistingPage,
				message2,
				null,
				null,
				title,
				null
		);

		ModelAndView modelAndView = new ModelAndView("source", "payload", payload2);
		when(browsingService.addFormula("source", formulaName, payload))
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
		String content = "Line 1\nLine 2\n";
		Boolean editExistingPage = true;
		String message = "message text";
		String title = "Title 1";
		when(formulas.getTitleSource())
				.thenReturn("SOURCETITLE");
		Payload payload = new Payload(
				formulas.getTitleSource(),
				null,
				content,
				editExistingPage,
				message,
				null,
				null,
				title,
				null
		);

		String message2 = "message text";
		Payload payload2 = new Payload(
				formulas.getTitleSource(),
				null,
				content,
				editExistingPage,
				message2,
				null,
				null,
				title,
				null
		);

		ModelAndView modelAndView = new ModelAndView("source", "payload", payload2);
		when(browsingService.savePage("source", payload))
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
*/
}