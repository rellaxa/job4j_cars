package ru.job4j.cars.service.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.model.File;
import ru.job4j.cars.repository.FileRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class SimpleFileService implements FileService {

	private final FileRepository fileRepository;

	private final String storageDirectory;

	public SimpleFileService(FileRepository fileRepository,
							 @Value("${file.directory}") String storageDirectory) {
		this.fileRepository = fileRepository;
		this.storageDirectory = storageDirectory;
		createStorageDirectory(storageDirectory);
	}

	private void createStorageDirectory(String path) {
		try {
			Files.createDirectories(Path.of(path));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public File save(FileDto fileDto) {
		var path = getNewFilePath(fileDto.getName());
		writeFileBytes(path, fileDto.getContent());
		return new File(0, fileDto.getName(), path);
	}

	@Override
	public Optional<FileDto> getFileById(int fileId) {
		var optionalFile = fileRepository.findById(fileId);
		if (optionalFile.isEmpty()) {
			return Optional.empty();
		}
		var file = optionalFile.get();
		var content = readFileAsBytes(file.getPath());
		return Optional.of(new FileDto(file.getName(), content));
	}

	@Override
	public Collection<File> getFilesByPostId(int postId) {
		return fileRepository.getFilesByPostId(postId);
	}

	@Override
	public Optional<FileDto> getFirstByPostId(int postId) {
		var files = fileRepository.getFilesByPostId(postId);
		if (files.isEmpty()) {
			return Optional.empty();
		}
		var file = files.iterator().next();
		var content = readFileAsBytes(file.getPath());
		return Optional.of(new FileDto(file.getName(), content));
	}

	private String getNewFilePath(String path) {
		return storageDirectory + java.io.File.separator + UUID.randomUUID() + path;
	}

	@Override
	public void deleteByPostId(int postId) {
		var deletedFilePaths = fileRepository.deleteByPostId(postId);
		deletedFilePaths.forEach(filePath -> deleteFileByPath(Path.of(filePath)));
	}

	@Override
	public void deleteSelectedFiles(int postId) {
		var selectedPaths = fileRepository.getPathsByPostId(postId);
		selectedPaths.forEach(filePath -> deleteFileByPath(Path.of(filePath)));
	}

	private void writeFileBytes(String path, byte[] content) {
		try {
			Files.write(Path.of(path), content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private byte[] readFileAsBytes(String path) {
		try {
			return Files.readAllBytes(Path.of(path));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteFileByPath(Path path) {
		try {
			Files.delete(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
