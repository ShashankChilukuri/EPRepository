package com.example.demo.service;

import com.example.demo.models.User;

import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    

    // Register new user
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    // Fetch all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Fetch user by username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Other methods for updating and deleting users can be added here.
}
