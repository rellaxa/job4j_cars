package ru.job4j.cars.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cars")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private int id;

	@Column(name = "car_name")
	private String name;

	@ManyToOne
	@JoinColumn(name = "brand_id")
	private CarBrand brand;

	@ManyToOne
	@JoinColumn(name = "class_car_id")
	private Classification classCar;

	@ManyToOne
	@JoinColumn(name = "body_id")
	private CarBody body;

	@ManyToOne
	@JoinColumn(name = "engine_id", foreignKey = @ForeignKey(name = "ENGINE_ID_FK"))
	private Engine engine;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "car_id")
	@ToString.Exclude
	private Set<HistoryOwner> ownersHistory = new HashSet<>();
}