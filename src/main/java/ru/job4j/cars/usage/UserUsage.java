package ru.job4j.cars.usage;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.implementation.HBNUserRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

public class UserUsage {

	public static void main(String[] args) {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure("hibernate.cfg.xml").build();
		try (SessionFactory sf = new MetadataSources(registry)
				.buildMetadata().buildSessionFactory()) {
			var crudRepository = new CrudRepository(sf);
			var userRepository = new HBNUserRepository(crudRepository);
			var user = new User();
			user.setLogin("admin");
			user.setPassword("admin");
            userRepository.save(user);

			userRepository.findAllOrderById()
					.forEach(System.out::println);
			userRepository.findByLikeLogin("e")
					.forEach(System.out::println);
			userRepository.findById(user.getId())
					.ifPresent(System.out::println);

			var optUser = userRepository.findByLoginAndPassword("admin", "admin");
			user = optUser.get();
			optUser.ifPresent(System.out::println);

			userRepository.deleteById(user.getId());
			userRepository.findAllOrderById()
					.forEach(System.out::println);
		} finally {
			StandardServiceRegistryBuilder.destroy(registry);
		}
	}
}