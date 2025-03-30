package com.example.login_auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.login_auth.controller.dto.request.loginRequestDTO;
import com.example.login_auth.controller.dto.request.registerRequestDTO;
import com.example.login_auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class authController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody registerRequestDTO request) {
        return authService.registerUser(request);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody loginRequestDTO request) {
        return authService.loginUser(request);
    }
    
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
