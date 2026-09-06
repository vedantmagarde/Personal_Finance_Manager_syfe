package com.example.personalfinancemanager;

import com.example.personalfinancemanager.entities.TransactionCategory;
import com.example.personalfinancemanager.repositories.TransactionCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final TransactionCategoryRepository categoryRepository;

    @Autowired
    public DatabaseSeeder(TransactionCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedDefaultCategories();
    }

    private void seedDefaultCategories() {
        // INCOME defaults
        createIfNotExists("Salary", "INCOME");

        // EXPENSE defaults
        createIfNotExists("Food", "EXPENSE");
        createIfNotExists("Rent", "EXPENSE");
        createIfNotExists("Transportation", "EXPENSE");
        createIfNotExists("Entertainment", "EXPENSE");
        createIfNotExists("Healthcare", "EXPENSE");
        createIfNotExists("Utilities", "EXPENSE");
    }

    private void createIfNotExists(String name, String type) {
        List<TransactionCategory> existing = categoryRepository.findByUserIsNull();
        boolean exists = existing.stream().anyMatch(c -> c.getCategoryName().equalsIgnoreCase(name));

        if (!exists) {
            TransactionCategory category = new TransactionCategory();
            category.setCategoryName(name);
            category.setType(type);
            category.setCustom(false);
            category.setUser(null);
            categoryRepository.save(category);
        }
    }
}
