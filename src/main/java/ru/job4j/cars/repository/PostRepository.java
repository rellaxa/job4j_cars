package ru.job4j.cars.repository;

import ru.job4j.cars.model.Post;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface PostRepository {

	Post save(Post post);

	boolean update(Post post);

	boolean delete(int postId);

	Optional<Post> findById(int postId);

	Collection<Post> findAllOrderById();

	Collection<Post> findAllPostsByLastDay(LocalDateTime from, LocalDateTime to);

	Collection<Post> findPostsWithPhoto();

	Collection<Post> findAllPostsByBrand(String brand);
}
