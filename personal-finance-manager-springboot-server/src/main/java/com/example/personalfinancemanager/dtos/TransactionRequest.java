package com.example.personalfinancemanager.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class TransactionRequest {

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be a positive decimal value")
    private Double amount;

    @NotNull(message = "Date cannot be null")
    @PastOrPresent(message = "Date cannot be a future date")
    private LocalDate date;

    @NotBlank(message = "Category cannot be blank")
    private String category;

    private String description;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
