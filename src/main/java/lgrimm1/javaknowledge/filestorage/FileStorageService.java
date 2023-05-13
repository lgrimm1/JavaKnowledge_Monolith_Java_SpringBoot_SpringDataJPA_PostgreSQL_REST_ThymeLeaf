package lgrimm1.javaknowledge.filestorage;

import lgrimm1.javaknowledge.databasestorage.DatabaseStorageService;
import lgrimm1.javaknowledge.datamodels.Multipart;
import lgrimm1.javaknowledge.datamodels.Payload;
import lgrimm1.javaknowledge.process.Formulas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @see #uploadFiles(Payload, List)
 * @see #findAll()
 * @see #deleteAllFiles()
 * @see #handleMaxSizeException()
 */
@Service
public class FileStorageService {

	private final FileStorageRepository repository;
	private final DatabaseStorageService databaseStorageService;
	private final Formulas formulas;

	@Autowired
	public FileStorageService(FileStorageRepository repository,
							  DatabaseStorageService databaseStorageService,
							  Formulas formulas) {
		this.repository = repository;
		this.databaseStorageService = databaseStorageService;
		this.formulas = formulas;
		if (!repository.init("." + File.separator + "upload", true)) {
			throw new RuntimeException("Could not initialize the file storage!");
		}
	}

	/**
	 * Returns long[] where [0]: original amount of files, [1]: amount of successfully uploaded files.
	 */
	public long[] uploadFiles(Payload payload, List<Multipart> files) {
		if (payload == null ||
				payload.getConfirm() == null ||
				!payload.getConfirm() ||
				files == null ||
				files.size() == 0) {
			return new long[]{0, 0};
		}
		files = files.stream()
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		return new long[]{files.size(), repository.saveAll(files).count()};
	}

	public List<Path> findAll() {
		return repository.findAll();
	}

	/**
	 * Returns long[] where [0]: original amount of files, [1]: amount of successfully deleted files.
	 */
	public long[] deleteAllFiles() {
		long count = repository.count();
		long deleted = repository.deleteAll();
		return new long[]{count, deleted};
	}

	public Payload handleMaxSizeException() {
		return new Payload(
				formulas.getTitleManagement(),
				false,
				null,
				null,
				"ONE OR MORE FILE(S) EXCEEDED 1MB!",
				null,
				null,
				"",
				databaseStorageService.getAllTitles()
		);
	}
}
