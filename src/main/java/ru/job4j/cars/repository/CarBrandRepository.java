package ru.job4j.cars.repository;

import ru.job4j.cars.model.CarBrand;

import java.util.Collection;
import java.util.Optional;

public interface CarBrandRepository {

	CarBrand save(CarBrand carBrand);

	Optional<CarBrand> findById(int id);

	Collection<CarBrand> findAll();

	boolean deleteById(int id);

	boolean deleteAll();
}
