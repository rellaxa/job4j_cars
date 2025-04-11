package ru.job4j.cars.repository.implementation;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.File;
import ru.job4j.cars.repository.FileRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class HBNFileRepository implements FileRepository {

	private final CrudRepository crudRepository;

	@Override
	public File save(File file) {
		crudRepository.run(session -> session.persist(file));
		return file;
	}

	@Override
	public Optional<File> findById(int fileId) {
		return crudRepository.optional("from File where id = :fId", File.class,
				Map.of("fId", fileId)
		);
	}

	@Override
	public Collection<File> findAll() {
		return crudRepository.query("from File", File.class);
	}

	@Override
	public boolean deleteById(int fileId) {
		return crudRepository.runWithRsl("delete from File where id = :fId",
				Map.of("fId", fileId)
		);
	}

	@Override
	public boolean deleteAll() {
		return crudRepository.runWithRsl("delete from File", Map.of());
	}
}
