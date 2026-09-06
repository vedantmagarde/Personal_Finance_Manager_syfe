package com.example.personalfinancemanager.services;

import com.example.personalfinancemanager.dtos.TransactionRequest;
import com.example.personalfinancemanager.dtos.TransactionResponse;
import com.example.personalfinancemanager.dtos.TransactionUpdateRequest;
import com.example.personalfinancemanager.entities.Transaction;
import com.example.personalfinancemanager.entities.TransactionCategory;
import com.example.personalfinancemanager.entities.User;
import com.example.personalfinancemanager.exceptions.ResourceNotFoundException;
import com.example.personalfinancemanager.repositories.TransactionCategoryRepository;
import com.example.personalfinancemanager.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionCategoryRepository transactionCategoryRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
                              TransactionCategoryRepository transactionCategoryRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionCategoryRepository = transactionCategoryRepository;
    }

    public TransactionResponse createTransaction(TransactionRequest request, User user) {
        TransactionCategory category = transactionCategoryRepository.findByNameForUser(request.getCategory(), user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found or not accessible"));

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setDate(request.getDate());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionCategory(category);
        transaction.setUser(user);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapToResponse(savedTransaction);
    }

    public List<TransactionResponse> getTransactions(User user, LocalDate startDate, LocalDate endDate, Integer categoryId) {
        List<Transaction> transactions = transactionRepository.findFilteredTransactions(user.getId(), startDate, endDate, categoryId);
        return transactions.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public TransactionResponse updateTransaction(Integer id, TransactionUpdateRequest request, User user) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Transaction not found");
        }

        if (request.getCategory() != null) {
            TransactionCategory category = transactionCategoryRepository.findByNameForUser(request.getCategory(), user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found or not accessible"));
            transaction.setTransactionCategory(category);
        }

        if (request.getAmount() != null) {
            transaction.setAmount(request.getAmount());
        }
        
        if (request.getDescription() != null) {
            transaction.setDescription(request.getDescription());
        }

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return mapToResponse(updatedTransaction);
    }

    public void deleteTransaction(Integer id, User user) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Transaction not found");
        }

        transactionRepository.delete(transaction);
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setDate(transaction.getDate());
        response.setDescription(transaction.getDescription());
        
        if (transaction.getTransactionCategory() != null) {
            response.setCategory(transaction.getTransactionCategory().getCategoryName());
            response.setType(transaction.getTransactionCategory().getType());
        }
        
        return response;
    }
}