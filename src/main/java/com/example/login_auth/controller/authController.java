package com.example.login_auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class authController {

    @PostMapping("/register")
    public String register() {
        return "User registered successfully!";
    }
    
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
