package com.example.login_auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    @PostMapping("/register")
    public String register() {

        return "User registered successfully!";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
