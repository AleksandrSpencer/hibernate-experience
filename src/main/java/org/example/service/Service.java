package org.example.service;

import org.example.entities.Order;
import org.example.entities.Product;
import org.example.entities.User;
import org.example.repository.*;

import org.hibernate.SessionFactory;

import java.util.List;


import static org.example.utils.HibernateSessionFactoryUtil.getSessionFactory;

public class Service {
    SessionFactory factory;

    {
        factory = getSessionFactory();

    }

    private final UserRepo usersRepo = new UserRepoImpl(factory);
    private final ProductRepo productRepo = new ProductRepoImpl(factory);
    private final OrderRepo orderRepo = new OrderRepoImpl(factory);


    public Service() {
    }

    public void buy(User user, Product product) {
        orderRepo.save(user, product);
    }

    public void saveUser(User user) {
        usersRepo.save(user);
    }

    public void saveProduct(Product product) {
        productRepo.save(product);
    }

    public void showProductsByUser(String userName) {
        orderRepo.showProductsByUser(userName);
    }

    public void findUserByProductTitle(String productTitle) {
        orderRepo.findUserByProductTitle(productTitle);
    }

    public void removeUser(String userName) {
        User user = usersRepo.findByName(userName);
        usersRepo.delete(user.getId());
        System.out.println("Removed " + user);

    }

    public void deleteProductById(Long id) {
        Product product = productRepo.findById(id);
        productRepo.delete(id);
        System.out.println("Removed " + product);
    }

    public List<User> findAllUsers() {
        return usersRepo.findAll();

    }


    public User findUserById(Long id) {
        return usersRepo.findById(id);
    }

    public Product findProductById(Long id) {
        return productRepo.findById(id);
    }

    public Product findProductByName(String name) {
        return productRepo.findByName(name);
    }

    public User findUserByName(String name) {
        return usersRepo.findByName(name);
    }


    public List<Product> findAllProducts() {
        return productRepo.findAll();
    }

    public List<Order> findAllOrders() {
        return orderRepo.findAll();
    }
}