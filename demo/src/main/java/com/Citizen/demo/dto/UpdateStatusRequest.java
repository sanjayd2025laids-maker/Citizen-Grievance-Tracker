package com.Citizen.demo.dto;

import com.Citizen.demo.entity.GrievanceStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusRequest {

    @NotNull(message = "Status is required")
    private GrievanceStatus status;

    public GrievanceStatus getStatus() {
        return status;
    }

    public void setStatus(GrievanceStatus status) {
        this.status = status;
    }
}
