package com.example.personalfinancemanager.dtos;

import java.util.List;

public class GoalListResponse {

    private List<SavingsGoalResponse> goals;

    public GoalListResponse() {
    }

    public GoalListResponse(List<SavingsGoalResponse> goals) {
        this.goals = goals;
    }

    public List<SavingsGoalResponse> getGoals() {
        return goals;
    }

    public void setGoals(List<SavingsGoalResponse> goals) {
        this.goals = goals;
    }
}
