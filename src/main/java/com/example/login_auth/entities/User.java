package com.example.login_auth.entities;

import jakarta.persistence.*;;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String password;
    @Column(unique = true)
    private String email;
}
