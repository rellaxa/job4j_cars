package ru.job4j.cars.repository;

import ru.job4j.cars.model.Owner;

import java.util.Collection;
import java.util.Optional;

public interface OwnerRepository {

	Owner save(Owner owner);

	Collection<Owner> findAllOrderById();

	Optional<Owner> findById(int ownerId);

	boolean delete(int ownerId);

	boolean deleteAll();
}
