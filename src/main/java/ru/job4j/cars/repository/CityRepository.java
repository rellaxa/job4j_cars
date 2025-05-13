package ru.job4j.cars.repository;

import ru.job4j.cars.model.City;

import java.util.Collection;
import java.util.Optional;

public interface CityRepository {

	Optional<City> findById(int  cityId);

	Collection<City> getAllCities();

}
