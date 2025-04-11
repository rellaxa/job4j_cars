package ru.job4j.cars.repository.implementation;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.CarBrand;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.CarBrandRepository;
import ru.job4j.cars.repository.CarRepository;
import ru.job4j.cars.repository.EngineRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HBNCarRepositoryTest {

	private static CarRepository carRepository;

	private static CarBrandRepository carBrandRepository;

	private static EngineRepository engineRepository;

	private static CarBrand carBrand;

	private static Engine engine;

	@BeforeAll
	static void setUp() {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		carRepository = new HBNCarRepository(new CrudRepository(sf));
		carBrandRepository = new HBNCarBrandRepository(new CrudRepository(sf));
		engineRepository = new HBNEngineRepository(new CrudRepository(sf));
	}

	@BeforeEach
	void updateStorage() {
		carBrand = carBrandRepository.save(new CarBrand(0, "Tesla"));
		engine = engineRepository.save(new Engine(0, "Engine"));
	}

	@AfterEach
	void cleanStorage() {
		carRepository.deleteAll();
		carBrandRepository.deleteAll();
		engineRepository.deleteAll();
	}

	private Car getDefaultCar() {
		return Car.builder()
				.name("Tesla Model S")
				.brand(carBrand)
				.engine(engine)
				.build();
	}

	@Test
	void whenSaveCarThenReturnSameCar() {
		Car car = getDefaultCar();
		car = carRepository.save(car);
		Optional<Car> savedCar = carRepository.findById(car.getId());
		assertThat(savedCar).isPresent();
		assertThat(savedCar.get().getName()).isEqualTo(car.getName());
	}

	@Test
	void whenSaveCarsAndFindAllThenReturnAllCars() {
		List<Car> cars = List.of(
				Car.builder().name("One").brand(carBrand).engine(engine).build(),
				Car.builder().name("Two").brand(carBrand).engine(engine).build(),
				Car.builder().name("Three").brand(carBrand).engine(engine).build()
		);
		cars.forEach(car -> carRepository.save(car));
		Collection<Car> findCars = carRepository.findAllOrderById();
		assertThat(findCars).hasSize(3);
		assertThat(cars).isEqualTo(findCars);
	}

	@Test
	void whenUpdateExistingCarThenReturnUpdatedCar() {
		Car car = getDefaultCar();
		carRepository.save(car);
		int carId = car.getId();
		Car updatedCar = Car.builder()
				.id(carId)
				.name("Xiaomi SU7")
				.brand(carBrand)
				.engine(engine)
				.build();
		boolean updated = carRepository.update(updatedCar);
		Optional<Car> findCar = carRepository.findById(carId);
		assertThat(updated).isTrue();
		assertThat(findCar).isPresent();
		assertThat(findCar.get().getName()).isEqualTo(updatedCar.getName());
	}

	@Test
	void whenDeleteExistingCarThenTrue() {
		Car car = getDefaultCar();
		carRepository.save(car);
		int carId = car.getId();
		boolean deleted = carRepository.delete(carId);
		assertThat(deleted).isTrue();
		assertThat(carRepository.findAllOrderById()).isEmpty();
	}

	@Test
	void whenDeleteNonExistingCarThenFalse() {
		Car car = getDefaultCar();
		carRepository.save(car);
		int carId = car.getId();
		boolean deleted = carRepository.delete(carId + 1000);
		assertThat(deleted).isFalse();
		assertThat(carRepository.findAllOrderById()).hasSize(1);
	}
}