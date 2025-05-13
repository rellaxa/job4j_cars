package ru.job4j.cars.service.engine;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.EngineRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleEngineService implements EngineService {

	private final EngineRepository engineRepository;

	@Override
	public Engine save(Engine engine) {
		return engineRepository.save(engine);
	}

	@Override
	public Optional<Engine> findById(int id) {
		return engineRepository.findById(id);
	}

	@Override
	public Optional<Integer> findEngineIdByCarId(int carId) {
		return engineRepository.findEngineIdByCarId(carId);
	}

	@Override
	public Collection<Engine> findAllOrderById() {
		return engineRepository.findAllOrderById();
	}

	@Override
	public boolean update(Engine engine) {
		return engineRepository.update(engine);
	}

	@Override
	public boolean deleteById(int engineId) {
		return engineRepository.deleteById(engineId);
	}
}
