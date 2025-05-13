package ru.job4j.cars.repository.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.PostRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class HBNPostRepository implements PostRepository {

	private final CrudRepository crudRepository;

	@Override
	public Post save(Post post) {
		log.info("Saving post: {}", post);
		crudRepository.run(session -> session.persist(post));
		return post;
	}

	@Override
	public Optional<Post> findById(int postId) {
		log.info("Finding post by id: {}", postId);
		return crudRepository.optional("from Post p where p.id = :fId", Post.class,
				Map.of("fId", postId));
	}

	@Override
	public Collection<Post> findAllPostsByBrand(String brand) {
		log.info("Finding all posts by {}", brand);
		return crudRepository.query("""
						SELECT p from Post p
							JOIN Car c
								ON c.id = p.car.id
						WHERE c.brand.name = :fBrand
						ORDER by p.id ASC
						""", Post.class,
				Map.of("fBrand", brand)
		);
	}

	@Override
	public Collection<Post> findAllOrderById() {
		log.info("Finding all posts by order by id");
		return crudRepository.query("from Post p order by p.id asc", Post.class);
	}

	@Override
	public Collection<Post> findPostsWithPhoto() {
		log.info("Finding posts with photo");
		return crudRepository.query("""
				    SELECT DISTINCT p
				    FROM Post p
				    	JOIN FETCH p.photos f
				    	JOIN FETCH p.car
				    ORDER BY p.id ASC
				""", Post.class);
	}

	@Override
	public Collection<Post> findAllPostsByLastDay(LocalDateTime from, LocalDateTime to) {
		log.info("Finding all posts by last day");
		return crudRepository.query(
				"from Post p WHERE p.created between :from and :to", Post.class,
				Map.of("from", from,
						"to", to)
		);
	}

	@Override
	public boolean update(Post post) {
		log.info("Updating post: {}", post);
		return crudRepository.tx(session -> session.merge(post)) != null;
	}

	@Override
	public boolean switchStatusByUser(int postId, int userId, boolean status) {
		log.info("Switching status by postId: {} and userId: {}", postId, userId);
		return crudRepository.runWithRsl("update Post set status = :fStatus where id = :fId and auto_user_id = :fUserId",
				Map.of("fId", postId,
						"fUserId", userId,
						"fStatus", status
				)
		);
	}

	@Override
	public int deleteByIdAndUser(int postId, int userId) {
		log.info("Deleting post by id: {} and userId: {}", postId, userId);
		var carId = crudRepository.optional("select p.car.id from Post p where id = :fPostId", Integer.class,
				Map.of("fPostId", postId)).get();
		crudRepository.runWithRsl("delete from Post where id = :fPostId and auto_user_id = :fUserId",
				Map.of("fPostId", postId,
						"fUserId", userId)
		);
		log.info("CarId: {}", carId);
		return carId;
	}

	@Override
	public boolean delete(int postId) {
		log.info("Deleting post: {}", postId);
		return crudRepository.runWithRsl("delete from Post where id = :fId",
				Map.of("fId", postId));
	}
}