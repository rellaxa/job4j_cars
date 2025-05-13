package ru.job4j.cars.service.owner.history;

import ru.job4j.cars.model.HistoryOwner;

import java.util.Collection;
import java.util.List;

public interface HistoryOwnerService {

	HistoryOwner save(HistoryOwner historyOwner);

	Collection<HistoryOwner> getHistoryOwnersByCarId(int carId);

	void updateTime(HistoryOwner lastHistoryOwner);

	List<Integer> deleteByCarId(int carId);
}
