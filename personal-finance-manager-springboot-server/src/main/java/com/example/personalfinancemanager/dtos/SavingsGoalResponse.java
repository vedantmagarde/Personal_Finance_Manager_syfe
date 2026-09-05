package com.example.personalfinancemanager.dtos;

import java.time.LocalDate;

public class SavingsGoalResponse {

    private Integer id;
    private String goalName;
    private Double targetAmount;
    private Double currentAmount;
    private LocalDate targetDate;
    private Double progressPercent; // Computed: (currentAmount / targetAmount) * 100

    public SavingsGoalResponse() {
    }

    public SavingsGoalResponse(Integer id, String goalName, Double targetAmount,
                               Double currentAmount, LocalDate targetDate) {
        this.id = id;
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.targetDate = targetDate;
        this.progressPercent = targetAmount > 0
                ? Math.min(100.0, (currentAmount / targetAmount) * 100.0)
                : 0.0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public Double getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(Double progressPercent) {
        this.progressPercent = progressPercent;
    }
}
