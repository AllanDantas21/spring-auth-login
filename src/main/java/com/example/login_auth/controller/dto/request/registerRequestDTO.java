package com.example.login_auth.controller.dto.request;

import lombok.Builder;

@Builder
public record registerRequestDTO(
        String name,
        String email,
        String password
){}
