package com.patienthub.dto;

import com.patienthub.model.CaseStatus;
import com.patienthub.model.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PatientRequest(
        @NotBlank @Size(max = 200) String fullName,
        @NotNull Gender gender,
        LocalDate dateOfBirth,
        Integer age,
        @NotBlank @Size(max = 32) String mobileNumber,
        @Size(max = 500) String address,
        @Size(max = 200) String emergencyContact,
        @Size(max = 500) String diagnosis,
        String medicalHistory,
        String currentMedications,
        String allergies,
        String surgeryDetails,
        @Size(max = 200) String surgeryType,
        String notes,
        CaseStatus caseStatus,
        LocalDate lastVisitDate
) {
}
