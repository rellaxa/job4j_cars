package ru.job4j.cars.repository.implementation;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.OwnerRepository;
import ru.job4j.cars.repository.UserRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HBNOwnerRepositoryTest {

	private static OwnerRepository repository;

	private static UserRepository userRepository;

	private static User user;

	@BeforeAll
	static void setUp() {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		repository = new HBNOwnerRepository(new CrudRepository(sf));
		userRepository = new HBNUserRepository(new CrudRepository(sf));
	}

	@BeforeEach
	void updateUserStorage() {
		user = new User(0, "Name", "Login", "Password");
		userRepository.save(user);
	}

	@AfterEach
	void cleanUp() {
		repository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	void whenSaveOwnerThenReturnOwner() {
		Owner owner = new Owner(0, "owner", user);
		repository.save(owner);
		Optional<Owner> savedOwner = repository.findById(owner.getId());
		assertThat(savedOwner).isPresent();
		assertThat(savedOwner.get().getName()).isEqualTo(owner.getName());
	}

	@Test
	void whenSaveOwnersThenReturnOwners() {
		List<Owner> owners = List.of(
				new Owner(0, "one", user),
				new Owner(0, "two", user),
				new Owner(0, "three", user)
		);
		owners.forEach(owner -> repository.save(owner));
		Collection<Owner> foundOwners = repository.findAllOrderById();
		assertThat(foundOwners).hasSize(3);
		assertThat(foundOwners).isEqualTo(owners);
	}

	@Test
	void whenDeleteExistingThenReturnTrue() {
		Owner owner = new Owner(0, "one", user);
		repository.save(owner);
		int id = owner.getId();
		boolean deleted = repository.deleteById(id);
		assertThat(deleted).isTrue();
		assertThat(repository.findById(id)).isEmpty();
	}

	@Test
	void whenDeletedNonExistingThenReturnFalse() {
		Owner owner = new Owner(0, "one", user);
		repository.save(owner);
		int id = owner.getId();
		boolean deleted = repository.deleteById(id + 1000);
		assertThat(deleted).isFalse();
		assertThat(repository.findById(id)).isPresent();
	}
}