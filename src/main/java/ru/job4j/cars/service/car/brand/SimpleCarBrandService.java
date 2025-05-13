package ru.job4j.cars.service.car.brand;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.CarBrand;
import ru.job4j.cars.repository.CarBrandRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleCarBrandService implements CarBrandService {

	private final CarBrandRepository carBrandRepository;

	@Override
	public CarBrand save(CarBrand carBrand) {
		return carBrandRepository.save(carBrand);
	}

	@Override
	public Optional<CarBrand> findById(int id) {
		return carBrandRepository.findById(id);
	}

	@Override
	public Collection<CarBrand> findAllOrderById() {
		return carBrandRepository.findAll();
	}
}
