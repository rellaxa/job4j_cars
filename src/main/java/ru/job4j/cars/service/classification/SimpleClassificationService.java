package ru.job4j.cars.service.classification;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Classification;
import ru.job4j.cars.repository.ClassificationRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleClassificationService implements ClassificationService {

	private final ClassificationRepository classificationRepository;

	@Override
	public Optional<Classification> findById(int id) {
		return classificationRepository.findById(id);
	}

	@Override
	public Collection<Classification> findAllOrderById() {
		return classificationRepository.findAllOrderById();
	}
}
