package com.example.personalfinancemanager.dtos;

import java.util.Map;

public class SummaryResponse {

    private Double totalIncome;
    private Double totalExpenses;
    private Double netSavings;
    private Map<String, Double> incomeByCategory;
    private Map<String, Double> expensesByCategory;

    public SummaryResponse() {
    }

    public SummaryResponse(Double totalIncome, Double totalExpenses,
                           Map<String, Double> incomeByCategory,
                           Map<String, Double> expensesByCategory) {
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.netSavings = totalIncome - totalExpenses;
        this.incomeByCategory = incomeByCategory;
        this.expensesByCategory = expensesByCategory;
    }

    public Double getTotalIncome() { return totalIncome; }
    public void setTotalIncome(Double totalIncome) { this.totalIncome = totalIncome; }

    public Double getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(Double totalExpenses) { this.totalExpenses = totalExpenses; }

    public Double getNetSavings() { return netSavings; }
    public void setNetSavings(Double netSavings) { this.netSavings = netSavings; }

    public Map<String, Double> getIncomeByCategory() { return incomeByCategory; }
    public void setIncomeByCategory(Map<String, Double> incomeByCategory) { this.incomeByCategory = incomeByCategory; }

    public Map<String, Double> getExpensesByCategory() { return expensesByCategory; }
    public void setExpensesByCategory(Map<String, Double> expensesByCategory) { this.expensesByCategory = expensesByCategory; }
}
