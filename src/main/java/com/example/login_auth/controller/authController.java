package com.example.login_auth.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.login_auth.controller.dto.request.loginRequestDTO;
import com.example.login_auth.controller.dto.request.registerRequestDTO;
import com.example.login_auth.controller.dto.response.AuthResponseDTO;
import com.example.login_auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class authController {

    private final AuthService authService;
    
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponseDTO> register(@RequestBody registerRequestDTO request) {
        return authService.registerUser(request);
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponseDTO> login(@RequestBody loginRequestDTO request) {
        return authService.loginUser(request);
    }
    
    @GetMapping(value = "/health", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> health() {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body("OK");
    }
}
