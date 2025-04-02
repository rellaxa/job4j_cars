package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class EngineRepository {

	private final CrudRepository crudRepository;

	public Engine create(Engine engine) {
		crudRepository.run(session -> session.persist(engine));
		return engine;
	}

	public void update(Engine engine) {
		crudRepository.run(session -> session.merge(engine));
	}

	public void delete(int engineId) {
		crudRepository.run(
				"delete from Engine where id = :fId",
				Map.of("fId", engineId)
		);
	}

	public List<Engine> findAllOrderById() {
		return crudRepository.query("from Engine order by id asc", Engine.class);
	}

	public Optional<Engine> findById(int engineId) {
		return crudRepository.optional(
				"from Engine where id = :fId", Engine.class,
				Map.of("fId", engineId)
		);
	}
}
