package org.example.repository;

import org.example.entities.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.example.utils.HibernateSessionFactoryUtil.getSessionFactory;

public class ProductRepoImpl implements ProductRepo {
    private SessionFactory factory;

    {
        factory = getSessionFactory();
    }

    Session session;

    public ProductRepoImpl(SessionFactory factory) {
        this.factory = factory;
    }


    @Override
    public List<Product> findAll() {
        return executeForSession(session -> session
                .createQuery("FROM Product p", Product.class)
                .getResultList()
        );
    }


    @Override
    public Product findByName(String name) {
        return executeForSession(session -> session
                .createQuery("FROM Product p WHERE  p.name =: name", Product.class)
                .setParameter("name", name).getSingleResult()
        );
    }

    @Override
    public Product findById(Long id) {
        return executeForSession(session -> session
                .get(Product.class, id)
        );
    }

    @Override
    public void update(Product product) {
        executeInTransaction(session ->
                session.merge(product)
        );
    }

    @Override
    public void save(Product product) {
        executeInTransaction(session -> {
            if (product.getId() == null) {
                session.persist(product);
            } else {
                session.merge(product);
            }
        });
    }

    @Override
    public void delete(Long id) {
        Product product = this.findById(id);
        executeInTransaction(session ->
                session.remove(product)
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
