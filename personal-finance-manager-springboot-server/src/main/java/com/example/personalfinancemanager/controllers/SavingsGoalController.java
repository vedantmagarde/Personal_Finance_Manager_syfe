package com.example.personalfinancemanager.controllers;

import com.example.personalfinancemanager.dtos.GoalListResponse;
import com.example.personalfinancemanager.dtos.SavingsGoalRequest;
import com.example.personalfinancemanager.dtos.SavingsGoalResponse;
import com.example.personalfinancemanager.dtos.SavingsGoalUpdateRequest;
import com.example.personalfinancemanager.security.CustomUserDetails;
import com.example.personalfinancemanager.services.SavingsGoalService;
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
@RequestMapping("/api/goals")
public class SavingsGoalController {

    private final SavingsGoalService savingsGoalService;

    @Autowired
    public SavingsGoalController(SavingsGoalService savingsGoalService) {
        this.savingsGoalService = savingsGoalService;
    }

    @GetMapping
    public ResponseEntity<GoalListResponse> getGoals(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<SavingsGoalResponse> goals = savingsGoalService.getGoals(userDetails.getUser());
        return ResponseEntity.ok(new GoalListResponse(goals));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SavingsGoalResponse> getGoal(
            @PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        SavingsGoalResponse response = savingsGoalService.getGoal(id, userDetails.getUser());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SavingsGoalResponse> createGoal(
            @Valid @RequestBody SavingsGoalRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        SavingsGoalResponse response = savingsGoalService.createGoal(request, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SavingsGoalResponse> updateGoal(
            @PathVariable Integer id,
            @Valid @RequestBody SavingsGoalUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        SavingsGoalResponse response = savingsGoalService.updateGoal(id, request, userDetails.getUser());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteGoal(
            @PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        savingsGoalService.deleteGoal(id, userDetails.getUser());
        return ResponseEntity.ok(Collections.singletonMap("message", "Goal deleted successfully"));
    }
}
