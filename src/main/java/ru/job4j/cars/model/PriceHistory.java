package ru.job4j.cars.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PriceHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Include
	private int id;

	private long before;

	private long after;

	private LocalDateTime created = LocalDateTime.now();
}
