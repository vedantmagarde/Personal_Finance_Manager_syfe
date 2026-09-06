package com.example.personalfinancemanager.controllers;

import com.example.personalfinancemanager.dtos.UserLoginRequest;
import com.example.personalfinancemanager.dtos.UserProfileUpdateRequest;
import com.example.personalfinancemanager.dtos.UserRegistrationRequest;
import com.example.personalfinancemanager.repositories.TransactionCategoryRepository;
import com.example.personalfinancemanager.repositories.TransactionRepository;
import com.example.personalfinancemanager.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private TransactionCategoryRepository transactionCategoryRepository;
    @Autowired private ObjectMapper objectMapper;

    private MockHttpSession session;

    @BeforeEach
    public void setup() throws Exception {
        transactionRepository.deleteAll();
        transactionCategoryRepository.deleteAll();
        userRepository.deleteAll();

        UserRegistrationRequest reg = new UserRegistrationRequest();
        reg.setUsername("profile@example.com");
        reg.setPassword("securePassword123");
        reg.setFullName("Profile User");
        reg.setPhoneNumber("9001234567");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reg)))
                .andExpect(status().isCreated());

        UserLoginRequest login = new UserLoginRequest();
        login.setUsername("profile@example.com");
        login.setPassword("securePassword123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn();

        session = (MockHttpSession) result.getRequest().getSession();
    }

    @Test
    public void testGetProfile_Success() throws Exception {
        mockMvc.perform(get("/api/user/profile").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("profile@example.com"))
                .andExpect(jsonPath("$.fullName").value("Profile User"))
                .andExpect(jsonPath("$.phoneNumber").value("9001234567"));
    }

    @Test
    public void testUpdateProfile_Success() throws Exception {
        UserProfileUpdateRequest update = new UserProfileUpdateRequest();
        update.setFullName("Updated Name");
        update.setPhoneNumber("9999999999");

        mockMvc.perform(put("/api/user/profile")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Updated Name"))
                .andExpect(jsonPath("$.phoneNumber").value("9999999999"))
                .andExpect(jsonPath("$.email").value("profile@example.com")); // email unchanged
    }

    @Test
    public void testUpdateProfile_BlankName_BadRequest() throws Exception {
        UserProfileUpdateRequest update = new UserProfileUpdateRequest();
        update.setFullName(""); // blank

        mockMvc.perform(put("/api/user/profile")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fullName").exists());
    }

    @Test
    public void testUnauthenticatedAccess() throws Exception {
        mockMvc.perform(get("/api/user/profile"))
                .andExpect(status().isUnauthorized());
    }
}
