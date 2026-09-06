package com.example.personalfinancemanager.services;

import com.example.personalfinancemanager.dtos.MonthlyReportResponse;
import com.example.personalfinancemanager.dtos.YearlyReportResponse;
import com.example.personalfinancemanager.entities.Transaction;
import com.example.personalfinancemanager.entities.User;
import com.example.personalfinancemanager.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public ReportService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public MonthlyReportResponse getMonthlyReport(User user, int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate startDate = ym.atDay(1);
        LocalDate endDate = ym.atEndOfMonth();

        List<Transaction> transactions = transactionRepository
                .findFilteredTransactions(user.getId(), startDate, endDate, null);

        Map<String, Double> totalIncome = new HashMap<>();
        Map<String, Double> totalExpenses = new HashMap<>();
        double totalIncSum = 0.0;
        double totalExpSum = 0.0;

        for (Transaction t : transactions) {
            String type = t.getTransactionCategory() != null
                    ? t.getTransactionCategory().getType()
                    : "EXPENSE";
            String categoryName = t.getTransactionCategory() != null
                    ? t.getTransactionCategory().getCategoryName()
                    : "Uncategorized";
            double amount = t.getAmount();

            if ("INCOME".equalsIgnoreCase(type)) {
                totalIncSum += amount;
                totalIncome.merge(categoryName, amount, Double::sum);
            } else {
                totalExpSum += amount;
                totalExpenses.merge(categoryName, amount, Double::sum);
            }
        }

        return new MonthlyReportResponse(month, year, totalIncome, totalExpenses, totalIncSum - totalExpSum);
    }

    public YearlyReportResponse getYearlyReport(User user, int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        List<Transaction> transactions = transactionRepository
                .findFilteredTransactions(user.getId(), startDate, endDate, null);

        Map<String, Double> totalIncome = new HashMap<>();
        Map<String, Double> totalExpenses = new HashMap<>();
        double totalIncSum = 0.0;
        double totalExpSum = 0.0;

        for (Transaction t : transactions) {
            String type = t.getTransactionCategory() != null
                    ? t.getTransactionCategory().getType()
                    : "EXPENSE";
            String categoryName = t.getTransactionCategory() != null
                    ? t.getTransactionCategory().getCategoryName()
                    : "Uncategorized";
            double amount = t.getAmount();

            if ("INCOME".equalsIgnoreCase(type)) {
                totalIncSum += amount;
                totalIncome.merge(categoryName, amount, Double::sum);
            } else {
                totalExpSum += amount;
                totalExpenses.merge(categoryName, amount, Double::sum);
            }
        }

        return new YearlyReportResponse(year, totalIncome, totalExpenses, totalIncSum - totalExpSum);
    }
}
