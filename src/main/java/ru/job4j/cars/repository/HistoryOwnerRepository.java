package ru.job4j.cars.repository;

import ru.job4j.cars.model.HistoryOwner;

import java.util.Collection;
import java.util.List;

public interface HistoryOwnerRepository {

	HistoryOwner save(HistoryOwner historyOwner);

	Collection<HistoryOwner> getHistoryOwnersByCarId(int carId);

	void updateTime(HistoryOwner lastHistoryOwner);

	List<Integer> deleteByCarId(int carId);
}
