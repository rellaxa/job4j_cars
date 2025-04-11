package ru.job4j.cars.repository.implementation;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.repository.OwnerRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class HBNOwnerRepository implements OwnerRepository {

	private final CrudRepository crudRepository;

	@Override
	public Owner save(Owner owner) {
		crudRepository.run(session -> session.persist(owner));
		return owner;
	}

	@Override
	public Collection<Owner> findAllOrderById() {
		return crudRepository.query("from Owner order by id asc", Owner.class);
	}

	@Override
	public Optional<Owner> findById(int ownerId) {
		return crudRepository.optional(
				"from Owner where id = :fId", Owner.class,
				Map.of("fId", ownerId)
		);
	}

	@Override
	public boolean delete(int ownerId) {
		return crudRepository.runWithRsl(
				"delete from Owner where id = :fId",
				Map.of("fId", ownerId)
		);
	}

	@Override
	public boolean deleteAll() {
		return crudRepository.runWithRsl("DELETE FROM Owner", Map.of());
	}
}
