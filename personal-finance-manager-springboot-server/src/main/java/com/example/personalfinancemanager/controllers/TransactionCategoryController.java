package com.example.personalfinancemanager.controllers;

import com.example.personalfinancemanager.entities.TransactionCategory;

import com.example.personalfinancemanager.services.TransactionCategoryService;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/transaction-category")
public class TransactionCategoryController {

    private static final Logger logger = Logger.getLogger(
            TransactionCategoryController.class.getName());

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(
            TransactionCategoryController.class);

    @Autowired
    private TransactionCategoryService transactionCategoryService;

    // GET

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionCategory>> getAllTransactionCategoriesByUserId(
            @PathVariable int userId) {

        logger.info(
                "Getting all transaction categories from user: "
                        + userId);

        List<TransactionCategory> transactionCategories = transactionCategoryService
                .getAllTransactionCategoriesByUserId(
                        userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionCategories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionCategory> getTransactionCategoryById(
            @PathVariable int id) {

        logger.info(
                "Getting Transaction Category with id: "
                        + id);

        Optional<TransactionCategory> transactionCategoryOptional = transactionCategoryService
                .getTransactionCategoryById(id);

        if (transactionCategoryOptional.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionCategoryOptional.get());
    }

    // POST

    @PostMapping
    public ResponseEntity<TransactionCategory> createTransactionCategory(
            @RequestBody TransactionCategory transactionCategory) {

        logger.info(
                "Create Transaction Category for: "
                        + transactionCategory.getCategoryName());

        transactionCategoryService
                .createTransactionCategory(
                        transactionCategory
                                .getUser()
                                .getId(),

                        transactionCategory
                                .getCategoryName(),

                        transactionCategory
                                .getCategoryColor());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    // UPDATE

    @PutMapping("/{id}")
    public ResponseEntity<TransactionCategory> updateTransactionCategoryById(
            @PathVariable int id,

            @RequestParam String newCategoryName,

            @RequestParam String newCategoryColor) {

        logger.info(
                "Updating transaction category with id: "
                        + id);

        TransactionCategory updatedTransactionCategory = transactionCategoryService
                .updateTransactionCategoryById(
                        id,
                        newCategoryName,
                        newCategoryColor);

        if (updatedTransactionCategory == null) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedTransactionCategory);
    }

    // DELETE

    @DeleteMapping("/{id}")
    public ResponseEntity<TransactionCategory> deleteTransactionCategoryById(
            @PathVariable int id) {

        logger.info(
                "Deleting transaction category with id: "
                        + id);

        if (!transactionCategoryService
                .deleteTransactionCategoryById(id)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}