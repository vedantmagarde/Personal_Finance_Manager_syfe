package com.example.personalfinancemanager.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class SavingsGoalRequest {

    @NotBlank(message = "Goal name cannot be blank")
    private String goalName;

    @NotNull(message = "Target amount cannot be null")
    @Positive(message = "Target amount must be a positive value")
    private Double targetAmount;

    @Future(message = "Target date must be a future date")
    private LocalDate targetDate;

    private LocalDate startDate;

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public Double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
