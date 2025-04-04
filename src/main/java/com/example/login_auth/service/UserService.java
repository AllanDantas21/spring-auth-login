package com.example.login_auth.service;

import java.util.List;

import com.example.login_auth.entities.User;
import com.example.login_auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    public String registerUser(String name, String email, String password, List<String> role) {
        User user = new User(1L, name, email, password, role);
        userRepository.save(user);
        return "User registered successfully";
    }
}
