package com.example.personalfinancemanager.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TransactionUpdateRequest {

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be a positive decimal value")
    private Double amount;

    @NotBlank(message = "Category cannot be blank")
    private String category;

    private String description;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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
