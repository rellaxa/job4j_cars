package ru.job4j.cars;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ParticipatesRun {

	public static void main(String[] args) {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure().build();
		try {
			SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();

			var ownerPostUser = new User();
			ownerPostUser.setLogin("Owner");
			ownerPostUser.setPassword("password");
			create(ownerPostUser, sf);

			var firstUser = new User();
			firstUser.setLogin("First");
			firstUser.setPassword("password");
			create(firstUser, sf);

			var secondUser = new User();
			secondUser.setLogin("Second");
			secondUser.setPassword("password");
			create(secondUser, sf);

			var subscribers = List.of(firstUser, secondUser);

			var post = new Post();
			post.setUser(ownerPostUser);
			post.setDescription("Selling Tesla model X");
			post.setPriceHistory(List.of(
					new PriceHistory(0, 1000, 2000, LocalDateTime.now()),
					new PriceHistory(0, 2000, 3000, LocalDateTime.now())
			));
			post.setSubscribers(subscribers);
			create(post, sf);
			var foundPost = sf.openSession()
					.createQuery("from Post where id = :fId", Post.class)
					.setParameter("fId", post.getId())
					.getSingleResult();
			System.out.println(foundPost);
			delete(post.getId(), sf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			StandardServiceRegistryBuilder.destroy(registry);
		}
	}

	public static <T> T create(T model, SessionFactory sf) {
		Session session = sf.openSession();
		session.beginTransaction();
		session.persist(model);
		session.getTransaction().commit();
		session.close();
		return model;
	}

	public static void delete(Integer id, SessionFactory sf) {
		Session session = sf.openSession();
		session.beginTransaction();
		Post post = new Post();
		post.setId(id);
		session.delete(post);
		session.getTransaction().commit();
		session.close();
	}
}
