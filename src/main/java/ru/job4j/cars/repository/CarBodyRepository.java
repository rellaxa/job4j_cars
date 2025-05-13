package ru.job4j.cars.repository;

import ru.job4j.cars.model.CarBody;

import java.util.Collection;
import java.util.Optional;

public interface CarBodyRepository {

	Optional<CarBody> findById(int id);

	Collection<CarBody> findAllOrderById();
}
