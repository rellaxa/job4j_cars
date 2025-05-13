package ru.job4j.cars.service.car.brand;

import ru.job4j.cars.model.CarBrand;
import ru.job4j.cars.service.ModelService;

public interface CarBrandService extends ModelService<CarBrand> {

	CarBrand save(CarBrand carBrand);
}
