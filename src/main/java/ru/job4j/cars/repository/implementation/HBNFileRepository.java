package ru.job4j.cars.repository.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.File;
import ru.job4j.cars.repository.FileRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class HBNFileRepository implements FileRepository {

	private final CrudRepository crudRepository;

	@Override
	public File save(File file) {
		log.info("Saving file {}", file);
		crudRepository.run(session -> session.persist(file));
		return file;
	}

	@Override
	public Optional<File> findById(int fileId) {
		log.info("Finding file by {}", fileId);
		return crudRepository.optional("from File where id = :fId", File.class,
				Map.of("fId", fileId)
		);
	}

	@Override
	public Collection<File> getFilesByPostId(int postId) {
		log.info("Finding files by post {}", postId);
		return crudRepository.query("from File where post_id = :fPostId", File.class,
				Map.of("fPostId", postId));
	}

	@Override
	public Collection<File> findAll() {
		log.info("Finding all files");
		return crudRepository.query("from File", File.class);
	}

	@Override
	public List<String> getPathsByPostId(int postId) {
		log.info("Finding files by postId {}", postId);
		return crudRepository.query(
				"select f.path from File f where post_id = :fPostId", String.class,
				Map.of("fPostId", postId)
		);
	}

	@Override
	public boolean deleteById(int fileId) {
		log.info("Deleting file by {}", fileId);
		return crudRepository.runWithRsl("delete from File where id = :fId",
				Map.of("fId", fileId)
		);
	}

	@Override
	public List<String> deleteByPostId(int postId) {
		log.info("Deleting files by postId {}", postId);
		List<String> paths = crudRepository.query(
				"select f.path from File f where post_id = :fPostId", String.class,
				Map.of("fPostId", postId)
		);
		if (!paths.isEmpty()) {
			crudRepository.runWithRsl("delete from File where post_id = :fPostId",
					Map.of("fPostId", postId));
		}
		log.info("File paths: {}", paths);
		return paths;
	}

	@Override
	public boolean deleteAll() {
		log.info("Deleting all files");
		return crudRepository.runWithRsl("delete from File", Map.of());
	}

}
