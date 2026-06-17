package com.awbd.ironplate.service;

import com.awbd.ironplate.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);

    Optional<User> findByUsername(String username);

    User findById(Long id);

    List<User> findAll();

    void deleteById(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
