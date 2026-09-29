package com.Citizen.demo.service.impl;

import com.Citizen.demo.dto.CreateDepartmentRequest;
import com.Citizen.demo.dto.UpdateDepartmentRequest;
import com.Citizen.demo.entity.Department;
import com.Citizen.demo.exception.BusinessRuleException;
import com.Citizen.demo.exception.ResourceNotFoundException;
import com.Citizen.demo.repository.DepartmentRepository;
import com.Citizen.demo.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department createDepartment(CreateDepartmentRequest request) {
        String name = request.getName() == null ? "" : request.getName().trim();
        if (name.isEmpty()) {
            throw new BusinessRuleException("Department name is required");
        }
        if (departmentRepository.findByNameIgnoreCase(name).isPresent()) {
            throw new BusinessRuleException("Department already exists");
        }

        Department department = new Department();
        department.setName(name);
        return departmentRepository.save(department);
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department updateDepartment(Long id, UpdateDepartmentRequest request) {
        Department department = getDepartmentById(id);
        String name = request.getName() == null ? "" : request.getName().trim();
        if (name.isEmpty()) {
            throw new BusinessRuleException("Department name is required");
        }

        Department existing = departmentRepository.findByNameIgnoreCase(name).orElse(null);
        if (existing != null && !existing.getId().equals(id)) {
            throw new BusinessRuleException("Department already exists");
        }

        department.setName(name);
        return departmentRepository.save(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = getDepartmentById(id);
        if (!department.getCategories().isEmpty() || !department.getGrievances().isEmpty()) {
            throw new BusinessRuleException("Department cannot be deleted because it is in use");
        }
        departmentRepository.delete(department);
    }
}
