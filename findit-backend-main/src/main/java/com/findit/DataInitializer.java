package com.findit;

import com.findit.model.entity.Student;
import com.findit.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepo.count() == 0) {
            Student student = new Student(
                    "john@college.edu",
                    passwordEncoder.encode("pass123"),  // 🔒 Encrypted!
                    "John Doe",
                    "9999999999",
                    "CS2024001",
                    "Computer Science"
            );
            userRepo.save(student);
            System.out.println("✅ Test student created — email: john@college.edu, password: pass123");
        }
    }
}