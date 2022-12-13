package org.example.repository;

import org.example.entities.Order;
import org.example.entities.Product;
import org.example.entities.User;

import java.util.List;


public interface OrderRepo {


    void save(User user, Product product);

    void findUserByProductTitle(String productTitle);

    void showProductsByUser(String userName);

    List<Order> findAll();
}
