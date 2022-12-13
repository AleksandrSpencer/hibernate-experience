package org.example.repository;

import org.example.entities.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.function.Consumer;

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
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();
            List<Product> products = session
                    .createQuery("SELECT p FROM Product p", Product.class)
                    .getResultList();
            session.getTransaction().commit();
            return products;
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
    public Product findByName(String name) {
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();
            Product product = session
                    .createQuery("FROM Product p WHERE  p.name =: name", Product.class)
                    .setParameter("name", name).getSingleResult();
            session.getTransaction().commit();
            return product;
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
    public Product findById(Long id) {
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();
            Product product = session.get(Product.class, id);
            session.getTransaction().commit();
            return product;
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
    public void update(Product product) {
        executeInTransaction(session -> {
            session.merge(product);
        });
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
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();
            Product product = session.get(Product.class, id);
            session.delete(product);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }


    private void executeInTransaction(Consumer<Session> consumer) {
        Session session = factory.getCurrentSession();
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
