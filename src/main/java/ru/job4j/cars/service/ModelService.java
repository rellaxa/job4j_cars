package ru.job4j.cars.service;

import java.util.Collection;
import java.util.Optional;

public interface ModelService<T> {

	Optional<T> findById(int id);

	Collection<T> findAllOrderById();
}
