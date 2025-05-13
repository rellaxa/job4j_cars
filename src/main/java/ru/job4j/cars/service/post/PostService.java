package ru.job4j.cars.service.post;

import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface PostService {

	Post save(Post post, Collection<FileDto> photos);

	Optional<Post> findById(int postId);

	Collection<Post> getAll();

	Collection<Post> getAllByBrand(String brand);

	boolean update(Post post, PriceHistory newPrice, Collection<FileDto> photos);

	boolean switchStatusByUser(int id, User user, boolean status);

	boolean deleteByIdAndUser(int postId, User user);

	void buyCar(Post post, User user);

}
