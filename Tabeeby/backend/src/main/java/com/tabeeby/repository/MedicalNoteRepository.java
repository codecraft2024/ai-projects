package com.tabeeby.repository;

import com.tabeeby.model.MedicalNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalNoteRepository extends JpaRepository<MedicalNote, Long> {
    List<MedicalNote> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}
