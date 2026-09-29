package com.Citizen.demo.service.impl;

import com.Citizen.demo.entity.Escalation;
import com.Citizen.demo.entity.Grievance;
import com.Citizen.demo.entity.GrievanceStatus;
import com.Citizen.demo.exception.BusinessRuleException;
import com.Citizen.demo.exception.ResourceNotFoundException;
import com.Citizen.demo.repository.EscalationRepository;
import com.Citizen.demo.repository.GrievanceRepository;
import com.Citizen.demo.service.EscalationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EscalationServiceImpl implements EscalationService {

    private final EscalationRepository escalationRepository;
    private final GrievanceRepository grievanceRepository;

    public EscalationServiceImpl(EscalationRepository escalationRepository, GrievanceRepository grievanceRepository) {
        this.escalationRepository = escalationRepository;
        this.grievanceRepository = grievanceRepository;
    }

    @Override
    public Escalation createEscalation(Grievance grievance) {
        if (grievance.getStatus() == GrievanceStatus.RESOLVED || grievance.getStatus() == GrievanceStatus.CLOSED) {
            throw new BusinessRuleException("A resolved or closed grievance cannot be escalated");
        }

        if (escalationRepository.existsByGrievanceId(grievance.getId())) {
            throw new BusinessRuleException("This grievance has already been escalated");
        }

        Escalation escalation = new Escalation();
        escalation.setGrievance(grievance);
        escalation.setEscalatedTo("Senior Officer");
        escalation.setEscalatedAt(LocalDateTime.now());
        escalation.setReason("SLA exceeded for category: " + grievance.getCategory().getName());

        grievance.setStatus(GrievanceStatus.ESCALATED);
        grievance.setUpdatedAt(LocalDateTime.now());
        grievanceRepository.save(grievance);

        return escalationRepository.save(escalation);
    }

    @Override
    public List<Escalation> getAllEscalations() {
        return escalationRepository.findAll();
    }

    @Override
    public Escalation getEscalationById(Long id) {
        return escalationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Escalation not found with id: " + id));
    }

    @Override
    public Escalation getEscalationByGrievanceId(Long grievanceId) {
        Grievance grievance = grievanceRepository.findById(grievanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Grievance not found with id: " + grievanceId));

        return escalationRepository.findByGrievance(grievance)
                .orElseThrow(() -> new ResourceNotFoundException("No escalation found for grievance id: " + grievanceId));
    }
}
