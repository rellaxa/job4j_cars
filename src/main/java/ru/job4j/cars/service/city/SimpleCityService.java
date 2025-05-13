package ru.job4j.cars.service.city;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.City;
import ru.job4j.cars.repository.CityRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleCityService implements CityService {

	private final CityRepository cityRepository;

	@Override
	public Optional<City> findById(int cityId) {
		return cityRepository.findById(cityId);
	}

	@Override
	public Collection<City> findAllOrderById() {
		return cityRepository.getAllCities();
	}
}
