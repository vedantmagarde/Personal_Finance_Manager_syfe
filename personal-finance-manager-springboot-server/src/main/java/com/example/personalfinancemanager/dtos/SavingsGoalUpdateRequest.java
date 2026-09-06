package com.example.personalfinancemanager.dtos;

import java.time.LocalDate;

public class SavingsGoalUpdateRequest {

    private Double targetAmount;
    private LocalDate targetDate;

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
}
