package com.example.personalfinancemanager.dtos;

import java.time.LocalDate;

public class SavingsGoalResponse {

    private Integer id;
    private String goalName;
    private Double targetAmount;
    private LocalDate targetDate;
    private LocalDate startDate;
    private Double currentProgress;
    private Double progressPercentage;
    private Double remainingAmount;

    public SavingsGoalResponse() {
    }

    public SavingsGoalResponse(Integer id, String goalName, Double targetAmount,
                               LocalDate targetDate, LocalDate startDate, Double currentProgress) {
        this.id = id;
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.targetDate = targetDate;
        this.startDate = startDate;
        this.currentProgress = currentProgress != null ? currentProgress : 0.0;
        
        this.progressPercentage = targetAmount > 0
                ? Math.min(100.0, (this.currentProgress / targetAmount) * 100.0)
                : 0.0;
        // Format to 2 decimal places manually to match tests if needed, but Double is fine.
        this.progressPercentage = Math.round(this.progressPercentage * 100.0) / 100.0;

        this.remainingAmount = targetAmount - this.currentProgress;
        if (this.remainingAmount < 0) {
            this.remainingAmount = 0.0;
        }
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

    public Double getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(Double currentProgress) {
        this.currentProgress = currentProgress;
    }

    public Double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public Double getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(Double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }
}
