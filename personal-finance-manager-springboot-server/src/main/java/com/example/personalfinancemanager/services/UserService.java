package com.example.personalfinancemanager.services;

import com.example.personalfinancemanager.dtos.UserProfileResponse;
import com.example.personalfinancemanager.dtos.UserProfileUpdateRequest;
import com.example.personalfinancemanager.dtos.UserRegistrationRequest;
import com.example.personalfinancemanager.entities.User;
import com.example.personalfinancemanager.exceptions.ConflictException;
import com.example.personalfinancemanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> getUserById(int userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User registerUser(UserRegistrationRequest request) {
        if (userRepository.findByEmail(request.getUsername()).isPresent()) {
            throw new ConflictException("Username (email) is already registered");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public UserProfileResponse getProfile(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getCreatedAt()
        );
    }

    public UserProfileResponse updateProfile(User user, UserProfileUpdateRequest request) {
        user.setFullName(request.getFullName());
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        User saved = userRepository.save(user);
        return new UserProfileResponse(
                saved.getId(),
                saved.getFullName(),
                saved.getEmail(),
                saved.getPhoneNumber(),
                saved.getCreatedAt()
        );
    }
}