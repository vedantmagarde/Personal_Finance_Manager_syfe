package com.example.personalfinancemanager.dtos;

import java.util.Map;

public class MonthlyReportResponse {

    private Integer month;
    private Integer year;
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(contentUsing = com.example.personalfinancemanager.config.MoneySerializer.class)
    private Map<String, Double> totalIncome;
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(contentUsing = com.example.personalfinancemanager.config.MoneySerializer.class)
    private Map<String, Double> totalExpenses;
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = com.example.personalfinancemanager.config.MoneySerializer.class)
    private Double netSavings;

    public MonthlyReportResponse() {
    }

    public MonthlyReportResponse(Integer month, Integer year, Map<String, Double> totalIncome,
                                 Map<String, Double> totalExpenses, Double netSavings) {
        this.month = month;
        this.year = year;
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.netSavings = netSavings;
    }

    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Map<String, Double> getTotalIncome() { return totalIncome; }
    public void setTotalIncome(Map<String, Double> totalIncome) { this.totalIncome = totalIncome; }

    public Map<String, Double> getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(Map<String, Double> totalExpenses) { this.totalExpenses = totalExpenses; }

    public Double getNetSavings() { return netSavings; }
    public void setNetSavings(Double netSavings) { this.netSavings = netSavings; }
}
