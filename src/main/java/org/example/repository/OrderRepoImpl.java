package org.example.repository;

import org.example.entities.Order;
import org.example.entities.Product;
import org.example.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.function.Consumer;

import static org.example.utils.HibernateSessionFactoryUtil.getSessionFactory;

public class OrderRepoImpl implements OrderRepo {
    private SessionFactory factory;

    {
        factory = getSessionFactory();

    }

    Session session;


    public OrderRepoImpl(SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void save(User user, Product product) {
        executeInTransaction(session -> {

            Order order = new Order();
            Order.Id id = new Order.Id();
            id.setUserId(user.getId());
            id.setProductId(product.getId());

            order.setId(id);
            order.setPrice(product.getPrice());

            session.persist(order);
            System.out.println("Saved an order: " + order);

        });
    }

    public void showProductsByUser(String userName) {
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();
            User user = session
                    .createQuery("FROM User  u WHERE  u.name =: name", User.class)
                    .setParameter("name", userName).getSingleResult();

            List<Order> orders = user.getOrders();

            orders.forEach(o ->
                    System.out.println("Product for " + user.getName() + ": "
                            + o.getProduct().toString()));
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();

            }
        }
    }

    public void findUserByProductTitle(String productTitle) {

        try {
            session = factory.getCurrentSession();
            session.beginTransaction();

            Product product = session
                    .createQuery("FROM Product p WHERE  p.name =: name", Product.class)
                    .setParameter("name", productTitle).getSingleResult();
            List<Order> orders = product.getOrders();

            orders.forEach(o ->
                    System.out.println("User for " + product.getName() + ": "
                            + o.getUser().toString()));
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();

            }
        }
    }


    @Override
    public List<Order> findAll() {
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();
            List<Order> orders = session
                    .createQuery("SELECT o FROM Order o", Order.class)
                    .getResultList();
            session.getTransaction().commit();
            return orders;
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
