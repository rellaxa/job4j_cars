package ru.job4j.cars.repository.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.City;
import ru.job4j.cars.repository.CityRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class HBNCityRepository implements CityRepository {

	private final CrudRepository crudRepository;

	@Override
	public Optional<City> findById(int cityId) {
		log.info("Finding city by id: {}", cityId);
		return crudRepository.optional("from City where id = : fId", City.class,
				Map.of("fId", cityId));
	}

	@Override
	public Collection<City> getAllCities() {
		log.info("Finding all cities");
		return crudRepository.query("from City c order by c.id asc", City.class);
	}
}
