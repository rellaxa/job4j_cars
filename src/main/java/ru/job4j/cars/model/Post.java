package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "auto_post")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Include
	private int id;

	private String description;

	private LocalDateTime created;

	@ManyToOne
	@JoinColumn(name = "auto_user_id")
	private User user;

	@OneToMany
	@JoinColumn(name = "post_id")
	private List<PriceHistory> priceHistory = new ArrayList<>();
}
