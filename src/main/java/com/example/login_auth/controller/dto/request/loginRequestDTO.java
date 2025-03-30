package com.example.login_auth.controller.dto.request;

import lombok.Builder;

@Builder
public record loginRequestDTO(String email, String password) {
}
