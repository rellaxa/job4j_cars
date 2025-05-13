package ru.job4j.cars.service.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleUserService implements UserService {

	private final UserRepository userRepository;

	@Override
	public Optional<User> save(User user) {
		return userRepository.save(user);
	}

	@Override
	public Optional<User> findByLoginAndPassword(String login, String password) {
		return userRepository.findByLoginAndPassword(login, password);
	}
}
