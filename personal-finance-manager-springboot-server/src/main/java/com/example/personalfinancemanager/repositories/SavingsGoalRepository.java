package com.example.personalfinancemanager.repositories;

import com.example.personalfinancemanager.entities.SavingsGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Integer> {

    List<SavingsGoal> findAllByUserIdOrderByTargetDateAsc(Integer userId);
}
