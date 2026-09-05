package com.example.personalfinancemanager.repositories;

import com.example.personalfinancemanager.entities.Transaction;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository
                extends JpaRepository<Transaction, Integer> {

        List<Transaction> findAllByUserIdOrderByTransactionDateDesc(
                        int userId,
                        Pageable pageable);

        List<Transaction> findAllByUserIdAndTransactionDateBetweenOrderByTransactionDateDesc(
                        int userId,
                        LocalDate startDate,
                        LocalDate endDate);

        @Query("""
                        SELECT DISTINCT YEAR(t.transactionDate)
                        FROM Transaction t
                        WHERE t.user.id = :userId
                        ORDER BY YEAR(t.transactionDate) DESC
                        """)
        List<Integer> findDistinctYearsByUserId(
                        @Param("userId") int userId);
}