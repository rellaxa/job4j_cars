package ru.job4j.cars.service.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.PostRepository;
import ru.job4j.cars.service.car.CarService;
import ru.job4j.cars.service.file.FileService;
import ru.job4j.cars.service.price.history.PriceHistoryService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimplePostService implements PostService {

	private final PostRepository postRepository;

	private final FileService fileService;

	private final CarService carService;

	private final PriceHistoryService priceHistoryService;

	@Override
	public Post save(Post post, Collection<FileDto> dtoFiles) {
		post.setCreated(LocalDateTime.now());
		var photos = post.getPhotos();
		dtoFiles.forEach(dtoFile -> photos.add(fileService.save(dtoFile)));
		return postRepository.save(post);
	}

	@Override
	public Optional<Post> findById(int postId) {
		return postRepository.findById(postId);
	}

	@Override
	public Collection<Post> getAll() {
		return postRepository.findAllOrderById();
	}

	@Override
	public Collection<Post> getAllByBrand(String brand) {
		return postRepository.findAllPostsByBrand(brand);
	}

	@Override
	public boolean update(Post post, PriceHistory newPrice, Collection<FileDto> dtoFiles) {
		var newPriceAfter = newPrice.getAfter();
		if (newPriceAfter != 0) {
			var historyPrices = post.getPriceHistory();
			historyPrices.get(historyPrices.size() - 1).setAfter(newPriceAfter);
			var nextPrice = new PriceHistory(0, newPriceAfter, 0, LocalDateTime.now());
			historyPrices.add(nextPrice);
		}
		if (!dtoFiles.isEmpty()) {
			fileService.deleteByPostId(post.getId());
			var photos = post.getPhotos();
			System.out.println("Photos: " + photos);
			dtoFiles.forEach(dtoFile -> photos.add(fileService.save(dtoFile)));
		} else {
			post.setPhotos(new ArrayList<>(fileService.getFilesByPostId(post.getId())));
		}
		carService.update(post.getCar());
		return postRepository.update(post);
	}

	@Override
	public void buyCar(Post post, User user) {
		Car car = post.getCar();
		carService.buyCar(car, user);
		post.setStatus(true);
		post.setUser(user);
		postRepository.update(post);
	}

	@Override
	public boolean switchStatusByUser(int id, User user, boolean status) {
		return postRepository.switchStatusByUser(id, user.getId(), status);
	}

	@Override
	public boolean deleteByIdAndUser(int postId, User user) {
		fileService.deleteByPostId(postId);
		priceHistoryService.deleteByPostId(postId);
		var carId = postRepository.deleteByIdAndUser(postId, user.getId());
		return carService.deleteByCarIdAndUserId(carId, user.getId());
	}

}
