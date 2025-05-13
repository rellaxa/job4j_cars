package ru.job4j.cars.service.car;

import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.User;

import java.util.Collection;
import java.util.Optional;

public interface CarService {

	Car save(Car car, User user);

	boolean update(Car car);

	void buyCar(Car car, User user);

	boolean deleteByCarIdAndUserId(int postId, int id);
}
