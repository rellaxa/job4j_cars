package ru.job4j.cars.repository.implementation;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.CarBrand;
import ru.job4j.cars.repository.CarBrandRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class HBNCarBrandRepository implements CarBrandRepository {

	private final CrudRepository crudRepository;

	public CarBrand save(CarBrand carBrand) {
		crudRepository.run(session -> session.persist(carBrand));
		return carBrand;
	}

	@Override
	public Collection<CarBrand> findAll() {
		return crudRepository.query("from CarBrand ORDER BY id ASC", CarBrand.class);
	}

	@Override
	public Optional<CarBrand> findById(int id) {
		return crudRepository.optional("from CarBrand WHERE id = :fId",
				CarBrand.class,
				Map.of("fId", id));
	}

	@Override
	public boolean deleteById(int id) {
		return crudRepository.runWithRsl(
				"DELETE CarBrand WHERE id = :fId",
				Map.of("fId", id)
		);
	}

	@Override
	public boolean deleteAll() {
		return crudRepository.runWithRsl("DELETE FROM CarBrand", Map.of());
	}
}
