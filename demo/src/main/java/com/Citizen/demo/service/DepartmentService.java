package com.Citizen.demo.service;

import com.Citizen.demo.dto.CreateDepartmentRequest;
import com.Citizen.demo.dto.UpdateDepartmentRequest;
import com.Citizen.demo.entity.Department;

import java.util.List;

public interface DepartmentService {
    Department createDepartment(CreateDepartmentRequest request);
    Department getDepartmentById(Long id);
    List<Department> getAllDepartments();
    Department updateDepartment(Long id, UpdateDepartmentRequest request);
    void deleteDepartment(Long id);
}
