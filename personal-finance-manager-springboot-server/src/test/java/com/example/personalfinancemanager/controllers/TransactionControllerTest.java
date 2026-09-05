package com.example.personalfinancemanager.controllers;

import com.example.personalfinancemanager.dtos.TransactionRequest;
import com.example.personalfinancemanager.dtos.TransactionUpdateRequest;
import com.example.personalfinancemanager.dtos.UserLoginRequest;
import com.example.personalfinancemanager.dtos.UserRegistrationRequest;
import com.example.personalfinancemanager.entities.TransactionCategory;
import com.example.personalfinancemanager.entities.User;
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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionCategoryRepository transactionCategoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockHttpSession session;
    private TransactionCategory category;

    @BeforeEach
    public void setup() throws Exception {
        transactionRepository.deleteAll();
        transactionCategoryRepository.deleteAll();
        userRepository.deleteAll();

        // 1. Register User
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest();
        registrationRequest.setUsername("test@example.com");
        registrationRequest.setPassword("securePassword123");
        registrationRequest.setFullName("Test User");
        registrationRequest.setPhoneNumber("1234567890");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated());

        // 2. Login to get session
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername("test@example.com");
        loginRequest.setPassword("securePassword123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        session = (MockHttpSession) result.getRequest().getSession();

        // 3. Create a category for the user
        User user = userRepository.findByEmail("test@example.com").get();
        category = new TransactionCategory();
        category.setCategoryName("Salary");
        category.setType("INCOME");
        category.setCustom(false);
        category.setUser(user);
        transactionCategoryRepository.save(category);
    }

    @Test
    public void testCreateTransaction_Success() throws Exception {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(50000.00);
        request.setDate(LocalDate.now());
        request.setCategory("Salary");
        request.setDescription("January Salary");

        mockMvc.perform(post("/api/transactions")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.amount").value(50000.00))
                .andExpect(jsonPath("$.category").value("Salary"))
                .andExpect(jsonPath("$.description").value("January Salary"))
                .andExpect(jsonPath("$.type").value("INCOME"));
    }

    @Test
    public void testGetTransactions() throws Exception {
        // Create one transaction first
        TransactionRequest request = new TransactionRequest();
        request.setAmount(50000.00);
        request.setDate(LocalDate.now());
        request.setCategory("Salary");
        request.setDescription("January Salary");

        mockMvc.perform(post("/api/transactions")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Get transactions
        mockMvc.perform(get("/api/transactions")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactions", hasSize(1)))
                .andExpect(jsonPath("$.transactions[0].amount").value(50000.00));
    }

    @Test
    public void testUpdateTransaction() throws Exception {
        // Create
        TransactionRequest createRequest = new TransactionRequest();
        createRequest.setAmount(50000.00);
        createRequest.setDate(LocalDate.now());
        createRequest.setCategory("Salary");
        createRequest.setDescription("January Salary");

        MvcResult createResult = mockMvc.perform(post("/api/transactions")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseString = createResult.getResponse().getContentAsString();
        Integer id = com.jayway.jsonpath.JsonPath.parse(responseString).read("$.id");

        // Update
        TransactionUpdateRequest updateRequest = new TransactionUpdateRequest();
        updateRequest.setAmount(60000.00);
        updateRequest.setCategory("Salary");
        updateRequest.setDescription("Updated Salary");

        mockMvc.perform(put("/api/transactions/" + id)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(60000.00))
                .andExpect(jsonPath("$.description").value("Updated Salary"));
    }

    @Test
    public void testDeleteTransaction() throws Exception {
        // Create
        TransactionRequest createRequest = new TransactionRequest();
        createRequest.setAmount(50000.00);
        createRequest.setDate(LocalDate.now());
        createRequest.setCategory("Salary");
        createRequest.setDescription("January Salary");

        MvcResult createResult = mockMvc.perform(post("/api/transactions")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseString = createResult.getResponse().getContentAsString();
        Integer id = com.jayway.jsonpath.JsonPath.parse(responseString).read("$.id");

        // Delete
        mockMvc.perform(delete("/api/transactions/" + id)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transaction deleted successfully"));

        // Verify it's gone
        mockMvc.perform(get("/api/transactions")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactions", hasSize(0)));
    }

    @Test
    public void testUnauthenticatedAccess() throws Exception {
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isUnauthorized());
    }
}
