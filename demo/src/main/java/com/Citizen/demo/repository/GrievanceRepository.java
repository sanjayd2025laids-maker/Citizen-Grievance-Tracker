package com.Citizen.demo.repository;

import com.Citizen.demo.entity.Grievance;
import com.Citizen.demo.entity.GrievanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface GrievanceRepository extends JpaRepository<Grievance, Long> {
    List<Grievance> findByStatus(GrievanceStatus status);

    List<Grievance> findByStatusNotIn(Collection<GrievanceStatus> statuses);
}
