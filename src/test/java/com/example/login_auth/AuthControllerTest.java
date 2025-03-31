package com.example.login_auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.login_auth.configurations.JwtAuthenticationFilter;
import com.example.login_auth.controller.authController;
import com.example.login_auth.controller.dto.request.registerRequestDTO;
import com.example.login_auth.repository.UserRepository;
import com.example.login_auth.service.AuthService;
import com.example.login_auth.service.TokenService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(authController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;
    
    @MockBean
    private UserRepository userRepository;
    
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private AuthService authService;
    
    @Test
    @WithMockUser
    public void testRegisterEndpoint() throws Exception {
        // Mock the service response
        when(authService.registerUser(any(registerRequestDTO.class)))
            .thenReturn(ResponseEntity.ok("Registration successful"));
        
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"test\",\"password\":\"password\",\"email\":\"test@example.com\"}")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testHealthEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/health"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
}