package com.example.personalfinancemanager.dtos;

import java.time.LocalDate;

public class TransactionResponse {

    private Integer id;
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = com.example.personalfinancemanager.config.MoneySerializer.class)
    private Double amount;
    private LocalDate date;
    private String category;
    private String description;
    private String type; // INCOME or EXPENSE

    public TransactionResponse() {
    }

    public TransactionResponse(Integer id, Double amount, LocalDate date, String category, String description, String type) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.description = description;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
