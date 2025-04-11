package ru.job4j.cars.repository;

import ru.job4j.cars.model.Car;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CarRepository {

	Car save(Car car);

	Optional<Car> findById(int carId);

	Collection<Car> findAllOrderById();

	boolean update(Car car);

	boolean delete(int carId);

	boolean deleteAll();
}
