package com.example.personalfinancemanager.controllers;

import com.example.personalfinancemanager.dtos.CategoryRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionCategoryControllerTest {

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
    private User testUser;

    @BeforeEach
    public void setup() throws Exception {
        transactionRepository.deleteAll();
        transactionCategoryRepository.deleteAll();
        userRepository.deleteAll();

        // 1. Register user
        UserRegistrationRequest reg = new UserRegistrationRequest();
        reg.setUsername("cattest@example.com");
        reg.setPassword("securePassword123");
        reg.setFullName("Cat Test User");
        reg.setPhoneNumber("9876543210");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reg)))
                .andExpect(status().isCreated());

        testUser = userRepository.findByEmail("cattest@example.com").get();

        // 2. Login to get session
        UserLoginRequest login = new UserLoginRequest();
        login.setUsername("cattest@example.com");
        login.setPassword("securePassword123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn();

        session = (MockHttpSession) result.getRequest().getSession();

        // 3. Seed a default (non-custom) category
        TransactionCategory defaultCat = new TransactionCategory();
        defaultCat.setCategoryName("Salary");
        defaultCat.setType("INCOME");
        defaultCat.setCustom(false);
        defaultCat.setUser(null); // Default categories have no user
        transactionCategoryRepository.save(defaultCat);
    }

    @Test
    public void testGetCategories_IncludesDefault() throws Exception {
        mockMvc.perform(get("/api/categories").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.categories[?(@.name == 'Salary')]").exists());
    }

    @Test
    public void testCreateCategory_Success() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Freelance");
        request.setType("INCOME");

        mockMvc.perform(post("/api/categories")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Freelance"))
                .andExpect(jsonPath("$.type").value("INCOME"))
                .andExpect(jsonPath("$.custom").value(true));
    }

    @Test
    public void testCreateCategory_DuplicateDefaultName_Conflict() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Salary"); // Already exists as a default
        request.setType("INCOME");

        mockMvc.perform(post("/api/categories")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value(containsString("Salary")));
    }

    @Test
    public void testCreateCategory_DuplicateCustomName_Conflict() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Freelance");
        request.setType("INCOME");

        // First create
        mockMvc.perform(post("/api/categories")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Second create – should conflict
        mockMvc.perform(post("/api/categories")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testCreateCategory_InvalidType_BadRequest() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Freelance");
        request.setType("INVALID_TYPE");

        mockMvc.perform(post("/api/categories")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").exists());
    }

    @Test
    public void testDeleteCustomCategory_Success() throws Exception {
        // Create a custom category first
        CategoryRequest request = new CategoryRequest();
        request.setName("SideProject");
        request.setType("INCOME");

        mockMvc.perform(post("/api/categories")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Now delete it
        mockMvc.perform(delete("/api/categories/SideProject").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Category deleted successfully"));
    }

    @Test
    public void testDeleteDefaultCategory_Forbidden() throws Exception {
        mockMvc.perform(delete("/api/categories/Salary").session(session))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Cannot delete a default category"));
    }

    @Test
    public void testDeleteNonExistentCategory_NotFound() throws Exception {
        mockMvc.perform(delete("/api/categories/NonExistent").session(session))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUnauthenticatedAccess() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isUnauthorized());
    }
}
