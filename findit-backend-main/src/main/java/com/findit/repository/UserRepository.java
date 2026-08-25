package com.findit.repository;

import com.findit.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User entity.
 * Spring auto-implements this at runtime — no code needed!
 *
 * JpaRepository<User, Long> gives us:
 *   - save(), findById(), findAll(), delete(), etc.
 *
 * We add custom methods just by naming them properly:
 *   - findByEmail() → Spring writes the SQL: SELECT * FROM users WHERE email = ?
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}