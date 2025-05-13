package ru.job4j.cars.repository;

import ru.job4j.cars.model.Post;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface PostRepository {

	Post save(Post post);

	Optional<Post> findById(int postId);

	Collection<Post> findAllOrderById();

	Collection<Post> findAllPostsByLastDay(LocalDateTime from, LocalDateTime to);

	Collection<Post> findAllPostsByBrand(String brand);

	Collection<Post> findPostsWithPhoto();

	boolean update(Post post);

	boolean switchStatusByUser(int id, int userId, boolean status);

	int deleteByIdAndUser(int postId, int userId);

	boolean delete(int postId);
}
