package ru.job4j.cars.repository;

import ru.job4j.cars.model.File;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FileRepository {

	File save(File file);

	Optional<File> findById(int fileId);

	Collection<File> getFilesByPostId(int postId);

	Collection<File> findAll();

	List<String> getPathsByPostId(int postId);

	boolean deleteById(int fileId);

	boolean deleteAll();

	List<String> deleteByPostId(int postId);
}
