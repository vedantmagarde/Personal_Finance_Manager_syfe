package com.example.personalfinancemanager.controllers;

import com.example.personalfinancemanager.entities.User;

import com.example.personalfinancemanager.services.UserService;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private static final Logger logger = Logger.getLogger(
            UserController.class.getName());

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(
            UserController.class);

    @Autowired
    private UserService userService;

    // GET USER BY EMAIL

    @GetMapping
    public ResponseEntity<User> getUserByEmail(
            @RequestParam String email) {

        logger.info(
                "Getting user by email: "
                        + email);

        Optional<User> userOptional = userService.getUserByEmail(email);

        if (userOptional.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();

        } else {

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userOptional.get());
        }
    }

    // LOGIN USER

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(
            @RequestParam String email,
            @RequestParam String password) {

        Optional<User> userOptional = userService.getUserByEmail(email);

        if (userOptional.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        if (!password.equals(
                userOptional
                        .get()
                        .getPassword())) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    // CREATE USER

    @PostMapping
    public ResponseEntity<User> createUser(
            @RequestBody User user) {

        logger.info("Creating New User");

        User newUser = userService.createUser(
                user.getName(),
                user.getEmail(),
                user.getPassword());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newUser);
    }
}