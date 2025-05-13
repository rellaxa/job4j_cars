package ru.job4j.cars.repository.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.CarBrand;
import ru.job4j.cars.repository.CarBrandRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class HBNCarBrandRepository implements CarBrandRepository {

	private final CrudRepository crudRepository;

	public CarBrand save(CarBrand carBrand) {
		log.info("Saving carBrand: {}", carBrand);
		crudRepository.run(session -> session.persist(carBrand));
		return carBrand;
	}

	@Override
	public Collection<CarBrand> findAll() {
		log.info("Finding all car brands");
		return crudRepository.query("from CarBrand ORDER BY id ASC", CarBrand.class);
	}

	@Override
	public Optional<CarBrand> findById(int id) {
		log.info("Finding car brand by id: {}", id);
		return crudRepository.optional("from CarBrand WHERE id = :fId",
				CarBrand.class,
				Map.of("fId", id));
	}

	@Override
	public boolean deleteById(int id) {
		log.info("Deleting car brand by id: {}", id);
		return crudRepository.runWithRsl(
				"DELETE CarBrand WHERE id = :fId",
				Map.of("fId", id)
		);
	}

	@Override
	public boolean deleteAll() {
		log.info("Deleting all cars");
		return crudRepository.runWithRsl("DELETE FROM CarBrand", Map.of());
	}
}
