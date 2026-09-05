package com.example.personalfinancemanager.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class SavingsGoalUpdateRequest {

    @NotNull(message = "Current amount cannot be null")
    @PositiveOrZero(message = "Current amount must be zero or a positive value")
    private Double currentAmount;

    public Double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Double currentAmount) {
        this.currentAmount = currentAmount;
    }
}
