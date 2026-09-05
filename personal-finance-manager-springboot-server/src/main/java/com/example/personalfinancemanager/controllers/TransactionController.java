package com.example.personalfinancemanager.controllers;

import com.example.personalfinancemanager.entities.Transaction;
import com.example.personalfinancemanager.services.TransactionService;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private static final Logger logger = Logger.getLogger(TransactionController.class.getName());

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    // GET ALL TRANSACTIONS
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getAllTransactionsByUserIdAndYearOrMonth(
            @PathVariable int userId,
            @RequestParam int year,
            @RequestParam(required = false) Integer month) {

        logger.info(
                "Getting all transactions with userId: "
                        + userId
                        + " @"
                        + year);

        List<Transaction> transactionsList;

        if (month == null) {

            transactionsList = transactionService
                    .getAllTransactionsByUserIdAndYear(
                            userId,
                            year);

        } else {

            transactionsList = transactionService
                    .getAllTransactionsByUserIdAndYearAndMonth(
                            userId,
                            year,
                            month);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionsList);
    }

    // GET RECENT TRANSACTIONS
    @GetMapping("/recent/user/{userId}")
    public ResponseEntity<List<Transaction>> getRecentTransactionsByUserId(
            @PathVariable int userId,
            @RequestParam int startPage,
            @RequestParam int endPage,
            @RequestParam int size) {

        logger.info(
                "Getting transactions for userId: "
                        + userId
                        + ", Page: ("
                        + startPage
                        + ","
                        + endPage
                        + ")");

        List<Transaction> recentTransactionList = transactionService.getRecentTransactionsByUserId(
                userId,
                startPage,
                endPage,
                size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(recentTransactionList);
    }

    // GET DISTINCT YEARS
    @GetMapping("/user/{userId}/years")
    public ResponseEntity<List<Integer>> getDistinctTransactionYears(
            @PathVariable int userId) {

        logger.info(
                "Getting distinct years for user: "
                        + userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        transactionService
                                .getDistinctTransactionYears(userId));
    }

    // CREATE TRANSACTION
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @RequestBody Transaction transaction) {

        logger.info("Creating Transaction");

        Transaction newTransaction = transactionService.createTransaction(transaction);

        if (newTransaction == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newTransaction);
    }

    // UPDATE TRANSACTION
    @PutMapping
    public ResponseEntity<Transaction> updateTransaction(
            @RequestBody Transaction transaction) {

        logger.info(
                "Updating transaction with id: "
                        + transaction.getId());

        Transaction updatedTransaction = transactionService.updateTransaction(transaction);

        if (updatedTransaction == null) {

            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedTransaction);
    }

    // DELETE TRANSACTION
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Transaction> deleteTransactionById(
            @PathVariable int transactionId) {

        logger.info(
                "Delete transaction with id: "
                        + transactionId);

        transactionService.deleteTransactionById(transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}