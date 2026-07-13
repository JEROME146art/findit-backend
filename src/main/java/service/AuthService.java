package com.findit.service;

import com.findit.model.dto.AuthResponse;
import com.findit.model.dto.LoginRequest;
import com.findit.model.dto.RegisterRequest;
import com.findit.model.entity.Student;
import com.findit.model.entity.User;
import com.findit.repository.UserRepository;
import com.findit.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Handles user registration and login.
 */
@Service
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepo,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Register a new student.
     */
    public AuthResponse register(RegisterRequest req) {
        // Check if email already used
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already registered: " + req.getEmail());
        }

        // Create student (encrypt password!)
        Student student = new Student(
                req.getEmail(),
                passwordEncoder.encode(req.getPassword()),  // 🔒 Encrypt!
                req.getFullName(),
                req.getPhone(),
                req.getRollNumber(),
                req.getDepartment()
        );

        User saved = userRepo.save(student);

        // Generate JWT token
        String token = jwtUtil.generateToken(saved.getEmail(), saved.getRole(), saved.getId());

        return new AuthResponse(token, saved.getId(), saved.getEmail(),
                saved.getFullName(), saved.getRole());
    }

    /**
     * Log in an existing user.
     */
    public AuthResponse login(LoginRequest req) {
        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Compare provided password with stored encrypted password
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId());

        return new AuthResponse(token, user.getId(), user.getEmail(),
                user.getFullName(), user.getRole());
    }
}
