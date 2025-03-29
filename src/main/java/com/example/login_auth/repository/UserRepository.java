package com.example.login_auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.login_auth.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
    User findByEmail(String email);
    User findByPassword(String password);
}
