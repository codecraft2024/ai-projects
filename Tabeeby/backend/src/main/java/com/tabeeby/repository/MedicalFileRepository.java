package com.tabeeby.repository;

import com.tabeeby.model.MedicalFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalFileRepository extends JpaRepository<MedicalFile, Long> {
    List<MedicalFile> findByPatientIdOrderByUploadedAtDesc(Long patientId);
}
