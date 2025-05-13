package ru.job4j.cars.service.owner;

import ru.job4j.cars.model.Owner;
import ru.job4j.cars.service.ModelService;

import java.util.List;
import java.util.Optional;

public interface OwnerService {

	Owner save(Owner owner);

	void deleteByIdList(List<Integer> ownerIds);

	Optional<Owner> findByUserName(String name);
}
