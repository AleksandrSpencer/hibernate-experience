package org.example.repository;

import org.example.entities.Order;
import org.example.entities.Product;
import org.example.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

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
        executeInTransaction(session -> {
                    User user = session
                    .createQuery("FROM User  u WHERE  u.name =: name", User.class)
                    .setParameter("name", userName).getSingleResult();

            List<Order> orders = user.getOrders();

            orders.forEach(o ->
                    System.out.println("Product for " + user.getName() + ": "
                            + o.getProduct().toString()));

                }
        );
    }

    public void findUserByProductTitle(String productTitle) {
        executeInTransaction(session1 -> {
                    Product product = session
                            .createQuery("FROM Product p WHERE  p.name =: name", Product.class)
                            .setParameter("name", productTitle).getSingleResult();
                    List<Order> orders = product.getOrders();

                    orders.forEach(o ->
                            System.out.println("User for " + product.getName() + ": "
                                    + o.getUser().toString()));
                }
        );
    }
    @Override
    public List<Order> findAll() {
        return executeForSession( session -> session
                                    .createQuery("FROM Order o", Order.class)
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
    private <R> R executeForSession(Function<Session, R> function){
        session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            return function.apply(session);
        } catch (Exception ex) {
            session.getTransaction().rollback();
            return null;
        } finally {
            if(session != null){
                session.close();
            }
        }
    }
}
