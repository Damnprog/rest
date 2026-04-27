package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    User save(User user, Set<Long> roleIds);

    User update(User user, Set<Long> roleIds);

    Optional<User> findById(Long id);

    List<User> findAll();

    void deleteById(Long id);

    User getUserByUsername(String username);


}