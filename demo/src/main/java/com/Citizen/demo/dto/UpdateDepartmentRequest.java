package com.Citizen.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateDepartmentRequest {

    @NotBlank(message = "Department name is required")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
