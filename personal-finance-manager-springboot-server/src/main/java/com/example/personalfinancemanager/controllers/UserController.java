package com.example.personalfinancemanager.controllers;

import com.example.personalfinancemanager.dtos.UserProfileResponse;
import com.example.personalfinancemanager.dtos.UserProfileUpdateRequest;
import com.example.personalfinancemanager.security.CustomUserDetails;
import com.example.personalfinancemanager.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfileResponse profile = userService.getProfile(userDetails.getUser());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @Valid @RequestBody UserProfileUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfileResponse updated = userService.updateProfile(userDetails.getUser(), request);
        return ResponseEntity.ok(updated);
    }
}
