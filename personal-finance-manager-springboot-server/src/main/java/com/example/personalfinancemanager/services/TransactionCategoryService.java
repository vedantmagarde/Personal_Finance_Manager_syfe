package com.example.personalfinancemanager.services;

import com.example.personalfinancemanager.dtos.CategoryRequest;
import com.example.personalfinancemanager.dtos.CategoryResponse;
import com.example.personalfinancemanager.entities.TransactionCategory;
import com.example.personalfinancemanager.entities.User;
import com.example.personalfinancemanager.exceptions.ConflictException;
import com.example.personalfinancemanager.exceptions.ForbiddenException;
import com.example.personalfinancemanager.exceptions.ResourceNotFoundException;
import com.example.personalfinancemanager.repositories.TransactionCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionCategoryService {

    private final TransactionCategoryRepository transactionCategoryRepository;

    @Autowired
    public TransactionCategoryService(TransactionCategoryRepository transactionCategoryRepository) {
        this.transactionCategoryRepository = transactionCategoryRepository;
    }

    public List<CategoryResponse> getCategories(User user) {
        List<TransactionCategory> categories = transactionCategoryRepository.findAllForUser(user.getId());
        return categories.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public CategoryResponse createCategory(CategoryRequest request, User user) {
        // 409 Conflict if a category with this name already exists (default or user's own)
        if (transactionCategoryRepository.existsByNameForUser(request.getName(), user.getId())) {
            throw new ConflictException("Category with name '" + request.getName() + "' already exists");
        }

        TransactionCategory category = new TransactionCategory();
        category.setCategoryName(request.getName());
        category.setType(request.getType());
        category.setCustom(true);
        category.setUser(user);

        TransactionCategory saved = transactionCategoryRepository.save(category);
        return mapToResponse(saved);
    }

    public void deleteCategory(String name, User user) {
        // First try to find a custom category owned by this user
        java.util.Optional<TransactionCategory> userCategoryOpt =
                transactionCategoryRepository.findByCategoryNameAndUserId(name, user.getId());

        if (userCategoryOpt.isPresent()) {
            TransactionCategory category = userCategoryOpt.get();
            // 403 if the category is used by any of the user's transactions
            if (transactionCategoryRepository.isCategoryInUseByUser(category.getId(), user.getId())) {
                throw new ForbiddenException("Cannot delete category: it is currently in use by one or more transactions");
            }
            transactionCategoryRepository.delete(category);
            return;
        }

        // If not found as user's custom category, check if it's a default one
        if (transactionCategoryRepository.findByCategoryNameAndIsCustomFalse(name).isPresent()) {
            throw new ForbiddenException("Cannot delete a default category");
        }

        // Not found at all
        throw new ResourceNotFoundException("Category '" + name + "' not found");
    }

    private CategoryResponse mapToResponse(TransactionCategory category) {
        return new CategoryResponse(
                category.getCategoryName(),
                category.getType(),
                category.isCustom()
        );
    }
}