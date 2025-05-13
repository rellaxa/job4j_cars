package ru.job4j.cars.service.engine;

import ru.job4j.cars.model.Engine;
import ru.job4j.cars.service.ModelService;

import java.util.Optional;

public interface EngineService extends ModelService<Engine> {

	Engine save(Engine engine);

	boolean update(Engine engine);

	Optional<Integer> findEngineIdByCarId(int carId);

	boolean deleteById(int engineId);
}
