package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.job4j.cars.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {

    private final SessionFactory sf;

    /**
     * Save user to DB.
     *
     * @param user user.
     * @return user with id.
     */
    public User create(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return user;
    }

    /**
     * Update user in DB.
     *
     * @param user user.
     */
    public void update(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("UPDATE User u SET u.login = :login, u.password = :password WHERE u.id = :id")
                    .setParameter("login", user.getLogin())
                    .setParameter("password", user.getPassword())
                    .setParameter("id", user.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Delete user by id.
     *
     * @param userId ID
     */
    public void delete(int userId) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE User WHERE id = :fId")
                    .setParameter("fId", userId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Sorted user list by id.
     *
     * @return user list.
     */
    public List<User> findAllOrderById() {
        Session session = sf.openSession();
        List<User> users = new ArrayList<>();
        try {
            session.beginTransaction();
            users = session.createQuery("from User ORDER BY id", User.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return users;
    }

    /**
     * Find user by ID
     *
     * @return user.
     */
    public Optional<User> findById(int userId) {
        Session session = sf.openSession();
        Optional<User> user = Optional.empty();
        try {
            session.beginTransaction();
            user = session.createQuery("from User u WHERE u.id = :fId", User.class)
                    .setParameter("fId", userId)
                    .uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return user;
    }

    /**
     * User list by login LIKE %key%
     *
     * @param key key
     * @return user list.
     */
    public List<User> findByLikeLogin(String key) {
        Session session = sf.openSession();
        List<User> users = new ArrayList<>();
        String stringKey = String.format("%%%s%%", key);
        try {
            session.beginTransaction();
            users = session.createQuery("from User u WHERE u.login LIKE :login", User.class)
                    .setParameter("login", stringKey)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return users;
    }

    /**
     * Find user by login.
     *
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        Session session = sf.openSession();
        Optional<User> user = Optional.empty();
        try {
            session.beginTransaction();
            user = session.createQuery("from User u WHERE u.login = :login", User.class)
                    .setParameter("login", login)
                    .uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return user;
    }
}