package com.example.personalfinancemanager.dtos;

import java.util.Map;

public class MonthlyReportResponse {

    private Integer month;
    private Integer year;
    private java.util.Map<String, java.math.BigDecimal> totalIncome;
    private java.util.Map<String, java.math.BigDecimal> totalExpenses;
    private java.math.BigDecimal netSavings;

    public MonthlyReportResponse() {
    }

    public MonthlyReportResponse(Integer month, Integer year, Map<String, Double> totalIncome,
                                 Map<String, Double> totalExpenses, Double netSavings) {
        this.month = month;
        this.year = year;
        
        this.totalIncome = new java.util.HashMap<>();
        if (totalIncome != null) {
            for (Map.Entry<String, Double> entry : totalIncome.entrySet()) {
                this.totalIncome.put(entry.getKey(), java.math.BigDecimal.valueOf(entry.getValue()).setScale(2, java.math.RoundingMode.HALF_UP));
            }
        }
        
        this.totalExpenses = new java.util.HashMap<>();
        if (totalExpenses != null) {
            for (Map.Entry<String, Double> entry : totalExpenses.entrySet()) {
                this.totalExpenses.put(entry.getKey(), java.math.BigDecimal.valueOf(entry.getValue()).setScale(2, java.math.RoundingMode.HALF_UP));
            }
        }
        
        this.netSavings = netSavings != null
                ? (netSavings == 0.0 ? java.math.BigDecimal.ZERO : java.math.BigDecimal.valueOf(netSavings).setScale(2, java.math.RoundingMode.HALF_UP))
                : null;
    }

    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public java.util.Map<String, java.math.BigDecimal> getTotalIncome() { return totalIncome; }
    public void setTotalIncome(java.util.Map<String, java.math.BigDecimal> totalIncome) { this.totalIncome = totalIncome; }

    public java.util.Map<String, java.math.BigDecimal> getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(java.util.Map<String, java.math.BigDecimal> totalExpenses) { this.totalExpenses = totalExpenses; }

    public java.math.BigDecimal getNetSavings() { return netSavings; }
    public void setNetSavings(Double netSavings) { this.netSavings = netSavings != null ? java.math.BigDecimal.valueOf(netSavings).setScale(2, java.math.RoundingMode.HALF_UP) : null; }
}
