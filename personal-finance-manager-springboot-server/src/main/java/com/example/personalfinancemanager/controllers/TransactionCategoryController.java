package com.example.personalfinancemanager.controllers;

import com.example.personalfinancemanager.dtos.CategoryListResponse;
import com.example.personalfinancemanager.dtos.CategoryRequest;
import com.example.personalfinancemanager.dtos.CategoryResponse;
import com.example.personalfinancemanager.security.CustomUserDetails;
import com.example.personalfinancemanager.services.TransactionCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class TransactionCategoryController {

    private final TransactionCategoryService transactionCategoryService;

    @Autowired
    public TransactionCategoryController(TransactionCategoryService transactionCategoryService) {
        this.transactionCategoryService = transactionCategoryService;
    }

    @GetMapping
    public ResponseEntity<CategoryListResponse> getCategories(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<CategoryResponse> categories = transactionCategoryService.getCategories(userDetails.getUser());
        return ResponseEntity.ok(new CategoryListResponse(categories));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        CategoryResponse response = transactionCategoryService.createCategory(request, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Map<String, String>> deleteCategory(
            @PathVariable String name,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        transactionCategoryService.deleteCategory(name, userDetails.getUser());
        return ResponseEntity.ok(Collections.singletonMap("message", "Category deleted successfully"));
    }
}