package ru.job4j.cars.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class FileDto {

	private String name;

	private byte[] content;
}
