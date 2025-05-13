package ru.job4j.cars.repository;

import ru.job4j.cars.model.Engine;

import java.util.Collection;
import java.util.Optional;

public interface EngineRepository {

	Engine save(Engine engine);

	Optional<Engine> findById(int engineId);

	Collection<Engine> findAllOrderById();

	Optional<Integer> findEngineIdByCarId(int carId);

	boolean update(Engine engine);

	boolean deleteById(int engineId);

	boolean deleteAll();
}
