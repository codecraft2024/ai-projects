package com.patienthub.dto;

import com.patienthub.model.CaseStatus;

import java.time.LocalDate;

public record PatientSummaryResponse(
        Long id,
        String medicalRecordNumber,
        String fullName,
        Integer age,
        String mobileNumber,
        String diagnosis,
        String surgeryType,
        LocalDate lastVisitDate,
        CaseStatus caseStatus
) {
}
