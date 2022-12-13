package org.example.repository;

import org.example.entities.Product;

import java.util.List;

public interface ProductRepo {
    Product findById(Long id);

    void save(Product product);

    void update(Product product);

    void delete(Long id);

    Product findByName(String name);


    List<Product> findAll();
}
