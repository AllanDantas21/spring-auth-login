package com.example.login_auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.login_auth.controller.authController;
import com.example.login_auth.configurations.JwtAuthenticationFilter;
import com.example.login_auth.service.TokenService;
import org.springframework.security.core.userdetails.UserDetailsService;

@WebMvcTest(authController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;
    
    @MockBean
    private UserDetailsService userDetailsService;
    
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Test
    @WithMockUser
    public void testRegisterEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
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