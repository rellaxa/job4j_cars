package ru.job4j.cars.repository.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.repository.PriceHistoryRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.Map;

@Slf4j
@Repository
@AllArgsConstructor
public class HBNPriceHistoryRepository implements PriceHistoryRepository {

	private final CrudRepository crudRepository;

	@Override
	public void deleteByPostId(int postId) {
		log.info("deleting price history by postId: {}", postId);
		crudRepository.runWithRsl("delete from PriceHistory where post_id = :fPostId",
				Map.of("fPostId", postId));
	}
}
