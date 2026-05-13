package com.carrental.service.impl;

import com.carrental.model.User;
import com.carrental.repository.UserRepository;
import com.carrental.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(User user) {
        if (user.getRole() == null) {
            user.setRole("CUSTOMER");
        }
        Long id = userRepository.saveUser(user);
        user.setId(id);
        return user;
    }

    @Override
    public Optional<User> login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}