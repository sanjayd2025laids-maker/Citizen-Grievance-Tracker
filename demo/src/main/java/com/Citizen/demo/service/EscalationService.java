package com.Citizen.demo.service;

import com.Citizen.demo.entity.Escalation;
import com.Citizen.demo.entity.Grievance;

import java.util.List;

public interface EscalationService {
    Escalation createEscalation(Grievance grievance);
    List<Escalation> getAllEscalations();
    Escalation getEscalationById(Long id);
    Escalation getEscalationByGrievanceId(Long grievanceId);
}
