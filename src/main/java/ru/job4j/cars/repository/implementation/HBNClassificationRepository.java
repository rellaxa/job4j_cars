package ru.job4j.cars.repository.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Classification;
import ru.job4j.cars.repository.ClassificationRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class HBNClassificationRepository implements ClassificationRepository {

	private final CrudRepository crudRepository;

	@Override
	public Optional<Classification> findById(int id) {
		log.info("Finding class_car by id: {}", id);
		return crudRepository.optional("from Classification where id = :fId", Classification.class,
				Map.of("fId", id));
	}

	@Override
	public Collection<Classification> findAllOrderById() {
		log.info("Finding all classes by order by id");
		return crudRepository.query("from Classification order by id asc", Classification.class);
	}
}
