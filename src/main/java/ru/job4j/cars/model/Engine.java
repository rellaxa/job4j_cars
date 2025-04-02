package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "engines")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Engine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private int id;

	@Column(name = "engine_name")
	private String name;
}
