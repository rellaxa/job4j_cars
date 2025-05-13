package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.model.*;
import ru.job4j.cars.service.car.CarService;
import ru.job4j.cars.service.city.CityService;
import ru.job4j.cars.service.file.FileService;
import ru.job4j.cars.service.owner.history.HistoryOwnerService;
import ru.job4j.cars.service.post.PostService;
import ru.job4j.cars.service.car.body.CarBodyService;
import ru.job4j.cars.service.car.brand.CarBrandService;
import ru.job4j.cars.service.classification.ClassificationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping({"/", "posts"})
@AllArgsConstructor
public class PostController {

	private final PostService postService;

	private final CarService carService;

	private final FileService fileService;

	private final ClassificationService classificationService;

	private final CarBrandService carBrandService;

	private final CarBodyService carBodyService;

	private final CityService cityService;

	private final HistoryOwnerService historyOwnerService;

	@GetMapping({"", "/brands/{b}"})
	public String getAllPosts(@PathVariable(name = "b", required = false) String brandName,
							  @RequestParam(name = "view", required = false, defaultValue = "cards") String view,
							  Model model) {
		var posts = brandName != null ? postService.getAllByBrand(brandName) : postService.getAll();
		model.addAttribute("currentBrand", brandName);
		model.addAttribute("posts", posts);
		model.addAttribute("brands", carBrandService.findAllOrderById());
		model.addAttribute("currentView", view);
		return "/posts/allPosts";
	}

	@GetMapping("/create")
	public String getCreationPage(Model model) {
		model.addAttribute("brands", carBrandService.findAllOrderById());
		model.addAttribute("classes", classificationService.findAllOrderById());
		model.addAttribute("bodies", carBodyService.findAllOrderById());
		model.addAttribute("cities", cityService.findAllOrderById());
		return "/posts/create";
	}

	@PostMapping("/create")
	public String create(@ModelAttribute Post post, @RequestParam List<MultipartFile> files,
						 @SessionAttribute User user) {
		Car car = post.getCar();
		carService.save(car, user);
		post.setUser(user);
		postService.save(post, getFilesDto(files));
		return "redirect:/posts";
	}

	@GetMapping("/{id}")
	public String getPostById(@PathVariable int id, Model model) {
		var optionalPost = postService.findById(id);
		if (optionalPost.isEmpty()) {
			model.addAttribute("error", "Post with id " + id + " not found");
			return "/errors/404";
		}
		var post = optionalPost.get();
		var filesById = fileService.getFilesByPostId(post.getId());
		model.addAttribute("files", filesById);
		model.addAttribute("post", post);
		model.addAttribute("historyOwners",
				historyOwnerService.getHistoryOwnersByCarId(post.getCar().getId()));
		return "/posts/one";
	}

	@GetMapping("/update/{id}")
	public String getUpdatePostPage(@PathVariable int id, @SessionAttribute User user, Model model) {
		var optionalPost = postService.findById(id);
		if (optionalPost.isEmpty()) {
			model.addAttribute("error", "Post with id " + id + " not found");
			return "/errors/404";
		}
		var post = optionalPost.get();
		if (!post.getUser().equals(user)) {
			model.addAttribute("error", "You are not the owner of this car");
			return "/errors/404";
		}
		var prices = post.getPriceHistory();
		var lastPrice = prices.get(prices.size() - 1);
		model.addAttribute("post", post);
		model.addAttribute("allPrices", prices);
		model.addAttribute("lastPrice", lastPrice);
		model.addAttribute("classes", classificationService.findAllOrderById());
		model.addAttribute("brands", carBrandService.findAllOrderById());
		model.addAttribute("bodies", carBodyService.findAllOrderById());
		model.addAttribute("cities", cityService.findAllOrderById());

		return "/posts/update";
	}

	@PostMapping("/update")
	public String updatePost(@ModelAttribute Post post,
							 @ModelAttribute PriceHistory lastPrice,
							 @RequestParam(required = false) List<MultipartFile> files) {
		log.info("Last price: {}", lastPrice);
		log.info("Post: {}", post);
		log.info("Car: {}", post.getCar());
		log.info("@RequestParam List<MultipartFile> files size: {}, last file name: {}", files.size(),
				files.get(files.size() - 1).toString());
		log.info("<--- 'Updating post' --->");
		postService.update(post, lastPrice, getFilesDto(files));
		return "redirect:/posts";
	}

	private List<FileDto> getFilesDto(List<MultipartFile> files) {
		List<FileDto> dtos = new ArrayList<>();
		try {
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					dtos.add(new FileDto(file.getOriginalFilename(), file.getBytes()));
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to process files", e);
		}
		return dtos;
	}

	@GetMapping("/switchStatus/{id}/{status}")
	public String switchStatus(@PathVariable int id, @PathVariable boolean status,
							   @SessionAttribute User user, Model model) {
		var isSuccess = postService.switchStatusByUser(id, user, status);
		if (!isSuccess) {
			model.addAttribute("error", "Post with id " + id
					+ " not found or you have no permission to edit this post.");
			return "/errors/404";
		}
		return "redirect:/posts";
	}

	@GetMapping("/buy/{id}")
	public String buyCar(@PathVariable int id, @SessionAttribute User user, Model model) {
		var optionalPost = postService.findById(id);
		if (optionalPost.isEmpty()) {
			model.addAttribute("error", "Post with id " + id + " not found");
			return "/errors/404";
		}
		var post = optionalPost.get();
		if (post.getUser().equals(user)) {
			model.addAttribute("error",
					"You are the owner of this car. You cannot buy your own car.");
			return "/errors/404";
		}
		if (post.isStatus()) {
			model.addAttribute("error", "This car is already bought.");
			return "/errors/404";
		}
		postService.buyCar(post, user);
		return "redirect:/posts";
	}

	@GetMapping("delete/{id}")
	public String deletePost(@PathVariable int id, @SessionAttribute User user, Model model) {
		var isDeleted = postService.deleteByIdAndUser(id, user);
		if (!isDeleted) {
			model.addAttribute("error", "Post with id " + id
					+ " not found or you have no permission to delete this post.");
			return "/errors/404";
		}
		return "redirect:/posts";
	}

}