package ru.job4j.cars.repository.implementation;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.*;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HBNPostRepositoryTest {

	private static PostRepository repository;

	private static UserRepository userRepository;

	private static CarRepository carRepository;

	private static CarBrandRepository carBrandRepository;

	private static EngineRepository engineRepository;

	private static FileRepository fileRepository;

	private static CarBrand carBrand;

	private static Engine engine;

	private static User user;

	private static Car car;

	@BeforeAll
	static void setUp() {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		repository = new HBNPostRepository(new CrudRepository(sf));
		userRepository = new HBNUserRepository(new CrudRepository(sf));
		engineRepository = new HBNEngineRepository(new CrudRepository(sf));
		carBrandRepository = new HBNCarBrandRepository(new CrudRepository(sf));
		carRepository = new HBNCarRepository(new CrudRepository(sf));
		fileRepository = new HBNFileRepository(new CrudRepository(sf));

		carBrand = carBrandRepository.save(new CarBrand(0, "Default"));
		engine = engineRepository.save(new Engine(0, "Engine"));
		car = carRepository.save(Car.builder()
				.name("Default")
				.brand(carBrand)
				.engine(engine)
				.build()
		);
		user = userRepository.save(new User(0, "Login", "Password")).get();
	}

	@AfterEach
	void deletePosts() {
		fileRepository.deleteAll();
		repository.findAllOrderById().forEach(post -> repository.delete(post.getId()));
	}

	@AfterAll
	static void cleanUpStorage() {
		userRepository.deleteAll();
		carRepository.deleteAll();
		engineRepository.deleteAll();
		carBrandRepository.deleteAll();
	}

	@Test
	void whenSavePostThenReturnSavedPost() {
		Post post = Post.builder().description("Test description").car(car).user(user).build();
		repository.save(post);
		Optional<Post> savedPost = repository.findById(post.getId());
		assertThat(savedPost).isPresent();
		assertThat(savedPost.get().getDescription()).isEqualTo(post.getDescription());
	}

	@Test
	void whenUpdatePostThenReturnUpdatedPost() {
		Post post = Post.builder().description("Test description").car(car).user(user).build();
		repository.save(post);
		post.setDescription("Updated description");
		boolean updated = repository.update(post);
		Optional<Post> updatedPost = repository.findById(post.getId());
		assertThat(updated).isTrue();
		assertThat(updatedPost).isPresent();
		assertThat(updatedPost.get().getDescription()).isEqualTo("Updated description");
	}

	@Test
	void whenSavePostsThenFindAll() {
		Post one = Post.builder().description("one").car(car).user(user).build();
		Post two = Post.builder().description("two").car(car).user(user).build();
		Post three = Post.builder().description("three").car(car).user(user).build();
		repository.save(one);
		repository.save(two);
		repository.save(three);
		Collection<Post> foundPosts = repository.findAllOrderById();
		assertThat(foundPosts).hasSize(3);
		assertThat(foundPosts).isEqualTo(List.of(one, two, three));
	}

	@Test
	void whenDeletePostThenTrue() {
		Post post = Post.builder().description("one").car(car).user(user).build();
		repository.save(post);
		boolean deleted = repository.delete(post.getId());
		assertThat(deleted).isTrue();
		assertThat(repository.findById(post.getId())).isEmpty();
	}

	@Test
	void whenFindPostsWithPhoto() {
		fileRepository.deleteAll();
		Post one = Post.builder().description("one").car(car).user(user).build();
		Post two = Post.builder().description("two").car(car).user(user).build();
		Post three = Post.builder().description("three").car(car).user(user).build();
		repository.save(one);
		repository.save(two);
		repository.save(three);
		List<File> files = List.of(
				new File(0, "1", "path1", one.getId()),
				new File(0, "2", "path2", one.getId()),
				new File(0, "3", "path3", one.getId()),
				new File(0, "2", "path4", two.getId())
		);
		files.forEach(file -> fileRepository.save(file));
		Collection<Post> foundPosts = repository.findPostsWithPhoto();
		Collection<Post> expectedPosts = List.of(one, two);
		assertThat(foundPosts).hasSize(2);
		assertThat(foundPosts).isEqualTo(expectedPosts);
	}

	@Test
	void whenFindPostsByBrand() {
		CarBrand tesla = new CarBrand(0, "Tesla");
		CarBrand ford = new CarBrand(0, "Ford");
		CarBrand toyota = new CarBrand(0, "Toyota");
		List.of(tesla, ford, toyota).forEach(brand -> carBrandRepository.save(brand));
		Car oneTesla = Car.builder().name("Tesla Model S").brand(tesla).engine(engine).build();
		Car twoTesla = Car.builder().name("Tesla Model Y").brand(tesla).engine(engine).build();
		Car threeTesla = Car.builder().name("Cybertruck").brand(tesla).engine(engine).build();
		Car carFord = Car.builder().name("F-150 Lightning®").brand(ford).engine(engine).build();
		Car carToyota = Car.builder().name("2025 Sequoia").brand(toyota).engine(engine).build();
		List.of(oneTesla, twoTesla, threeTesla, carFord, carToyota)
				.forEach(car -> carRepository.save(car));
		Post one = Post.builder().description("1").car(oneTesla).user(user).build();
		Post two = Post.builder().description("2").car(twoTesla).user(user).build();
		Post three = Post.builder().description("3").car(threeTesla).user(user).build();
		Post four = Post.builder().description("4").car(carFord).user(user).build();
		Post five = Post.builder().description("5").car(carToyota).user(user).build();
		List.of(one, two, three, four, five)
				.forEach(post -> repository.save(post));
		List<Post> teslaPosts = repository.findAllPostsByBrand("Tesla").stream()
				.toList();
		Collection<Post> fordPosts = repository.findAllPostsByBrand("Ford");
		assertThat(teslaPosts).hasSize(3);
		assertThat(teslaPosts.get(0).getCar().getName()).isEqualTo("Tesla Model S");
		assertThat(teslaPosts.get(1).getCar().getName()).isEqualTo("Tesla Model Y");
		assertThat(teslaPosts.get(2).getCar().getName()).isEqualTo("Cybertruck");
		assertThat(fordPosts).hasSize(1);
		assertThat(fordPosts.iterator().next().getCar().getName()).isEqualTo("F-150 Lightning®");
	}

	@Test
	void whenFindAllPostsByLastDay() {
		LocalDateTime today = LocalDateTime.now();
		Post one = Post.builder().description("1").created(today).car(car).user(user).build();
		Post two = Post.builder().description("2").created(today.plusHours(5)).car(car).user(user).build();
		Post three = Post.builder().description("3").created(today.minusDays(1)).car(car).user(user).build();
		Post four = Post.builder().description("4").created(today.plusDays(1)).car(car).user(user).build();
		List.of(one, two, three, four)
				.forEach(post -> repository.save(post));
		Collection<Post> result = repository.findAllPostsByLastDay(today.minusHours(1), today.plusHours(10));
		Collection<Post> expectedPosts = List.of(one, two);
		assertThat(result.size()).isEqualTo(2);
		assertThat(result).isEqualTo(expectedPosts);
	}
}