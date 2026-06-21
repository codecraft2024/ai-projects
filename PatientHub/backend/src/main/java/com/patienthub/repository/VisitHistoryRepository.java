package com.patienthub.repository;

import com.patienthub.model.VisitHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitHistoryRepository extends JpaRepository<VisitHistory, Long> {
    List<VisitHistory> findByPatientIdOrderByVisitDateDescCreatedAtDesc(Long patientId);
}
