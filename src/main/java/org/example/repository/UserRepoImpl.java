package org.example.repository;
import org.example.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.example.utils.HibernateSessionFactoryUtil.getSessionFactory;


public class UserRepoImpl implements UserRepo {
    private SessionFactory factory;


    {
        factory = getSessionFactory();

    }

    Session session;

    public UserRepoImpl(SessionFactory factory) {
        this.factory = factory;
    }


    @Override
    public User findById(Long id) {
        return executeForSession(session -> session
                .get(User.class, id)
        );
    }

    @Override
    public void save(User user) {
        executeInTransaction(session -> {
            if (user.getId() == null) {
                session.persist(user);
            } else {
                session.merge(user);
            }
        });
    }

    @Override
    public void update(User user) {
        executeInTransaction(session -> session.merge(user));
    }

    @Override
    public void delete(Long id) {
        User user = this.findById(id);
        executeInTransaction(
                session -> session.remove(user)
        );

    }

    @Override
    public User findByName(String name) {
        return executeForSession(session -> session
                .createQuery("FROM User  u WHERE  u.name =: name", User.class)
                .setParameter("name", name).getSingleResult()
        );
    }

    @Override
    public List<User> findAll() {
        return executeForSession(session -> session
                .createQuery("FROM User u", User.class)
                .getResultList()
        );
    }


    private void executeInTransaction(Consumer<Session> consumer) {
        session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            consumer.accept(session);
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private <R> R executeForSession(Function<Session, R> function) {
        session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            return function.apply(session);
        } catch (Exception ex) {
            session.getTransaction().rollback();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}

