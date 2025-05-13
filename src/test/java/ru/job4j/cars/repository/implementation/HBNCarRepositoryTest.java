package ru.job4j.cars.repository.implementation;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.*;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HBNCarRepositoryTest {

	private static CarRepository carRepository;

	private static ClassificationRepository classificationRepository;

	private static CarBrandRepository carBrandRepository;

	private static CarBodyRepository carBodyRepository;

	private static EngineRepository engineRepository;

	private static Classification classification;

	private static CarBrand carBrand;

	private static CarBody carBody;

	private static Engine engine;

	@BeforeAll
	static void setUp() {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		carRepository = new HBNCarRepository(new CrudRepository(sf));
		carBrandRepository = new HBNCarBrandRepository(new CrudRepository(sf));
		engineRepository = new HBNEngineRepository(new CrudRepository(sf));
		classificationRepository = new HBNClassificationRepository(new CrudRepository(sf));
		carBodyRepository = new HBNCarBodyRepository(new CrudRepository(sf));
	}

	@BeforeEach
	void updateStorage() {
		carBrand = carBrandRepository.save(new CarBrand(0, "Tesla"));
		engine = engineRepository.save(new Engine(0, "Engine"));
		classification = classificationRepository.findById(1).get();
		carBody = carBodyRepository.findById(1).get();
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
				.classCar(classification)
				.body(carBody)
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
				Car.builder().name("One").brand(carBrand).classCar(classification).body(carBody).engine(engine).build(),
				Car.builder().name("Two").brand(carBrand).classCar(classification).body(carBody).engine(engine).build(),
				Car.builder().name("Three").brand(carBrand).classCar(classification).body(carBody).engine(engine).build()
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
				.classCar(classification)
				.body(carBody)
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
	void whenDeleteByIdExistingCarThenTrue() {
		Car car = getDefaultCar();
		carRepository.save(car);
		int carId = car.getId();
		var isDeleted = carRepository.deleteById(carId);
		assertThat(isDeleted).isTrue();
		assertThat(carRepository.findAllOrderById()).isEmpty();
	}

	@Test
	void whenDeleteByIdNonExistingCarThenFalse() {
		Car car = getDefaultCar();
		carRepository.save(car);
		int carId = car.getId();
		var isDeleted = carRepository.deleteById(carId + 1000);
		assertThat(isDeleted).isFalse();
		assertThat(carRepository.findAllOrderById()).hasSize(1);
	}
}