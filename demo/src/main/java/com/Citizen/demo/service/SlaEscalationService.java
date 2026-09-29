package com.Citizen.demo.service;

import com.Citizen.demo.entity.Grievance;
import com.Citizen.demo.entity.GrievanceStatus;
import com.Citizen.demo.repository.GrievanceRepository;
import com.Citizen.demo.repository.EscalationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class SlaEscalationService {

    private final GrievanceRepository grievanceRepository;
    private final EscalationRepository escalationRepository;
    private final EscalationService escalationService;

    public SlaEscalationService(GrievanceRepository grievanceRepository,
                               EscalationRepository escalationRepository,
                               EscalationService escalationService) {
        this.grievanceRepository = grievanceRepository;
        this.escalationRepository = escalationRepository;
        this.escalationService = escalationService;
    }

    @Scheduled(fixedRate = 60000)
    public void checkExpiredSla() {
        List<GrievanceStatus> activeStatuses = Arrays.asList(
                GrievanceStatus.OPEN,
                GrievanceStatus.ASSIGNED,
                GrievanceStatus.IN_PROGRESS,
                GrievanceStatus.ESCALATED
        );

        List<Grievance> grievances = grievanceRepository.findByStatusNotIn(List.of(GrievanceStatus.RESOLVED, GrievanceStatus.CLOSED));

        for (Grievance grievance : grievances) {
            if (grievance.getStatus() == GrievanceStatus.ESCALATED && escalationRepository.existsByGrievanceId(grievance.getId())) {
                continue;
            }

            if (grievance.getCategory() == null || grievance.getCategory().getSlaDays() == null) {
                continue;
            }

            LocalDateTime slaDeadline = grievance.getCreatedAt().plusDays(grievance.getCategory().getSlaDays());
            if (LocalDateTime.now().isAfter(slaDeadline) || LocalDateTime.now().isEqual(slaDeadline)) {
                if (!escalationRepository.existsByGrievanceId(grievance.getId())) {
                    escalationService.createEscalation(grievance);
                }
            }
        }
    }
}
