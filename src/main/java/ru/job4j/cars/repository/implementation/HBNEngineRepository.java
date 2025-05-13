package ru.job4j.cars.repository.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.EngineRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class HBNEngineRepository implements EngineRepository {

	private final CrudRepository crudRepository;

	@Override
	public Engine save(Engine engine) {
		log.info("Saving engine {}", engine);
		crudRepository.run(session -> session.persist(engine));
		return engine;
	}

	@Override
	public Optional<Engine> findById(int engineId) {
		log.info("Finding engine by {}", engineId);
		return crudRepository.optional(
				"from Engine where id = :fId", Engine.class,
				Map.of("fId", engineId)
		);
	}

	@Override
	public Collection<Engine> findAllOrderById() {
		log.info("Finding all engines by order by id");
		return crudRepository.query("from Engine order by id asc", Engine.class);
	}

	@Override
	public Optional<Integer> findEngineIdByCarId(int carId) {
		log.info("Finding engine id by car id: {}", carId);
		return crudRepository.optional("select c.engine.id from Car c where id = :fCarId",
				Integer.class, Map.of("fCarId", carId));
	}

	@Override
	public boolean update(Engine engine) {
		log.info("Updating engine {}", engine);
		return crudRepository.tx(session -> session.merge(engine)) != null;
	}

	@Override
	public boolean deleteById(int engineId) {
		log.info("Deleting engine by {}", engineId);
		return crudRepository.runWithRsl(
				"delete from Engine where id = :fId",
				Map.of("fId", engineId)
		);
	}

	@Override
	public boolean deleteAll() {
		log.info("Deleting all engines");
		return crudRepository.runWithRsl("delete from Engine", Map.of());
	}
}
