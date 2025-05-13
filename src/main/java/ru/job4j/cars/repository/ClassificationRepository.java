package ru.job4j.cars.repository;

import ru.job4j.cars.model.Classification;

import java.util.Collection;
import java.util.Optional;

public interface ClassificationRepository {

	Optional<Classification> findById(int id);

	Collection<Classification> findAllOrderById();
}
