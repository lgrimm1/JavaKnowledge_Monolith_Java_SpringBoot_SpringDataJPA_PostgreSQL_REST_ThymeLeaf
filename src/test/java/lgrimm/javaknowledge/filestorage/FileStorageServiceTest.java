package lgrimm.javaknowledge.filestorage;

import lgrimm.javaknowledge.databasestorage.DatabaseStorageService;
import lgrimm.javaknowledge.datamodels.Multipart;
import lgrimm.javaknowledge.datamodels.Payload;
import lgrimm.javaknowledge.process.Formulas;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

class FileStorageServiceTest {

	FileStorageRepository repository;
	FileStorageService service;
	DatabaseStorageService databaseStorageService;
	Formulas formulas;
	Path path1, path2;
	List<Path> paths, paths2;
	String filename1, filename2, content1, content2;
	Multipart file1, file2;

	@BeforeEach
	void setUp() {
		repository = Mockito.mock(FileStorageRepository.class);
		when(repository.init("." + File.separator + "upload", true))
				.thenReturn(true);
		databaseStorageService = Mockito.mock(DatabaseStorageService.class);
		formulas = Mockito.mock(Formulas.class);
		formulas = Mockito.mock(Formulas.class);
		service = new FileStorageService(repository, databaseStorageService, formulas);

		filename1 = "file1.txt";
		filename2 = "file2.txt";
		content1 = "content1";
		content2 = "content2";
		file1 = new Multipart("file", filename1, MediaType.TEXT_PLAIN_VALUE, content1.getBytes());
		file2 = new Multipart("file", filename2, MediaType.TEXT_PLAIN_VALUE, content2.getBytes());
		path1 = new File(filename1).toPath();
		path2 = new File(filename2).toPath();
		paths = List.of(path1, path2);
		paths2 = List.of(path1, path2);
		when(repository.findAll())
				.thenReturn(paths);
	}

	@Test
	void init_Fail() {
		String root = "." + File.separator + "upload";
		when(repository.init(root, false))
				.thenReturn(false);
		when(repository.init(root, true))
				.thenReturn(false);

		Exception e = Assertions.assertThrows(Exception.class,
				() -> new FileStorageService(repository, databaseStorageService, formulas));
		Assertions.assertEquals("Could not initialize the file storage!", e.getMessage());
	}

	@Test
	void init_Success() {
		String root = "." + File.separator + "upload";
		when(repository.init(root, false))
				.thenReturn(true);
		when(repository.init(root, true))
				.thenReturn(true);

		service = Assertions.assertDoesNotThrow(
				() -> new FileStorageService(repository, databaseStorageService, formulas)
		);
		Assertions.assertNotNull(service);
	}

	@Test
	void uploadFiles_NullPayload() {
		Assertions.assertArrayEquals(new long[]{0,0}, service.uploadFiles(null, null));
	}

	@Test
	void uploadFiles_NullConfirm() {
		Payload payload = new Payload(
				"",
				null,
				null,
				null,
				"",
				null,
				null,
				null,
				new ArrayList<>()
				);
		Assertions.assertArrayEquals(new long[]{0,0}, service.uploadFiles(payload, null));
	}

	@Test
	void uploadFiles_NotConfirmed() {
		Payload payload = new Payload(
				"",
				false,
				null,
				null,
				"",
				null,
				null,
				null,
				new ArrayList<>()
				);
		Assertions.assertArrayEquals(new long[]{0,0}, service.uploadFiles(payload, null));
	}

	@Test
	void uploadFiles_NullFiles() {
		Payload payload = new Payload(
				"",
				true,
				null,
				null,
				"",
				null,
				null,
				null,
				new ArrayList<>()
		);
		Assertions.assertArrayEquals(new long[]{0,0}, service.uploadFiles(payload, null));
	}

	@Test
	void uploadFiles_EmptyFiles() {
		Payload payload = new Payload(
				"",
				true,
				null,
				null,
				"",
				null,
				null,
				null,
				new ArrayList<>()
		);
		Assertions.assertArrayEquals(new long[]{0,0}, service.uploadFiles(payload, new ArrayList<>()));
	}

	@Test
	void uploadFiles_GivenFiles() {
		List<Multipart> filesWithoutNull = new ArrayList<>();
		filesWithoutNull.add(file1);
		filesWithoutNull.add(file2);
		List<Multipart> filesWithNull = new ArrayList<>(filesWithoutNull);
		filesWithNull.add(null);
		when(repository.saveAll(filesWithoutNull))
				.thenReturn(Stream.of(filename1));
		Payload payload = new Payload(
				"",
				true,
				null,
				null,
				"",
				null,
				null,
				null,
				new ArrayList<>()
		);
		Assertions.assertArrayEquals(new long[]{2,1}, service.uploadFiles(payload, filesWithNull));
	}

	@Test
	void findAll() {
		List<Path> expected = paths2;
		when(repository.findAll())
				.thenReturn(paths);
		Assertions.assertEquals(expected, service.findAll());
	}

	@Test
	void deleteAllFiles() {
		when(repository.count())
				.thenReturn(2L);
		when(repository.deleteAll())
				.thenReturn(1L);
		paths = List.of(path1);
		Assertions.assertArrayEquals(new long[]{2,1}, service.deleteAllFiles());
	}

	@Test
	void handleMaxSizeException() {
		List<String> titles = List.of("Title 1", "Title 2");
		when(databaseStorageService.getAllTitles())
				.thenReturn(titles);
		Payload expectedPayload = new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"ONE OR MORE FILE(S) EXCEEDED 1MB!",
				null,
				null,
				"",
				titles
		);
		Assertions.assertEquals(expectedPayload, service.handleMaxSizeException());
	}
}