package com.example.login_auth.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.login_auth.entities.User;

@Service
public class TokenService {
    private String secret = "evaitomando";
    private String refreshSecret = "refreshsecretkey"; // Different secret for refresh tokens

    public String generateToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("login-auth")
                    .withSubject(user.getEmail())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("login-auth")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            return "";
        }
    }

    public Boolean isTokenValid(String token, String email){
        String tokenSubject = validateToken(token);
        if (tokenSubject == null || tokenSubject.isEmpty()){
            return false;
        }
        return true;
    }

    public String generateRefreshToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(refreshSecret);
            String token = JWT.create()
                    .withIssuer("login-auth")
                    .withSubject(user.getEmail())
                    .withExpiresAt(genExpirationDate(24 * 1))
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating refresh token", exception);
        }
    }

    public String validateRefreshToken(String refreshToken){
        try {
            Algorithm algorithm = Algorithm.HMAC256(refreshSecret);
            return JWT.require(algorithm)
                    .withIssuer("login-auth")
                    .build()
                    .verify(refreshToken)
                    .getSubject();
        } catch (JWTVerificationException exception){
            return "";
        }
    }

    public String refreshAccessToken(String refreshToken) {
        String email = validateRefreshToken(refreshToken);
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Invalid refresh token");
        }
        
        User user = new User();
        user.setEmail(email);
        return generateToken(user);
    }

    public Boolean isRefreshTokenValid(String refreshToken, String email){
        String tokenSubject = validateRefreshToken(refreshToken);
        if (tokenSubject == null || tokenSubject.isEmpty()){
            return false;
        }
        return email.equals(tokenSubject);
    }

    public Long getExpirationTime() {
        return genExpirationDate().toEpochMilli();
    }

    private Instant genExpirationDate(){
        return genExpirationDate(1);
    }
    
    private Instant genExpirationDate(int hours){
        return LocalDateTime.now().plusHours(hours).toInstant(ZoneOffset.of("-03:00"));
    }

}