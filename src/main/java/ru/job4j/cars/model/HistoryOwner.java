package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "history_owner")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HistoryOwner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private int id;

	@Column(name = "start_at")
	private LocalDateTime startAt = LocalDateTime.now();

	@Column(name = "end_at")
	private LocalDateTime endAt = LocalDateTime.now();

	@ManyToOne
	@JoinColumn(name = "car_id")
	private Car car;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private Owner owner;
}