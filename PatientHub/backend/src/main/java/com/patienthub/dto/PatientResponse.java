package com.patienthub.dto;

import com.patienthub.model.CaseStatus;
import com.patienthub.model.Gender;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record PatientResponse(
        Long id,
        String medicalRecordNumber,
        String fullName,
        Gender gender,
        LocalDate dateOfBirth,
        Integer age,
        String mobileNumber,
        String address,
        String emergencyContact,
        String diagnosis,
        String medicalHistory,
        String currentMedications,
        String allergies,
        String surgeryDetails,
        String surgeryType,
        String notes,
        CaseStatus caseStatus,
        LocalDate lastVisitDate,
        Instant createdAt,
        Instant updatedAt,
        List<MedicalFileResponse> files,
        List<VisitHistoryResponse> visits,
        List<MedicalNoteResponse> medicalNotes
) {
}
