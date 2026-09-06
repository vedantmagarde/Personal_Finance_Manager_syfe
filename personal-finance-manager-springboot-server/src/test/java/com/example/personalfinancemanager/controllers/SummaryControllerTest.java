package com.example.personalfinancemanager.controllers;

import com.example.personalfinancemanager.dtos.TransactionRequest;
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

@SpringBootTest
@AutoConfigureMockMvc
public class SummaryControllerTest {

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
        reg.setUsername("summary@example.com");
        reg.setPassword("securePassword123");
        reg.setFullName("Summary User");
        reg.setPhoneNumber("8001234567");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reg)))
                .andExpect(status().isCreated());

        UserLoginRequest login = new UserLoginRequest();
        login.setUsername("summary@example.com");
        login.setPassword("securePassword123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn();

        session = (MockHttpSession) result.getRequest().getSession();

        // Seed categories
        User user = userRepository.findByEmail("summary@example.com").get();

        TransactionCategory salaryCategory = new TransactionCategory();
        salaryCategory.setCategoryName("Salary");
        salaryCategory.setType("INCOME");
        salaryCategory.setCustom(false);
        salaryCategory.setUser(null);
        transactionCategoryRepository.save(salaryCategory);

        TransactionCategory rentCategory = new TransactionCategory();
        rentCategory.setCategoryName("Rent");
        rentCategory.setType("EXPENSE");
        rentCategory.setCustom(false);
        rentCategory.setUser(null);
        transactionCategoryRepository.save(rentCategory);

        // Create transactions
        TransactionRequest income = new TransactionRequest();
        income.setAmount(80000.0);
        income.setDate(LocalDate.now());
        income.setCategory("Salary");
        income.setDescription("Monthly salary");

        mockMvc.perform(post("/api/transactions")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(income)))
                .andExpect(status().isCreated());

        TransactionRequest expense = new TransactionRequest();
        expense.setAmount(20000.0);
        expense.setDate(LocalDate.now());
        expense.setCategory("Rent");
        expense.setDescription("Monthly rent");

        mockMvc.perform(post("/api/transactions")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expense)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetSummary_AllTime() throws Exception {
        mockMvc.perform(get("/api/summary").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(80000.0))
                .andExpect(jsonPath("$.totalExpenses").value(20000.0))
                .andExpect(jsonPath("$.netSavings").value(60000.0))
                .andExpect(jsonPath("$.incomeByCategory.Salary").value(80000.0))
                .andExpect(jsonPath("$.expensesByCategory.Rent").value(20000.0));
    }

    @Test
    public void testGetSummary_ByYearMonth() throws Exception {
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        mockMvc.perform(get("/api/summary")
                        .session(session)
                        .param("year", String.valueOf(year))
                        .param("month", String.valueOf(month)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(80000.0))
                .andExpect(jsonPath("$.netSavings").value(60000.0));
    }

    @Test
    public void testGetSummary_FutureMonth_ReturnsZero() throws Exception {
        mockMvc.perform(get("/api/summary")
                        .session(session)
                        .param("year", "2099")
                        .param("month", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(0.0))
                .andExpect(jsonPath("$.totalExpenses").value(0.0))
                .andExpect(jsonPath("$.netSavings").value(0.0));
    }

    @Test
    public void testUnauthenticatedAccess() throws Exception {
        mockMvc.perform(get("/api/summary"))
                .andExpect(status().isUnauthorized());
    }
}
