package ru.job4j.cars.repository.implementation;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.PostRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class HBNPostRepository implements PostRepository {

	private final CrudRepository crudRepository;

	@Override
	public Post save(Post post) {
		crudRepository.run(session -> session.persist(post));
		return post;
	}

	@Override
	public boolean update(Post post) {
		return crudRepository.tx(session -> session.merge(post)) != null;
	}

	@Override
	public boolean delete(int postId) {
		return crudRepository.runWithRsl("delete from Post where id = :fId",
				Map.of("fId", postId));
	}

	@Override
	public Optional<Post> findById(int postId) {
		return crudRepository.optional("from Post p where p.id = :fId", Post.class,
				Map.of("fId", postId));
	}

	@Override
	public Collection<Post> findAllOrderById() {
		return crudRepository.query("from Post p order by p.id asc", Post.class);
	}

	@Override
	public Collection<Post> findAllPostsByLastDay(LocalDateTime from, LocalDateTime to) {
		return crudRepository.query(
				"from Post p WHERE p.created between :from and :to", Post.class,
				Map.of("from", from,
						"to", to)
		);
	}

	@Override
	public Collection<Post> findPostsWithPhoto() {
		return crudRepository.query("""
				    SELECT DISTINCT p
				    FROM Post p
				    	JOIN FETCH p.photos
				    	JOIN FETCH p.car
				    	JOIN FETCH File f ON p.id = f.postId
				    ORDER BY p.id ASC
				""", Post.class);
	}

	@Override
	public Collection<Post> findAllPostsByBrand(String brand) {
		return crudRepository.query("""
						SELECT DISTINCT p from Post p
							JOIN Car c
								ON c.id = p.car.id
						WHERE c.brand.name = :fBrand
						ORDER by p.id ASC
						""", Post.class,
				Map.of("fBrand", brand)
		);
	}

}