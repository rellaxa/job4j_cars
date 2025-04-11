package ru.job4j.cars.repository.implementation;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.EngineRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class HBNEngineRepository implements EngineRepository {

	private final CrudRepository crudRepository;

	@Override
	public Engine save(Engine engine) {
		crudRepository.run(session -> session.persist(engine));
		return engine;
	}

	@Override
	public Optional<Engine> findById(int engineId) {
		return crudRepository.optional(
				"from Engine where id = :fId", Engine.class,
				Map.of("fId", engineId)
		);
	}

	@Override
	public Collection<Engine> findAllOrderById() {
		return crudRepository.query("from Engine order by id asc", Engine.class);
	}

	@Override
	public boolean deleteById(int engineId) {
		return crudRepository.runWithRsl(
				"delete from Engine where id = :fId",
				Map.of("fId", engineId)
		);
	}

	@Override
	public boolean deleteAll() {
		return crudRepository.runWithRsl("delete from Engine", Map.of());
	}
}
