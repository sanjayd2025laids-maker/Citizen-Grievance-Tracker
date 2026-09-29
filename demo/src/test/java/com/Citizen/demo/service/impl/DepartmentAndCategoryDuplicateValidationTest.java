package com.Citizen.demo.service.impl;

import com.Citizen.demo.dto.CreateCategoryRequest;
import com.Citizen.demo.dto.CreateDepartmentRequest;
import com.Citizen.demo.entity.Department;
import com.Citizen.demo.exception.BusinessRuleException;
import com.Citizen.demo.repository.CategoryRepository;
import com.Citizen.demo.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DepartmentAndCategoryDuplicateValidationTest {

    @Test
    void createDepartment_shouldRejectDuplicateNameIgnoringCase() {
        DepartmentRepository departmentRepository = mock(DepartmentRepository.class);
        DepartmentServiceImpl service = new DepartmentServiceImpl(departmentRepository);

        when(departmentRepository.findByNameIgnoreCase("road")).thenReturn(Optional.of(new Department()));

        CreateDepartmentRequest request = new CreateDepartmentRequest();
        request.setName("road");

        assertThatThrownBy(() -> service.createDepartment(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Department already exists");
    }

    @Test
    void createCategory_shouldRejectDuplicateNameIgnoringCase() {
        DepartmentRepository departmentRepository = mock(DepartmentRepository.class);
        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        CategoryServiceImpl service = new CategoryServiceImpl(categoryRepository, departmentRepository);

        Department department = new Department();
        department.setId(10L);
        department.setName("Public Works Department");

        when(departmentRepository.findById(10L)).thenReturn(Optional.of(department));
        when(categoryRepository.findByNameIgnoreCase("road")).thenReturn(Optional.of(new com.Citizen.demo.entity.Category()));

        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setDepartmentId(10L);
        request.setName("road");
        request.setSlaDays(3);

        assertThatThrownBy(() -> service.createCategory(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Category already exists");
    }
}
