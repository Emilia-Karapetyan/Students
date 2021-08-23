package com.example.demo.student.repository;

import com.example.demo.student.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    User findUserByEmailIgnoreCase(String email);
    Optional<User> findByRefreshToken(String refreshToken);
}
