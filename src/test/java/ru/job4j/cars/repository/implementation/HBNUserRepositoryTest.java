package ru.job4j.cars.repository.implementation;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.UserRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HBNUserRepositoryTest {

	private static UserRepository repository;

	@BeforeAll
	static void setUp() {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		repository = new HBNUserRepository(new CrudRepository(sf));
	}

	@AfterEach
	void cleanUp() {
		repository.deleteAll();
	}

	@Test
	void whenSaveNewUserThenReturnSavedUser() {
		User newUser = new User(0, "Login", "Password");
		Optional<User> savedUser = repository.save(newUser);
		assertThat(savedUser).isPresent();
		assertThat(savedUser.get().getLogin()).isEqualTo("Login");
	}

	@Test
	void whenSaveNewUserWithTheSameLoginThenReturnEmpty() {
		User user = new User(0, "Login", "Password");
		repository.save(user);
		Optional<User> savedUser = repository.save(user);
		assertThat(savedUser).isEmpty();
	}

	@Test
	void whenUpdateExistingUserThenReturnUpdatedUser() {
		User user = new User(0, "Login", "Password");
		Optional<User> savedUser = repository.save(user);
		user.setLogin("Update");
		boolean updated = repository.update(user);
		assertThat(updated).isTrue();
		assertThat(repository.findById(user.getId()).get().getLogin()).isEqualTo("Update");
	}

	@Test
	void whenSaveUsersThenFindAllUsers() {
		User one = new User(0, "One", "Password");
		User two = new User(0, "Two", "Password");
		User three = new User(0, "Three", "Password");
		repository.save(one);
		repository.save(two);
		repository.save(three);
		Collection<User> foundUsers = repository.findAllOrderById();
		Collection<User> expectedUsers = List.of(
				one, two, three
		);
		assertThat(foundUsers.size()).isEqualTo(3);
		assertThat(foundUsers).isEqualTo(expectedUsers);
	}

	@Test
	void whenDeleteExistingUserThenTrue() {
		User user = new User(0, "Login", "Password");
		repository.save(user);
		boolean deleted = repository.delete(user.getId());
		assertThat(deleted).isTrue();
		assertThat(repository.findById(user.getId())).isEmpty();
	}

	@Test
	void whenFindByLoginAndPasswordThenReturnUser() {
		User user = new User(0, "Login", "Password");
		repository.save(user);
		Optional<User> foundUser = repository.findByLoginAndPassword(user.getLogin(), user.getPassword());
		assertThat(foundUser).isPresent();
		assertThat(foundUser.get()).isEqualToComparingFieldByField(user);
	}
}