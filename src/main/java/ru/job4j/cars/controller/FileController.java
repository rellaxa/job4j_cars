package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.cars.service.file.FileService;

@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class FileController {

	private final FileService fileService;

	@GetMapping("/one/{postId}")
	public ResponseEntity<?> getFirstByPostId(@PathVariable int postId) {
		var contentOptional = fileService.getFirstByPostId(postId);
		if (contentOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(contentOptional.get().getContent());
	}

	@GetMapping("/{fileId}")
	public ResponseEntity<?> getFilesById(@PathVariable int fileId) {
		var contentOptional = fileService.getFileById(fileId);
		if (contentOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(contentOptional.get().getContent());
	}
}