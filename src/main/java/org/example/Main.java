package org.example;

import org.example.entities.Order;
import org.example.service.*;
import org.example.entities.Product;
import org.example.entities.User;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Service s = new Service();

        //find  user by id
        User user = s.findUserById(18L);
        System.out.println(user);

        //find all allUsers
        List<User> users = s.findAllUsers();
        users.forEach(System.out::println);

        //find all products
        List<Product> products = s.findAllProducts();
        products.forEach(System.out::println);

        //find  all orders
        List<Order> orders = s.findAllOrders();
        orders.forEach(System.out::println);

        //find  product by name
        String productName = "Product_7";
        Product product = s.findProductByName(productName);
        System.out.println(product);

        //Save new user
        User userNew = new User("InimitableUser-8");
        s.saveUser(userNew);
        System.out.println("Created newOne:" + userNew);

        //remove user  by name
        s.removeUser(userNew.getName());

        //find product by id
        Product productRequired = s.findProductById(25L);
        System.out.println(productRequired);

        //create and save new product
        Product product1 = new Product("SuperProduct-22", new BigDecimal("73.00"));
        Product product2 = new Product("SuperProduct-33", new BigDecimal("37.00"));
        s.saveProduct(product1);
        s.saveProduct(product2);

        //create new order
        s.buy(user, product2);

        //find user by product's name via order
        String productTitle = "Product_8";
        System.out.println(productTitle);
        s.findUserByProductTitle(productTitle);

        //find product  by user's name via order
        String useName = "Person_8";
        s.showProductsByUser(useName);


    }
}

