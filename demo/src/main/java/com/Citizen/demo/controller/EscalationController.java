package com.Citizen.demo.controller;

import com.Citizen.demo.entity.Escalation;
import com.Citizen.demo.service.EscalationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EscalationController {

    private final EscalationService escalationService;

    public EscalationController(EscalationService escalationService) {
        this.escalationService = escalationService;
    }

    @GetMapping("/escalations")
    public ResponseEntity<List<Escalation>> getAllEscalations() {
        return ResponseEntity.ok(escalationService.getAllEscalations());
    }

    @GetMapping("/escalations/{id}")
    public ResponseEntity<Escalation> getEscalationById(@PathVariable Long id) {
        return ResponseEntity.ok(escalationService.getEscalationById(id));
    }

    @GetMapping("/grievances/{grievanceId}/escalation")
    public ResponseEntity<Escalation> getEscalationByGrievanceId(@PathVariable Long grievanceId) {
        return ResponseEntity.ok(escalationService.getEscalationByGrievanceId(grievanceId));
    }
}
