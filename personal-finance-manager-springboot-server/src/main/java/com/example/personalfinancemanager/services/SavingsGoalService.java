package com.example.personalfinancemanager.services;

import com.example.personalfinancemanager.dtos.SavingsGoalRequest;
import com.example.personalfinancemanager.dtos.SavingsGoalResponse;
import com.example.personalfinancemanager.dtos.SavingsGoalUpdateRequest;
import com.example.personalfinancemanager.entities.SavingsGoal;
import com.example.personalfinancemanager.entities.User;
import com.example.personalfinancemanager.exceptions.ResourceNotFoundException;
import com.example.personalfinancemanager.repositories.SavingsGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavingsGoalService {

    private final SavingsGoalRepository savingsGoalRepository;

    @Autowired
    public SavingsGoalService(SavingsGoalRepository savingsGoalRepository) {
        this.savingsGoalRepository = savingsGoalRepository;
    }

    public List<SavingsGoalResponse> getGoals(User user) {
        return savingsGoalRepository.findAllByUserIdOrderByTargetDateAsc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SavingsGoalResponse createGoal(SavingsGoalRequest request, User user) {
        SavingsGoal goal = new SavingsGoal();
        goal.setGoalName(request.getGoalName());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setCurrentAmount(0.0);
        goal.setTargetDate(request.getTargetDate());
        goal.setUser(user);

        SavingsGoal saved = savingsGoalRepository.save(goal);
        return mapToResponse(saved);
    }

    public SavingsGoalResponse updateGoalProgress(Integer id, SavingsGoalUpdateRequest request, User user) {
        SavingsGoal goal = savingsGoalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Savings goal not found"));

        if (!goal.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Savings goal not found");
        }

        goal.setCurrentAmount(request.getCurrentAmount());
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
        return new SavingsGoalResponse(
                goal.getId(),
                goal.getGoalName(),
                goal.getTargetAmount(),
                goal.getCurrentAmount(),
                goal.getTargetDate()
        );
    }
}
