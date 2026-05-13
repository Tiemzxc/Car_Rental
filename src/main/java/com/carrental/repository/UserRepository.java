package com.carrental.repository;

import com.carrental.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Long saveUser(User user);
    Optional<User> findByEmail(String email);
    List<User> findAll();
}