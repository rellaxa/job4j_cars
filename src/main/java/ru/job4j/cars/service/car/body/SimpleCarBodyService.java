package ru.job4j.cars.service.car.body;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.CarBody;
import ru.job4j.cars.repository.CarBodyRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleCarBodyService implements CarBodyService {

	private final CarBodyRepository carBodyRepository;

	@Override
	public Optional<CarBody> findById(int id) {
		return carBodyRepository.findById(id);
	}

	@Override
	public Collection<CarBody> findAllOrderById() {
		return carBodyRepository.findAllOrderById();
	}
}
