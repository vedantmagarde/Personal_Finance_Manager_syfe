package com.example.personalfinancemanager.repositories;

import com.example.personalfinancemanager.entities.TransactionCategory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionCategoryRepository
        extends JpaRepository<TransactionCategory, Integer> {

    List<TransactionCategory> findAllByUserId(int userId);
}