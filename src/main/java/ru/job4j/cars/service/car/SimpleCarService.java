package ru.job4j.cars.service.car;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.HistoryOwner;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CarRepository;
import ru.job4j.cars.service.engine.EngineService;
import ru.job4j.cars.service.owner.OwnerService;
import ru.job4j.cars.service.owner.history.HistoryOwnerService;

import java.util.*;

@Service
@AllArgsConstructor
public class SimpleCarService implements CarService {

	private final EngineService engineService;

	private final CarRepository carRepository;

	private final HistoryOwnerService historyOwnerService;

	private final OwnerService ownerService;

	@Override
	public Car save(Car car, User user) {
		engineService.save(car.getEngine());
		var historyOwner = getHistoryOwner(car, user);
		car.getOwnersHistory().add(historyOwner);
		return carRepository.save(car);
	}

	@Override
	public boolean update(Car car) {
		var historyOwners = historyOwnerService.getHistoryOwnersByCarId(car.getId());
		car.setOwnersHistory(new HashSet<>(historyOwners));
		engineService.update(car.getEngine());
		return carRepository.update(car);
	}

	@Override
	public void buyCar(Car car, User user) {
		var historyOwners = historyOwnerService.getHistoryOwnersByCarId(car.getId());
		var lastHistoryOwner = new ArrayList<>(historyOwners).get(historyOwners.size() - 1);
		historyOwnerService.updateTime(lastHistoryOwner);

		var historyOwner = getHistoryOwner(car, user);
		historyOwnerService.save(historyOwner);
	}

	private HistoryOwner getHistoryOwner(Car car, User user) {
		var owner = ownerService.findByUserName(user.getName())
				.orElseGet(() -> {
					Owner o = new Owner();
					o.setName(user.getName());
					o.setUser(user);
					return ownerService.save(o);
				});
		var historyOwner = new HistoryOwner();
		historyOwner.setCar(car);
		historyOwner.setOwner(owner);
		return historyOwner;
	}

	@Override
	public boolean deleteByCarIdAndUserId(int carId, int userId) {
		var ownerIds = historyOwnerService.deleteByCarId(carId);
		ownerService.deleteByIdList(ownerIds);
		var engineId = engineService.findEngineIdByCarId(carId).get();
		carRepository.deleteById(carId);
		return engineService.deleteById(engineId);
	}

}
