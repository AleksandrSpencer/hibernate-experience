package org.example.repository;

import org.example.entities.User;

import java.util.List;

public interface UserRepo {
    User findById(Long id);

    void save(User user);

    void update(User user);

    void delete(Long id);

    User findByName(String name);

    List<User> findAll();
}
