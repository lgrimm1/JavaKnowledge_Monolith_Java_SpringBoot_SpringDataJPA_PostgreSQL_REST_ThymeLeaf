package lgrimm1.javaknowledge.controller;

import lgrimm1.javaknowledge.datamodels.Multipart;
import lgrimm1.javaknowledge.datamodels.Payload;
import lgrimm1.javaknowledge.filestorage.FileStorageService;
import lgrimm1.javaknowledge.process.Formulas;
import lgrimm1.javaknowledge.services.BrowsingService;
import lgrimm1.javaknowledge.services.EditingService;
import lgrimm1.javaknowledge.services.ManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(Controller.class)
@AutoConfigureDataJpa
class ControllerManagementTest {

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
	void managePages() throws Exception {
        when(managementService.managePages())
                .thenReturn(payload2);

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
		when(managementService.createSourcePage())
				.thenReturn(payload2);

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
	void editSourcePage_Success() throws Exception {
		when(managementService.editSourcePage(payload))
				.thenReturn(payload2);

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
	void editSourcePage_Fail() throws Exception {
		when(managementService.editSourcePage(payload))
				.thenThrow(new RuntimeException("message"));
        when(managementService.managePages())
                .thenReturn(payload2);
        payload2.setMessage("message");

		mockMvc
				.perform(
						post("/source/edit")
								.flashAttr("payload", payload)
				)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("management"))
				.andExpect(model().size(1))
				.andExpect(model().attribute("payload", payload2));
	}

	@Test
	void renameSourcePage_Success() throws Exception {
		when(managementService.renameSourcePage(payload))
				.thenReturn(payload2);

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
	void renameSourcePage_Fail() throws Exception {
        when(managementService.renameSourcePage(payload))
                .thenThrow(new RuntimeException("message"));
        when(managementService.managePages())
                .thenReturn(payload2);
        payload2.setMessage("message");

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
	void deleteSourcePages_Success() throws Exception {
		when(managementService.deletePages(payload))
				.thenReturn(payload2);

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
	void deleteSourcePages_Fail() throws Exception {
        when(managementService.deletePages(payload))
                .thenThrow(new RuntimeException("message"));
        when(managementService.managePages())
                .thenReturn(payload2);
        payload2.setMessage("message");

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
	void importTxt_Success() throws Exception {
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

		Multipart multipartOfFiles1 = new Multipart(
				"files",
				filename1,
				MediaType.TEXT_PLAIN_VALUE,
				content1.getBytes()
		);
		Multipart multipartOfFiles2 = new Multipart(
				"files",
				filename2,
				MediaType.TEXT_PLAIN_VALUE,
				content2.getBytes()
		);
		List<Multipart> multipartList = List.of(multipartOfFiles1, multipartOfFiles2);
		long[] uploadResults = new long[]{2, 2};
		when(fileStorageService.uploadFiles(payload, multipartList))
				.thenReturn(uploadResults);

		Path path1 = Path.of(filename1);
		Path path2 = Path.of(filename2);
		Stream<Path> paths = Stream.of(path1, path2);
		when(fileStorageService.findAll())
				.thenReturn(paths);

		when(managementService.importTxt(payload, paths, uploadResults))
				.thenReturn(payload2);

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
	void importTxt_Fail() throws Exception {
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

		Multipart multipartOfFiles1 = new Multipart(
				"files",
				filename1,
				MediaType.TEXT_PLAIN_VALUE,
				content1.getBytes()
		);
		Multipart multipartOfFiles2 = new Multipart(
				"files",
				filename2,
				MediaType.TEXT_PLAIN_VALUE,
				content2.getBytes()
		);
		List<Multipart> multipartList = List.of(multipartOfFiles1, multipartOfFiles2);
		long[] uploadResults = new long[]{2, 2};
		when(fileStorageService.uploadFiles(payload, multipartList))
				.thenReturn(uploadResults);

		Path path1 = Path.of(filename1);
		Path path2 = Path.of(filename2);
		Stream<Path> paths = Stream.of(path1, path2);
		when(fileStorageService.findAll())
				.thenReturn(paths);

        when(managementService.importTxt(payload, paths, uploadResults))
                .thenThrow(new RuntimeException("message"));
        when(managementService.managePages())
                .thenReturn(payload2);
        payload2.setMessage("message");

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
	void generateHtml_Success() throws Exception {
		when(managementService.generateHtml(payload))
				.thenReturn(payload2);

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
	void generateHtml_Fail() throws Exception {
        when(managementService.generateHtml(payload))
                .thenThrow(new RuntimeException("message"));
        when(managementService.managePages())
                .thenReturn(payload2);
        payload2.setMessage("message");

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