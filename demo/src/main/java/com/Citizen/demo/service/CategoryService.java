package com.Citizen.demo.service;

import com.Citizen.demo.dto.CreateCategoryRequest;
import com.Citizen.demo.dto.UpdateCategoryRequest;
import com.Citizen.demo.entity.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CreateCategoryRequest request);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    Category updateCategory(Long id, UpdateCategoryRequest request);
    void deleteCategory(Long id);
}
