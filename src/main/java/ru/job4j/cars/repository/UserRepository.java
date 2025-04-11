package ru.job4j.cars.repository;

import ru.job4j.cars.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

	Optional<User> save(User user);

	boolean update(User user);

	boolean delete(int userId);

	boolean deleteAll();

	Collection<User> findAllOrderById();

	Optional<User> findById(int userId);

	Collection<User> findByLikeLogin(String key);

	Optional<User> findByLoginAndPassword(String login, String password);
}
