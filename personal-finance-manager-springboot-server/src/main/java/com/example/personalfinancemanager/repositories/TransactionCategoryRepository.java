package com.example.personalfinancemanager.repositories;

import com.example.personalfinancemanager.entities.TransactionCategory;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, Integer> {

    // Used in Phase 2: TransactionService - finds a category by name accessible to a user
    @Query("SELECT c FROM TransactionCategory c WHERE c.categoryName = :name AND (c.user.id = :userId OR c.isCustom = false)")
    Optional<TransactionCategory> findByNameForUser(@Param("name") String name, @Param("userId") Integer userId);

    // Phase 3: Get all categories visible to a user (user's own + all default ones)
    @Query("SELECT c FROM TransactionCategory c WHERE c.user.id = :userId OR c.isCustom = false ORDER BY c.isCustom ASC, c.categoryName ASC")
    List<TransactionCategory> findAllForUser(@Param("userId") Integer userId);

    // Phase 3: Find a custom category by name belonging to a specific user
    Optional<TransactionCategory> findByCategoryNameAndUserId(String categoryName, Integer userId);

    // Phase 3: Find a default (non-custom) category by name
    Optional<TransactionCategory> findByCategoryNameAndIsCustomFalse(String categoryName);

    // Phase 3: Check if a category name already exists for a user or as a default
    @Query("SELECT COUNT(c) > 0 FROM TransactionCategory c WHERE c.categoryName = :name AND (c.user.id = :userId OR c.isCustom = false)")
    boolean existsByNameForUser(@Param("name") String name, @Param("userId") Integer userId);

    // Phase 3: Check if a category is used by any transaction of a user
    @Query("SELECT COUNT(t) > 0 FROM Transaction t WHERE t.transactionCategory.id = :categoryId AND t.user.id = :userId")
    boolean isCategoryInUseByUser(@Param("categoryId") Integer categoryId, @Param("userId") Integer userId);
}