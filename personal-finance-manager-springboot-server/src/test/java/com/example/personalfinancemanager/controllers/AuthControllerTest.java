package com.example.personalfinancemanager.controllers;

import com.example.personalfinancemanager.dtos.UserLoginRequest;
import com.example.personalfinancemanager.dtos.UserRegistrationRequest;
import com.example.personalfinancemanager.entities.User;
import com.example.personalfinancemanager.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRegistrationRequest registrationRequest;
    private UserLoginRequest loginRequest;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();

        registrationRequest = new UserRegistrationRequest();
        registrationRequest.setUsername("test@example.com");
        registrationRequest.setPassword("securePassword123");
        registrationRequest.setFullName("Test User");
        registrationRequest.setPhoneNumber("1234567890");

        loginRequest = new UserLoginRequest();
        loginRequest.setUsername("test@example.com");
        loginRequest.setPassword("securePassword123");
    }

    @Test
    public void testRegister_Success() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.userId").exists());
    }

    @Test
    public void testRegister_Conflict() throws Exception {
        // First request to register
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated());

        // Second request should fail with conflict
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Username (email) is already registered"));
    }

    @Test
    public void testRegister_BadRequest() throws Exception {
        registrationRequest.setUsername("invalid-email"); // Invalid email

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").exists());
    }

    @Test
    public void testLogin_Success() throws Exception {
        // Register user first
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated());

        // Now login
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    public void testLogin_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}
