package ru.job4j.cars.service.price.history;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.repository.PriceHistoryRepository;

@Service
@AllArgsConstructor
public class SimplePriceHistoryService implements PriceHistoryService {

	private final PriceHistoryRepository priceHistoryRepository;

	@Override
	public void deleteByPostId(int postId) {
		priceHistoryRepository.deleteByPostId(postId);
	}
}
