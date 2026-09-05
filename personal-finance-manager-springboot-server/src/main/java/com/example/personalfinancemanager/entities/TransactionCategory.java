package com.example.personalfinancemanager.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "transaction_category")
public class TransactionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_type", nullable = false)
    private String type; // e.g., "INCOME" or "EXPENSE"

    @Column(name = "is_custom", nullable = false)
    private boolean isCustom = true; // default true for user created

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean custom) {
        isCustom = custom;
    }
}