package ru.job4j.cars.service.owner.history;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.HistoryOwner;
import ru.job4j.cars.repository.HistoryOwnerRepository;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class SimpleHistoryOwnerService implements HistoryOwnerService {

	private final HistoryOwnerRepository historyOwnerRepository;

	@Override
	public HistoryOwner save(HistoryOwner historyOwner) {
		return historyOwnerRepository.save(historyOwner);
	}

	@Override
	public Collection<HistoryOwner> getHistoryOwnersByCarId(int carId) {
		return historyOwnerRepository.getHistoryOwnersByCarId(carId);
	}

	@Override
	public void updateTime(HistoryOwner lastHistoryOwner) {
		historyOwnerRepository.updateTime(lastHistoryOwner);
	}

	@Override
	public List<Integer> deleteByCarId(int carId) {
		return historyOwnerRepository.deleteByCarId(carId);
	}
}
