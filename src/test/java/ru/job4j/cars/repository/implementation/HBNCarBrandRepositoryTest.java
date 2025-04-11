package ru.job4j.cars.repository.implementation;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.CarBrand;
import ru.job4j.cars.repository.CarBrandRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HBNCarBrandRepositoryTest {

	private static CarBrandRepository repository;

	@BeforeAll
	static void setUp() {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		repository = new HBNCarBrandRepository(new CrudRepository(sf));
	}

	@AfterEach
	void cleanUp() {
		repository.deleteAll();
	}

	@Test
	void whenSaveCarBrandThenReturnCarBrand() {
		CarBrand carBrand = new CarBrand();
		carBrand.setName("Toyota");
		carBrand = repository.save(carBrand);
		Optional<CarBrand> savedCar = repository.findById(carBrand.getId());
		assertThat(savedCar).isPresent();
		assertThat(savedCar.get().getName()).isEqualTo(carBrand.getName());
	}

	@Test
	void whenSaveBrandsThenReturnAllBrands() {
		var brands = List.of(new CarBrand(0, "Toyota"), new CarBrand(0, "Tesla"));
		brands.forEach(repository::save);
		var savedBrands = repository.findAll();
		assertThat(savedBrands).hasSize(2);
		assertThat(savedBrands).containsExactly(brands.get(0), brands.get(1));
	}

	@Test
	void whenDeleteByIdThenTrue() {
		var first = new CarBrand(0, "Toyota");
		var second = new CarBrand(0, "Tesla");
		repository.save(first);
		repository.save(second);
		var deleted = repository.deleteById(first.getId());
		assertThat(deleted).isTrue();
		assertThat(repository.findById(first.getId())).isEmpty();
		assertThat(repository.findById(second.getId()).get()).isEqualTo(second);
	}
}