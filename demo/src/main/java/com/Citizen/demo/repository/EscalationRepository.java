package com.Citizen.demo.repository;

import com.Citizen.demo.entity.Escalation;
import com.Citizen.demo.entity.Grievance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EscalationRepository extends JpaRepository<Escalation, Long> {
    Optional<Escalation> findByGrievance(Grievance grievance);
    boolean existsByGrievanceId(Long grievanceId);
}
