package com.findit.service;

import com.findit.model.dto.UpdateProfileRequest;
import com.findit.model.dto.UserProfileResponse;
import com.findit.model.entity.Student;
import com.findit.model.entity.User;
import com.findit.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Get profile of a user by their unique database ID.
     */
    public UserProfileResponse getUserProfile(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return convertToProfileResponse(user);
    }

    /**
     * Update user profile details.
     */
    @Transactional
    public UserProfileResponse updateProfile(Long id, UpdateProfileRequest req) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Update common fields
        user.setFullName(req.getFullName());
        user.setPhone(req.getPhone());

        // Update student-specific fields if applicable
        if (user instanceof Student) {
            Student student = (Student) user;
            student.setRollNumber(req.getRollNumber());
            student.setDepartment(req.getDepartment());
        }

        User saved = userRepo.save(user);
        return convertToProfileResponse(saved);
    }

    /**
     * Helper to translate a User entity (or its Student subclass) into a safe DTO.
     */
    private UserProfileResponse convertToProfileResponse(User user) {
        String rollNumber = null;
        String department = null;

        if (user instanceof Student) {
            Student student = (Student) user;
            rollNumber = student.getRollNumber();
            department = student.getDepartment();
        }

        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getPhone(),
                rollNumber,
                department,
                user.getRole()
        );
    }
}
