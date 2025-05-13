package ru.job4j.cars.repository;

import ru.job4j.cars.model.Owner;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OwnerRepository {

	Owner save(Owner owner);

	Optional<Owner> findById(int ownerId);

	Optional<Owner> findByUserName(String name);

	Collection<Owner> findAllOrderById();

	boolean deleteById(int ownerId);

	boolean deleteAll();

	void deleteByOwnerIdList(List<Integer> ownerIds);
}
