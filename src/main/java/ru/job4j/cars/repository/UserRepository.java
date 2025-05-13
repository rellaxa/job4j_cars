package ru.job4j.cars.repository;

import ru.job4j.cars.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

	Optional<User> save(User user);

	Optional<User> findById(int userId);

	Collection<User> findByLikeLogin(String key);

	Collection<User> findAllOrderById();

	Optional<User> findByLoginAndPassword(String login, String password);

	boolean update(User user);

	boolean deleteById(int userId);

	boolean deleteAll();
}
