package lgrimm.javaknowledge.filestorage;

import lgrimm.javaknowledge.datamodels.*;
import org.springframework.core.io.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/**
 * @see #init(String, boolean)
 * @see #getStorageRootFolder()
 * @see #getByFilename(String)
 * @see #getAll()
 * @see #findByFilename(String)
 * @see #findAll()
 * @see #save(Multipart)
 * @see #saveAll(List)
 * @see #delete(String)
 * @see #deleteAll()
 * @see #count()
 */
@Repository
public class FileStorageRepository {
	private Path storageRootFolder;

	public boolean init(String repositoryPath, boolean deleteAllFromStorage) {
		try {
			this.storageRootFolder = Paths.get(repositoryPath);
			if (deleteAllFromStorage) {
				FileSystemUtils.deleteRecursively(storageRootFolder.toFile());
			}
			Files.createDirectories(storageRootFolder);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	public Path getStorageRootFolder() {
		return storageRootFolder;
	}

	public Optional<Resource> getByFilename(String filename) {
		try {
			Path path = storageRootFolder.resolve(filename);
			Resource resource = new UrlResource(path.toUri());
			if (resource.exists() || resource.isReadable()) {
				return Optional.of(resource);
			}
			else {
				return Optional.empty();
			}
		}
		catch (Exception e) {
			return Optional.empty();
		}
	}

	public List<Resource> getAll() {
		return this.findAll().stream()
				.map(path -> path.toFile().getName())
				.map(this::getByFilename)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.toList();
	}

	public Optional<Path> findByFilename(String filename) {
		try {
			Path path = this.storageRootFolder.resolve(filename);
			if (path.toFile().exists() || path.toFile().isFile()) {
				return Optional.of(storageRootFolder.relativize(path));
			}
			return Optional.empty();
		}
		catch (Exception e) {
			return Optional.empty();
		}
	}

	public List<Path> findAll() {
		try (Stream<Path> walk = Files.walk(this.storageRootFolder, 1)) {
			return walk
					.filter(path -> !path.equals(this.storageRootFolder))
					.toList();
		}
		catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public Optional<String> save(Multipart file) {
		try {
			Path path = this.storageRootFolder.resolve(file.getOriginalFilename());
			Files.copy(new ByteArrayInputStream(file.getContent()), path);
			return Optional.of(file.getOriginalFilename());
		}
		catch (Exception e) {
			return Optional.empty();
		}
	}

	public Stream<String> saveAll(List<Multipart> files) {
		return files.stream()
				.map(this::save)
				.filter(Optional::isPresent)
				.map(Optional::get);
	}

	public boolean delete(String filename) {
		try {
			Path file = storageRootFolder.resolve(filename);
			return Files.deleteIfExists(file);
		}
		catch (Exception e) {
			return false;
		}
	}

	public long deleteAll() {
		try (Stream<Path> walk = Files.walk(this.storageRootFolder, 1)) {
			return walk
					.filter(path -> !path.equals(this.storageRootFolder))
					.map(Path::toFile)
					.map(File::delete)
					.filter(success -> success)
					.count();
		}
		catch (Exception e) {
			return 0;
		}
	}

	public long count() {
		try (Stream<Path> walk = Files.walk(this.storageRootFolder, 1)) {
			return walk
					.filter(path -> !path.equals(this.storageRootFolder))
					.count();
		}
		catch (Exception e) {
			return -1;
		}
	}
}
