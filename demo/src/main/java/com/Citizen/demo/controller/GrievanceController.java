package com.Citizen.demo.controller;

import com.Citizen.demo.dto.CreateGrievanceRequest;
import com.Citizen.demo.dto.RatingRequest;
import com.Citizen.demo.dto.UpdateStatusRequest;
import com.Citizen.demo.entity.Grievance;
import com.Citizen.demo.service.GrievanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GrievanceController {

    private final GrievanceService grievanceService;

    public GrievanceController(GrievanceService grievanceService) {
        this.grievanceService = grievanceService;
    }

    @PostMapping("/grievances")
    public ResponseEntity<Grievance> createGrievance(@Valid @RequestBody CreateGrievanceRequest request) {
        Grievance grievance = grievanceService.createGrievance(request);
        return new ResponseEntity<>(grievance, HttpStatus.CREATED);
    }

    @GetMapping("/grievances")
    public ResponseEntity<List<Grievance>> getAllGrievances() {
        return ResponseEntity.ok(grievanceService.getAllGrievances());
    }

    @GetMapping("/grievances/{id}")
    public ResponseEntity<Grievance> getGrievanceById(@PathVariable Long id) {
        return ResponseEntity.ok(grievanceService.getGrievanceById(id));
    }

    @PutMapping("/grievances/{id}/status")
    public ResponseEntity<Grievance> updateStatus(@PathVariable Long id,
                                                 @Valid @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(grievanceService.updateStatus(id, request));
    }

    @PutMapping("/grievances/{id}/rating")
    public ResponseEntity<Grievance> rateGrievance(@PathVariable Long id,
                                                  @Valid @RequestBody RatingRequest request) {
        return ResponseEntity.ok(grievanceService.rateGrievance(id, request));
    }

    @DeleteMapping("/grievances/{id}")
    public ResponseEntity<Void> deleteGrievance(@PathVariable Long id) {
        grievanceService.deleteGrievance(id);
        return ResponseEntity.noContent().build();
    }
}
