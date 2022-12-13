package org.example.repository;

import org.example.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.function.Consumer;

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

        try {
            session = factory.getCurrentSession();
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.getTransaction().commit();
            return user;
        } finally {
            session.close();
        }
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
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();
            User user = session
                    .createQuery("FROM User  u WHERE  u.name =: name", User.class)
                    .setParameter("name", name).getSingleResult();
            session.getTransaction().commit();
            return user;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    @Override
    public List<User> findAll() {

        try {
            session = factory.getCurrentSession();
            session.beginTransaction();
            List<User> users = session
                    .createQuery("SELECT u FROM User u", User.class)
                    .getResultList();
            session.getTransaction().commit();
            return users;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
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
}

