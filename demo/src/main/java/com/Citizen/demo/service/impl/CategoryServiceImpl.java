package com.Citizen.demo.service.impl;

import com.Citizen.demo.dto.CreateCategoryRequest;
import com.Citizen.demo.dto.UpdateCategoryRequest;
import com.Citizen.demo.entity.Category;
import com.Citizen.demo.entity.Department;
import com.Citizen.demo.exception.BusinessRuleException;
import com.Citizen.demo.exception.ResourceNotFoundException;
import com.Citizen.demo.repository.CategoryRepository;
import com.Citizen.demo.repository.DepartmentRepository;
import com.Citizen.demo.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final DepartmentRepository departmentRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, DepartmentRepository departmentRepository) {
        this.categoryRepository = categoryRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Category createCategory(CreateCategoryRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId()));

        String name = request.getName() == null ? "" : request.getName().trim();
        if (name.isEmpty()) {
            throw new BusinessRuleException("Category name is required");
        }
        if (categoryRepository.findByNameIgnoreCase(name).isPresent()) {
            throw new BusinessRuleException("Category already exists");
        }

        Category category = new Category();
        category.setName(name);
        category.setSlaDays(request.getSlaDays());
        category.setDepartment(department);

        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(Long id, UpdateCategoryRequest request) {
        Category category = getCategoryById(id);
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId()));

        String name = request.getName() == null ? "" : request.getName().trim();
        if (name.isEmpty()) {
            throw new BusinessRuleException("Category name is required");
        }

        Category existing = categoryRepository.findByNameIgnoreCase(name).orElse(null);
        if (existing != null && !existing.getId().equals(id)) {
            throw new BusinessRuleException("Category already exists");
        }

        category.setName(name);
        category.setSlaDays(request.getSlaDays());
        category.setDepartment(department);
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        if (!category.getGrievances().isEmpty()) {
            throw new BusinessRuleException("Category cannot be deleted because it is in use");
        }
        categoryRepository.delete(category);
    }
}
