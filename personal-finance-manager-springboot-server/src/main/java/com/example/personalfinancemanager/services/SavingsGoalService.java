package com.example.personalfinancemanager.services;

import com.example.personalfinancemanager.dtos.SavingsGoalRequest;
import com.example.personalfinancemanager.dtos.SavingsGoalResponse;
import com.example.personalfinancemanager.dtos.SavingsGoalUpdateRequest;
import com.example.personalfinancemanager.entities.SavingsGoal;
import com.example.personalfinancemanager.entities.Transaction;
import com.example.personalfinancemanager.entities.User;
import com.example.personalfinancemanager.exceptions.ResourceNotFoundException;
import com.example.personalfinancemanager.repositories.SavingsGoalRepository;
import com.example.personalfinancemanager.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavingsGoalService {

    private final SavingsGoalRepository savingsGoalRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public SavingsGoalService(SavingsGoalRepository savingsGoalRepository, TransactionRepository transactionRepository) {
        this.savingsGoalRepository = savingsGoalRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<SavingsGoalResponse> getGoals(User user) {
        return savingsGoalRepository.findAllByUserIdOrderByTargetDateAsc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SavingsGoalResponse getGoal(Integer id, User user) {
        SavingsGoal goal = savingsGoalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Savings goal not found"));

        if (!goal.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Savings goal not found");
        }
        
        return mapToResponse(goal);
    }

    public SavingsGoalResponse createGoal(SavingsGoalRequest request, User user) {
        SavingsGoal goal = new SavingsGoal();
        goal.setGoalName(request.getGoalName());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setTargetDate(request.getTargetDate());
        
        if (request.getStartDate() != null) {
            goal.setStartDate(request.getStartDate());
        } else {
            goal.setStartDate(LocalDate.now());
        }
        
        goal.setUser(user);

        SavingsGoal saved = savingsGoalRepository.save(goal);
        return mapToResponse(saved);
    }

    public SavingsGoalResponse updateGoal(Integer id, SavingsGoalUpdateRequest request, User user) {
        SavingsGoal goal = savingsGoalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Savings goal not found"));

        if (!goal.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Savings goal not found");
        }

        if (request.getTargetAmount() != null) {
            goal.setTargetAmount(request.getTargetAmount());
        }
        if (request.getTargetDate() != null) {
            goal.setTargetDate(request.getTargetDate());
        }

        SavingsGoal updated = savingsGoalRepository.save(goal);
        return mapToResponse(updated);
    }

    public void deleteGoal(Integer id, User user) {
        SavingsGoal goal = savingsGoalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Savings goal not found"));

        if (!goal.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Savings goal not found");
        }

        savingsGoalRepository.delete(goal);
    }

    private SavingsGoalResponse mapToResponse(SavingsGoal goal) {
        // Calculate progress: Total Income - Total Expenses since start date
        List<Transaction> transactions = transactionRepository.findFilteredTransactions(
                goal.getUser().getId(), goal.getStartDate(), null, null);

        double totalIncome = 0.0;
        double totalExpenses = 0.0;

        for (Transaction t : transactions) {
            String type = t.getTransactionCategory() != null
                    ? t.getTransactionCategory().getType()
                    : "EXPENSE";

            if ("INCOME".equalsIgnoreCase(type)) {
                totalIncome += t.getAmount();
            } else {
                totalExpenses += t.getAmount();
            }
        }

        double currentProgress = totalIncome - totalExpenses;
        if (currentProgress < 0) {
            currentProgress = 0.0;
        }

        return new SavingsGoalResponse(
                goal.getId(),
                goal.getGoalName(),
                goal.getTargetAmount(),
                goal.getTargetDate(),
                goal.getStartDate(),
                currentProgress
        );
    }
}
