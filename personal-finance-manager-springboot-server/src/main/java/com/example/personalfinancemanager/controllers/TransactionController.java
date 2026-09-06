package com.example.personalfinancemanager.controllers;

import com.example.personalfinancemanager.dtos.TransactionListResponse;
import com.example.personalfinancemanager.dtos.TransactionRequest;
import com.example.personalfinancemanager.dtos.TransactionResponse;
import com.example.personalfinancemanager.dtos.TransactionUpdateRequest;
import com.example.personalfinancemanager.security.CustomUserDetails;
import com.example.personalfinancemanager.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        TransactionResponse response = transactionService.createTransaction(request, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<TransactionListResponse> getTransactions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String category,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        List<TransactionResponse> transactions = transactionService.getTransactions(userDetails.getUser(), startDate, endDate, category);
        return ResponseEntity.ok(new TransactionListResponse(transactions));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable Integer id,
            @Valid @RequestBody TransactionUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        TransactionResponse response = transactionService.updateTransaction(id, request, userDetails.getUser());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTransaction(
            @PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        transactionService.deleteTransaction(id, userDetails.getUser());
        return ResponseEntity.ok(Collections.singletonMap("message", "Transaction deleted successfully"));
    }
}