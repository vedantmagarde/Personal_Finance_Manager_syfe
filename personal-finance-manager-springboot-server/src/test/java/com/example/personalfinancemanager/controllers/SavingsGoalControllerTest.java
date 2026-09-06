package com.example.personalfinancemanager.controllers;

import com.example.personalfinancemanager.dtos.SavingsGoalRequest;
import com.example.personalfinancemanager.dtos.SavingsGoalUpdateRequest;
import com.example.personalfinancemanager.dtos.UserLoginRequest;
import com.example.personalfinancemanager.dtos.UserRegistrationRequest;
import com.example.personalfinancemanager.repositories.SavingsGoalRepository;
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
public class SavingsGoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SavingsGoalRepository savingsGoalRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionCategoryRepository transactionCategoryRepository;

    private MockHttpSession session;

    @BeforeEach
    public void setup() throws Exception {
        transactionRepository.deleteAll();
        transactionCategoryRepository.deleteAll();
        savingsGoalRepository.deleteAll();
        userRepository.deleteAll();

        // Register
        UserRegistrationRequest reg = new UserRegistrationRequest();
        reg.setUsername("goals@example.com");
        reg.setPassword("securePassword123");
        reg.setFullName("Goals User");
        reg.setPhoneNumber("1122334455");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reg)))
                .andExpect(status().isCreated());

        // Login
        UserLoginRequest login = new UserLoginRequest();
        login.setUsername("goals@example.com");
        login.setPassword("securePassword123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn();

        session = (MockHttpSession) result.getRequest().getSession();
    }

    private SavingsGoalRequest buildGoalRequest(String name, double target) {
        SavingsGoalRequest req = new SavingsGoalRequest();
        req.setGoalName(name);
        req.setTargetAmount(target);
        req.setTargetDate(LocalDate.now().plusMonths(6));
        req.setStartDate(LocalDate.now());
        return req;
    }

    @Test
    public void testCreateGoal_Success() throws Exception {
        SavingsGoalRequest request = buildGoalRequest("Emergency Fund", 5000.0);

        mockMvc.perform(post("/api/goals")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.goalName").value("Emergency Fund"))
                .andExpect(jsonPath("$.targetAmount").value(5000.0))
                .andExpect(jsonPath("$.currentProgress").value(0.0))
                .andExpect(jsonPath("$.progressPercentage").value(0.0))
                .andExpect(jsonPath("$.remainingAmount").value(5000.0));
    }

    @Test
    public void testCreateGoal_InvalidAmount_BadRequest() throws Exception {
        SavingsGoalRequest request = new SavingsGoalRequest();
        request.setGoalName("Bad Goal");
        request.setTargetAmount(-100.0); // negative amount
        request.setTargetDate(LocalDate.now().plusMonths(3));

        mockMvc.perform(post("/api/goals")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.targetAmount").exists());
    }

    @Test
    public void testGetGoals_Empty() throws Exception {
        mockMvc.perform(get("/api/goals").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goals", hasSize(0)));
    }

    @Test
    public void testGetGoals_WithData() throws Exception {
        // Create a goal first
        mockMvc.perform(post("/api/goals")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildGoalRequest("Vacation Fund", 3000.0))))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/goals").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goals", hasSize(1)))
                .andExpect(jsonPath("$.goals[0].goalName").value("Vacation Fund"));
    }

    @Test
    public void testUpdateGoal() throws Exception {
        // Create
        MvcResult createResult = mockMvc.perform(post("/api/goals")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildGoalRequest("Car Fund", 10000.0))))
                .andExpect(status().isCreated())
                .andReturn();

        Integer id = com.jayway.jsonpath.JsonPath.parse(
                createResult.getResponse().getContentAsString()).read("$.id");

        // Update progress
        SavingsGoalUpdateRequest updateRequest = new SavingsGoalUpdateRequest();
        updateRequest.setTargetAmount(8000.0);
        updateRequest.setTargetDate(LocalDate.now().plusMonths(2));

        mockMvc.perform(put("/api/goals/" + id)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.targetAmount").value(8000.0))
                .andExpect(jsonPath("$.remainingAmount").value(8000.0));
    }

    @Test
    public void testDeleteGoal_Success() throws Exception {
        // Create
        MvcResult createResult = mockMvc.perform(post("/api/goals")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildGoalRequest("Laptop Fund", 8000.0))))
                .andExpect(status().isCreated())
                .andReturn();

        Integer id = com.jayway.jsonpath.JsonPath.parse(
                createResult.getResponse().getContentAsString()).read("$.id");

        // Delete
        mockMvc.perform(delete("/api/goals/" + id).session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Goal deleted successfully"));

        // Verify gone
        mockMvc.perform(get("/api/goals").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goals", hasSize(0)));
    }

    @Test
    public void testDeleteGoal_NotFound() throws Exception {
        mockMvc.perform(delete("/api/goals/9999").session(session))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUnauthenticatedAccess() throws Exception {
        mockMvc.perform(get("/api/goals"))
                .andExpect(status().isUnauthorized());
    }
}
