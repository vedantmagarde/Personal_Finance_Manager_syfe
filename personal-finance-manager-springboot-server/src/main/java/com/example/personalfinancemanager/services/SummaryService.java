package com.example.personalfinancemanager.services;

import com.example.personalfinancemanager.dtos.SummaryResponse;
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
public class SummaryService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public SummaryService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Builds a financial summary for the given user and optional period.
     * If year and month are both provided, scope to that month.
     * If only year is provided, scope to that full year.
     * If neither is provided, scope to all time.
     */
    public SummaryResponse getSummary(User user, Integer year, Integer month) {
        LocalDate startDate;
        LocalDate endDate;

        if (year != null && month != null) {
            YearMonth ym = YearMonth.of(year, month);
            startDate = ym.atDay(1);
            endDate = ym.atEndOfMonth();
        } else if (year != null) {
            startDate = LocalDate.of(year, 1, 1);
            endDate = LocalDate.of(year, 12, 31);
        } else {
            startDate = null;
            endDate = null;
        }

        List<Transaction> transactions = transactionRepository
                .findFilteredTransactions(user.getId(), startDate, endDate, null);

        double totalIncome = 0.0;
        double totalExpenses = 0.0;
        Map<String, Double> incomeByCategory = new HashMap<>();
        Map<String, Double> expensesByCategory = new HashMap<>();

        for (Transaction t : transactions) {
            String type = t.getTransactionCategory() != null
                    ? t.getTransactionCategory().getType()
                    : "EXPENSE";
            String categoryName = t.getTransactionCategory() != null
                    ? t.getTransactionCategory().getCategoryName()
                    : "Uncategorized";
            double amount = t.getAmount();

            if ("INCOME".equalsIgnoreCase(type)) {
                totalIncome += amount;
                incomeByCategory.merge(categoryName, amount, Double::sum);
            } else {
                totalExpenses += amount;
                expensesByCategory.merge(categoryName, amount, Double::sum);
            }
        }

        return new SummaryResponse(totalIncome, totalExpenses, incomeByCategory, expensesByCategory);
    }
}
