package ru.job4j.cars.repository.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.repository.OwnerRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class HBNOwnerRepository implements OwnerRepository {

	private final CrudRepository crudRepository;

	@Override
	public Owner save(Owner owner) {
		log.info("Saving owner {}", owner);
		crudRepository.run(session -> session.persist(owner));
		return owner;
	}

	@Override
	public Collection<Owner> findAllOrderById() {
		log.info("Finding all owner by id");
		return crudRepository.query("from Owner order by id asc", Owner.class);
	}

	@Override
	public Optional<Owner> findById(int ownerId) {
		log.info("Finding owner by {}", ownerId);
		return crudRepository.optional(
				"from Owner where id = :fId", Owner.class,
				Map.of("fId", ownerId)
		);
	}

	@Override
	public Optional<Owner> findByUserName(String name) {
		return crudRepository.optional("from Owner where owner_name = :fOwnerName",
				Owner.class, Map.of("fOwnerName", name));
	}

	@Override
	public boolean deleteById(int ownerId) {
		log.info("Deleting owner by {}", ownerId);
		return crudRepository.runWithRsl(
				"delete from Owner where id = :fId",
				Map.of("fId", ownerId)
		);
	}

	@Override
	public void deleteByOwnerIdList(List<Integer> ownerIds) {
		log.info("Deleting all owner by ids: {}", ownerIds);
		crudRepository.runWithRsl(
				"delete from Owner o where o.id in :ids and o.id not in (select ho.owner.id from HistoryOwner ho)",
				Map.of("ids", ownerIds));
	}

	@Override
	public boolean deleteAll() {
		log.info("Deleting all owners");
		return crudRepository.runWithRsl("DELETE FROM Owner", Map.of());
	}
}
