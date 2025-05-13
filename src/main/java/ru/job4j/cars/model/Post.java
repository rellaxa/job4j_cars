package ru.job4j.cars.model;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "auto_post")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Include
	private int id;

	private String description;

	private boolean status;

	@Column(name = "created")
	private LocalDateTime created;

	@ManyToOne
	@JoinColumn(name = "auto_user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "car_id", foreignKey = @ForeignKey(name = "CAR_ID_FK"))
	private Car car;

	@ManyToOne
	@JoinColumn(name = "city_id")
	private City city;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "post_id")
	@ToString.Exclude
	private List<File> photos = new ArrayList<>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "post_id")
	private List<PriceHistory> priceHistory = new ArrayList<>();

	@ManyToMany
	@ToString.Exclude
	@JoinTable(
			name = "participates",
			joinColumns = { @JoinColumn(name = "post_id") },
			inverseJoinColumns = { @JoinColumn(name = "user_id") }
	)
	private Set<User> participates = new HashSet<>();

}