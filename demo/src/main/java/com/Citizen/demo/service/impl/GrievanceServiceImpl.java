package com.Citizen.demo.service.impl;

import com.Citizen.demo.dto.CreateGrievanceRequest;
import com.Citizen.demo.dto.RatingRequest;
import com.Citizen.demo.dto.UpdateStatusRequest;
import com.Citizen.demo.entity.Category;
import com.Citizen.demo.entity.Department;
import com.Citizen.demo.entity.Grievance;
import com.Citizen.demo.entity.GrievanceStatus;
import com.Citizen.demo.exception.BusinessRuleException;
import com.Citizen.demo.exception.ResourceNotFoundException;
import com.Citizen.demo.repository.CategoryRepository;
import com.Citizen.demo.repository.GrievanceRepository;
import com.Citizen.demo.service.GrievanceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class GrievanceServiceImpl implements GrievanceService {

    private final GrievanceRepository grievanceRepository;
    private final CategoryRepository categoryRepository;

    public GrievanceServiceImpl(GrievanceRepository grievanceRepository, CategoryRepository categoryRepository) {
        this.grievanceRepository = grievanceRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Grievance createGrievance(CreateGrievanceRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Department department = category.getDepartment();
        if (department == null) {
            throw new BusinessRuleException("Category is not assigned to any department");
        }

        Grievance grievance = new Grievance();
        grievance.setDescription(request.getDescription().trim());
        grievance.setLocation(request.getLocation().trim());
        grievance.setCategory(category);
        grievance.setDepartment(department);
        grievance.setStatus(GrievanceStatus.OPEN);

        LocalDateTime now = LocalDateTime.now();
        grievance.setCreatedAt(now);
        grievance.setUpdatedAt(now);

        return grievanceRepository.save(grievance);
    }

    @Override
    public Grievance getGrievanceById(Long id) {
        return grievanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grievance not found with id: " + id));
    }

    @Override
    public List<Grievance> getAllGrievances() {
        return grievanceRepository.findAll();
    }

    @Override
    public Grievance updateStatus(Long grievanceId, UpdateStatusRequest request) {
        Grievance grievance = getGrievanceById(grievanceId);
        GrievanceStatus newStatus = request.getStatus();

        if (grievance.getStatus() == GrievanceStatus.CLOSED) {
            throw new BusinessRuleException("A closed grievance cannot be changed back to another status");
        }

        validateStatusTransition(grievance.getStatus(), newStatus);

        grievance.setStatus(newStatus);
        grievance.setUpdatedAt(LocalDateTime.now());

        return grievanceRepository.save(grievance);
    }

    @Override
    public Grievance rateGrievance(Long grievanceId, RatingRequest request) {
        Grievance grievance = getGrievanceById(grievanceId);

        if (grievance.getStatus() != GrievanceStatus.CLOSED) {
            throw new BusinessRuleException("Rating can be submitted only after the grievance is closed");
        }

        if (request.getRating() == null || request.getRating() < 1 || request.getRating() > 5) {
            throw new BusinessRuleException("Rating must be between 1 and 5");
        }

        grievance.setRating(request.getRating());
        grievance.setRatingComment(request.getRatingComment());
        grievance.setUpdatedAt(LocalDateTime.now());
        return grievanceRepository.save(grievance);
    }

    @Override
    public void deleteGrievance(Long id) {
        Grievance grievance = getGrievanceById(id);
        grievanceRepository.delete(grievance);
    }

    public void validateStatusTransition(GrievanceStatus currentStatus, GrievanceStatus newStatus) {
        if (currentStatus == newStatus) {
            return;
        }

        if (newStatus == GrievanceStatus.ESCALATED) {
            if (currentStatus == GrievanceStatus.RESOLVED || currentStatus == GrievanceStatus.CLOSED) {
                throw new BusinessRuleException("A resolved or closed grievance cannot be escalated again");
            }
            return;
        }

        List<GrievanceStatus> validTransitions = switch (currentStatus) {
            case OPEN -> Arrays.asList(GrievanceStatus.ASSIGNED, GrievanceStatus.ESCALATED);
            case ASSIGNED -> Arrays.asList(GrievanceStatus.IN_PROGRESS, GrievanceStatus.ESCALATED);
            case IN_PROGRESS -> Arrays.asList(GrievanceStatus.RESOLVED, GrievanceStatus.ESCALATED);
            case RESOLVED -> Arrays.asList(GrievanceStatus.CLOSED);
            case ESCALATED -> Arrays.asList(GrievanceStatus.RESOLVED, GrievanceStatus.CLOSED);
            case CLOSED -> List.of();
        };

        if (!validTransitions.contains(newStatus)) {
            throw new BusinessRuleException("Invalid status transition from " + currentStatus + " to " + newStatus);
        }
    }
}
