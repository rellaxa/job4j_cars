package ru.job4j.cars.repository.implementation;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.repository.CarRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class HBNCarRepository implements CarRepository {

	private final CrudRepository crudRepository;

	/**
	 * Сохранить в базе.
	 * @param car автомобиль.
	 * @return пользователь с id.
	 */
	@Override
	public Car save(Car car) {
		crudRepository.run(session -> session.persist(car));
		return car;
	}

	/**
	 * Найти автомобиль по ID
	 * @param carId ID
	 * @return автомобиль.
	 */
	@Override
	public Optional<Car> findById(int carId) {
		return crudRepository.optional(
				"from Car where id = :fId", Car.class,
				Map.of("fId", carId)
		);
	}

	/**
	 * Список автомобилей отсортированных по id.
	 * @return список автомобилей.
	 */
	@Override
	public Collection<Car> findAllOrderById() {
		return crudRepository.query("from Car order by id asc", Car.class);
	}

	/**
	 * Обновить в базе автомобиль.
	 * @param car автомобиль.
	 */
	@Override
	public boolean update(Car car) {
		return crudRepository.tx(session -> session.merge(car)) != null;
	}

	/**
	 * Удалить автомобиль по id.
	 * @param carId ID
	 */
	@Override
	public boolean delete(int carId) {
		return crudRepository.runWithRsl(
				"delete from Car where id = :fId",
				Map.of("fId", carId)
		);
	}

	/**
	 * Удалить все автомобили.
	 * @return true если успешно удалены/
	 */
	@Override
	public boolean deleteAll() {
		return crudRepository.runWithRsl("delete from Car", Map.of());
	}
}
