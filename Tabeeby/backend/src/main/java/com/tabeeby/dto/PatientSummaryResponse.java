package com.tabeeby.dto;

import com.tabeeby.model.CaseStatus;

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
