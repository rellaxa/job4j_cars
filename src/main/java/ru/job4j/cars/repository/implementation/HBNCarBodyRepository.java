package ru.job4j.cars.repository.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.CarBody;
import ru.job4j.cars.repository.CarBodyRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class HBNCarBodyRepository implements CarBodyRepository {

	private final CrudRepository crudRepository;

	@Override
	public Optional<CarBody> findById(int id) {
		log.info("Finding car body by id: {}", id);
		return crudRepository.optional("from CarBody where id = :fId", CarBody.class,
				Map.of("fId", id));
	}

	@Override
	public Collection<CarBody> findAllOrderById() {
		log.info("Finding all car bodies");
		return crudRepository.query("from CarBody order by id desc", CarBody.class);
	}
}
