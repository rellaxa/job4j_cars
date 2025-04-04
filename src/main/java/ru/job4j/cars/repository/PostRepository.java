package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class PostRepository {

	private final CrudRepository crudRepository;

	public Post create(Post post) {
		crudRepository.run(session -> session.persist(post));
		return post;
	}

	public boolean update(Post post) {
		return crudRepository.tx(session -> session.merge(post)) != null;
	}

	public boolean delete(int postId) {
		return crudRepository.run("delete from Post where id = :fId",
				Map.of("fId", postId));
	}

	public Optional<Post> findById(int postId) {
		return crudRepository.optional("from Post p JOIN FETCH p.photos where p.id = :fId", Post.class,
				Map.of("fId", postId));
	}

	public List<Post> findAllOrderById() {
		return crudRepository.query("from Post p JOIN FETCH p.photos order by p.id asc", Post.class);
	}

	public List<Post> findAllPostsByLastDay() {
		return crudRepository.query("from Post p WHERE DATE(p.created) = :fDate", Post.class,
				Map.of("fDate", LocalDate.now()));
	}

	public List<Post> findAllPostsWithPhotos() {
		return crudRepository.query("from Post p WHERE SIZE(p.photos) > 0 ORDER BY p.id ASC", Post.class);
	}

	public List<Post> findAllPostsByBrand(String brand) {
		return crudRepository.query("SELECT DISTINCT p from Post p JOIN Car c ON c.id = p.car.id WHERE c.brand.name = :fBrand ", Post.class,
				Map.of("fBrand", brand));
	}

	public static void main(String[] args) {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure().build();
		try (SessionFactory sf = new MetadataSources(registry)
				.buildMetadata().buildSessionFactory()) {
			var postRepository = new PostRepository(new CrudRepository(sf));
			System.out.println(postRepository.findById(12));
			System.out.println(postRepository.findAllOrderById());
			System.out.println(postRepository.findAllPostsByLastDay());
			System.out.println(postRepository.findAllPostsWithPhotos());
			System.out.println(postRepository.findAllPostsByBrand("Ford"));
		} finally {
			StandardServiceRegistryBuilder.destroy(registry);
		}
	}
}
