package ru.job4j.cars.repository.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.UserRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class HBNUserRepository implements UserRepository {

	private final CrudRepository crudRepository;

	/**
	 * Сохранить в базе.
	 * @param user пользователь.
	 * @return пользователь с id.
	 */
	@Override
	public Optional<User> save(User user) {
		log.info("Saving user {}", user);
		try {
			crudRepository.run(session -> session.persist(user));
			return Optional.of(user);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	/**
	 * Найти пользователя по ID
	 * @return пользователь.
	 */
	@Override
	public Optional<User> findById(int userId) {
		log.info("Finding user by id {}", userId);
		return crudRepository.optional(
				"from User where id = :fId", User.class,
				Map.of("fId", userId)
		);
	}

	/**
	 * Список пользователь отсортированных по id.
	 * @return список пользователей.
	 */
	@Override
	public Collection<User> findAllOrderById() {
		log.info("Finding all users");
		return crudRepository.query("from User order by id asc", User.class);
	}

	/**
	 * Список пользователей по login LIKE %key%
	 * @param key key
	 * @return список пользователей.
	 */
	@Override
	public Collection<User> findByLikeLogin(String key) {
		return crudRepository.query(
				"from User where login like :fKey", User.class,
				Map.of("fKey", "%" + key + "%")
		);
	}

	/**
	 * Найти пользователя по login и password.
	 * @param login login,
	 * @param password password.
	 * @return Optional or user.
	 */
	@Override
	public Optional<User> findByLoginAndPassword(String login, String password) {
		log.info("Finding user by login and password {}", login);
		return crudRepository.optional(
				"from User where login = :fLogin and password = :fPassword", User.class,
				Map.of(
						"fLogin", login,
						"fPassword", password
				)
		);
	}

	/**
	 * Обновить в базе пользователя.
	 * @param user пользователь.
	 */
	@Override
	public boolean update(User user) {
		return crudRepository.tx(session -> session.merge(user)) != null;
	}

	/**
	 * Удалить пользователя по id.
	 * @param userId ID
	 */
	@Override
	public boolean deleteById(int userId) {
		return crudRepository.runWithRsl(
				"delete from User where id = :fId",
				Map.of("fId", userId)
		);
	}

	@Override
	public boolean deleteAll() {
		return crudRepository.runWithRsl("delete from User", Map.of());
	}
}