package com.Citizen.demo.service;

import com.Citizen.demo.dto.CreateGrievanceRequest;
import com.Citizen.demo.dto.RatingRequest;
import com.Citizen.demo.dto.UpdateStatusRequest;
import com.Citizen.demo.entity.Grievance;

import java.util.List;

public interface GrievanceService {
    Grievance createGrievance(CreateGrievanceRequest request);
    Grievance getGrievanceById(Long id);
    List<Grievance> getAllGrievances();
    Grievance updateStatus(Long grievanceId, UpdateStatusRequest request);
    Grievance rateGrievance(Long grievanceId, RatingRequest request);
    void deleteGrievance(Long id);
}
