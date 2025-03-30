package com.example.login_auth.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.login_auth.controller.dto.request.loginRequestDTO;
import com.example.login_auth.controller.dto.request.registerRequestDTO;
import com.example.login_auth.entities.User;
import com.example.login_auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public ResponseEntity<String> registerUser(registerRequestDTO request) {
        User user = new User();
        user.setUsername(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());
        userRepository.save(user);
        return ResponseEntity.ok("Registration successful");
    }

    public ResponseEntity<String> loginUser(loginRequestDTO request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        if (!user.getPassword().equals(request.password())) {
            throw new IllegalArgumentException("Invalid password");
        }
        return ResponseEntity.ok("Login successful");
    }
}
