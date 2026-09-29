package com.Citizen.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateCategoryRequest {

    @NotBlank(message = "Category name is required")
    private String name;

    @NotNull(message = "SLA days is required")
    @Min(value = 1, message = "SLA days must be at least 1")
    private Integer slaDays;

    @NotNull(message = "Department id is required")
    private Long departmentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSlaDays() {
        return slaDays;
    }

    public void setSlaDays(Integer slaDays) {
        this.slaDays = slaDays;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
