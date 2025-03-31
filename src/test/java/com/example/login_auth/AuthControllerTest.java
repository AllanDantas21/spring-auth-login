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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.login_auth.configurations.JwtAuthenticationFilter;
import com.example.login_auth.controller.authController;
import com.example.login_auth.controller.dto.request.loginRequestDTO;
import com.example.login_auth.controller.dto.request.registerRequestDTO;
import com.example.login_auth.controller.dto.response.AuthResponseDTO;
import com.example.login_auth.repository.UserRepository;
import com.example.login_auth.service.AuthService;
import com.example.login_auth.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(authController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
        // Create a response DTO
        AuthResponseDTO responseDTO = new AuthResponseDTO(
            "test-token",
            "test-refresh-token",
            3600000L
        );
        
        // Mock the service response with proper type and content type
        when(authService.registerUser(any(registerRequestDTO.class)))
            .thenReturn(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(responseDTO));
        
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"test\",\"password\":\"password\",\"email\":\"test@example.com\"}")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testLoginEndpoint() throws Exception {
        // Create a response DTO
        AuthResponseDTO responseDTO = new AuthResponseDTO(
            "login-token",
            "login-refresh-token",
            3600000L
        );
        
        // Mock the service response with proper content type
        when(authService.loginUser(any(loginRequestDTO.class)))
            .thenReturn(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(responseDTO));
        
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\",\"password\":\"password\"}")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testHealthEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/health")
                .accept(MediaType.TEXT_PLAIN))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
}