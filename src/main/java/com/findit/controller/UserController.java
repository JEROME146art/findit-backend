package com.findit.controller;

import com.findit.model.dto.UpdateProfileRequest;
import com.findit.model.dto.UserProfileResponse;
import com.findit.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Fetch user profile.
     * GET /api/users/profile/{id}
     */
    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long id) {
        UserProfileResponse profile = userService.getUserProfile(id);
        return ResponseEntity.ok(profile);
    }

    /**
     * Update user profile.
     * PUT /api/users/profile/{id}
     */
    @PutMapping("/profile/{id}")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProfileRequest request) {
        UserProfileResponse updated = userService.updateProfile(id, request);
        return ResponseEntity.ok(updated);
    }
}
