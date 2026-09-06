package com.example.personalfinancemanager.dtos;

import java.time.LocalDate;

public class SavingsGoalResponse {

    private Integer id;
    private String goalName;
    private java.math.BigDecimal targetAmount;
    private LocalDate targetDate;
    private LocalDate startDate;
    private java.math.BigDecimal currentProgress;
    private Double progressPercentage;
    private java.math.BigDecimal remainingAmount;

    public SavingsGoalResponse() {
    }

    public SavingsGoalResponse(Integer id, String goalName, Double targetAmount,
                               LocalDate targetDate, LocalDate startDate, Double currentProgress) {
        this.id = id;
        this.goalName = goalName;
        this.targetAmount = targetAmount != null ? java.math.BigDecimal.valueOf(targetAmount).setScale(2, java.math.RoundingMode.HALF_UP) : null;
        this.targetDate = targetDate;
        this.startDate = startDate;
        
        Double progress = currentProgress != null ? currentProgress : 0.0;
        this.currentProgress = (progress == 0.0)
                ? java.math.BigDecimal.ZERO
                : java.math.BigDecimal.valueOf(progress).setScale(2, java.math.RoundingMode.HALF_UP);
        
        this.progressPercentage = targetAmount != null && targetAmount > 0
                ? Math.min(100.0, (progress / targetAmount) * 100.0)
                : 0.0;
        this.progressPercentage = Math.round(this.progressPercentage * 100.0) / 100.0;

        Double remaining = (targetAmount != null ? targetAmount : 0.0) - progress;
        if (remaining < 0) remaining = 0.0;
        this.remainingAmount = (remaining == 0.0)
                ? java.math.BigDecimal.ZERO
                : java.math.BigDecimal.valueOf(remaining).setScale(2, java.math.RoundingMode.HALF_UP);
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

    public java.math.BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Double targetAmount) {
        this.targetAmount = targetAmount != null ? java.math.BigDecimal.valueOf(targetAmount).setScale(2, java.math.RoundingMode.HALF_UP) : null;
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

    public java.math.BigDecimal getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(Double currentProgress) {
        this.currentProgress = currentProgress != null ? java.math.BigDecimal.valueOf(currentProgress).setScale(2, java.math.RoundingMode.HALF_UP) : null;
    }

    public Double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public java.math.BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(Double remainingAmount) {
        this.remainingAmount = remainingAmount != null ? java.math.BigDecimal.valueOf(remainingAmount).setScale(2, java.math.RoundingMode.HALF_UP) : null;
    }
}
