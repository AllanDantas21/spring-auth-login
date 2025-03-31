package com.example.login_auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.login_auth.configurations.JwtAuthenticationFilter;
import com.example.login_auth.entities.User;
import com.example.login_auth.repository.UserRepository;
import com.example.login_auth.service.TokenService;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class JwtAuthenticationFilterTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;
    private User testUser;
    private String validToken = "valid.jwt.token";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
        
        SecurityContextHolder.clearContext();
        
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setEmail("test@example.com");
        testUser.setRoles(Collections.singletonList("USER"));
        
        // The filter uses email, not username for validation
        when(tokenService.validateToken(validToken)).thenReturn("testuser");
        // Change findByUsername to findByEmail
        when(userRepository.findByEmail("testuser")).thenReturn(Optional.of(testUser));
        when(tokenService.isTokenValid(eq(validToken), eq("testuser"))).thenReturn(true);
    }

    @Test
    public void testNoAuthorizationHeader() throws Exception {
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        assertNull(SecurityContextHolder.getContext().getAuthentication(), 
                "Security context should not contain authentication");
        
        verify(tokenService, never()).validateToken(anyString());
    }

    @Test
    public void testNonBearerToken() throws Exception {
        request.addHeader("Authorization", "Basic dXNlcjpwYXNzd29yZA==");
        
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        assertNull(SecurityContextHolder.getContext().getAuthentication(), 
                "Security context should not contain authentication");
        
        verify(tokenService, never()).validateToken(anyString());
    }

    @Test
    public void testValidToken() throws Exception {
        request.addHeader("Authorization", "Bearer " + validToken);
        
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        assertNotNull(SecurityContextHolder.getContext().getAuthentication(), 
                "Security context should contain authentication");
        
        assertEquals("testuser", 
                ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(),
                "Authenticated user should be testuser");
        
        verify(tokenService).validateToken(validToken);
        // Change to verify findByEmail instead of findByUsername
        verify(userRepository).findByEmail("testuser");
        verify(tokenService).isTokenValid(eq(validToken), eq("testuser"));
    }

    @Test
    public void testInvalidToken() throws Exception {
        String invalidToken = "invalid.token";
        // For invalid tokens, validateToken typically returns null or throws an exception
        when(tokenService.validateToken(invalidToken)).thenReturn(null);
        
        request.addHeader("Authorization", "Bearer " + invalidToken);
        
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        assertNull(SecurityContextHolder.getContext().getAuthentication(), 
                "Security context should not contain authentication with invalid token");
        
        verify(tokenService).validateToken(invalidToken);
        // No need to change this line as it's never called
        verify(userRepository, never()).findByUsername(anyString());
        verify(tokenService, never()).isTokenValid(anyString(), anyString());
    }
    
    @Test
    public void testExpiredToken() throws Exception {
        String expiredToken = "expired.jwt.token";
        // Token validation extracts the username
        when(tokenService.validateToken(expiredToken)).thenReturn("testuser");
        // But token is expired/invalid when checking with the user
        when(tokenService.isTokenValid(eq(expiredToken), eq("testuser"))).thenReturn(false);
        
        request.addHeader("Authorization", "Bearer " + expiredToken);
        
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        assertNull(SecurityContextHolder.getContext().getAuthentication(), 
                "Security context should not contain authentication with expired token");
        
        verify(tokenService).validateToken(expiredToken);
        // Change to verify findByEmail instead of findByUsername
        verify(userRepository).findByEmail("testuser");
        verify(tokenService).isTokenValid(eq(expiredToken), eq("testuser"));
    }
}