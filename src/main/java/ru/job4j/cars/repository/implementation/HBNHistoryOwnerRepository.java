package ru.job4j.cars.repository.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.HistoryOwner;
import ru.job4j.cars.repository.HistoryOwnerRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@AllArgsConstructor
public class HBNHistoryOwnerRepository implements HistoryOwnerRepository {

	private final CrudRepository crudRepository;

	@Override
	public HistoryOwner save(HistoryOwner historyOwner) {
		log.info("Saving historyOwner {}", historyOwner);
		crudRepository.run(session -> session.persist(historyOwner));
		return historyOwner;
	}

	@Override
	public Collection<HistoryOwner> getHistoryOwnersByCarId(int carId) {
		log.info("Finding history owners by car id: {}", carId);
		return crudRepository.query("from HistoryOwner where car_id = :carId", HistoryOwner.class,
				Map.of("carId", carId));
	}

	@Override
	public void updateTime(HistoryOwner lastHistoryOwner) {
		log.info("Updating last history owner time: {}", lastHistoryOwner);
		crudRepository.runWithRsl("update HistoryOwner set end_at = :fEndAt where id = :fId",
				Map.of("fEndAt", LocalDateTime.now(),
						"fId", lastHistoryOwner.getId()
				)
		);
	}

	@Override
	public List<Integer> deleteByCarId(int carId) {
		log.info("Deleting History owners by carId: {}", carId);
		var ownerIds = crudRepository.query("select h.owner.id from HistoryOwner h where car_id = :fCarId",
				Integer.class, Map.of("fCarId", carId));
		crudRepository.runWithRsl("delete from HistoryOwner where car_id = :fCarId",
				Map.of("fCarId", carId));
		log.info("Owners ids: {}", ownerIds);
		return ownerIds;
	}
}
