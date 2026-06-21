package com.patienthub.config;

import com.patienthub.model.*;
import com.patienthub.repository.PatientRepository;
import com.patienthub.repository.VisitHistoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedPatients(PatientRepository patientRepository, VisitHistoryRepository visitHistoryRepository) {
        return args -> {
            if (patientRepository.count() > 0) {
                return;
            }

            seed(patientRepository, visitHistoryRepository, "Ahmed Hassan", Gender.MALE, 34,
                    "+20 100 234 5678", "ACL Reconstruction", "Arthroscopic ACL reconstruction",
                    "ACL Reconstruction", LocalDate.of(2026, 5, 28), CaseStatus.FOLLOW_UP);

            seed(patientRepository, visitHistoryRepository, "Fatima El-Sayed", Gender.FEMALE, 8,
                    "+20 101 345 6789", "Pediatric Hip Dysplasia", "Pavlik harness protocol",
                    "Hip Dysplasia Treatment", LocalDate.of(2026, 5, 30), CaseStatus.ACTIVE);

            seed(patientRepository, visitHistoryRepository, "Omar Khalil", Gender.MALE, 52,
                    "+20 102 456 7890", "Knee Replacement", "Total knee replacement — post-op week 3",
                    "Total Knee Replacement", LocalDate.of(2026, 5, 15), CaseStatus.ACTIVE);

            seed(patientRepository, visitHistoryRepository, "Nour Ibrahim", Gender.FEMALE, 28,
                    "+20 103 567 8901", "Ankle Fracture", "Conservative management completed",
                    "Ankle Fracture Repair", LocalDate.of(2026, 5, 20), CaseStatus.DISCHARGED);

            seed(patientRepository, visitHistoryRepository, "Youssef Mahmoud", Gender.MALE, 45,
                    "+20 104 678 9012", "Shoulder Arthroscopy", "Rotator cuff repair completed",
                    "Shoulder Arthroscopy", LocalDate.of(2026, 6, 1), CaseStatus.FOLLOW_UP);

            seed(patientRepository, visitHistoryRepository, "Mariam Farouk", Gender.FEMALE, 12,
                    "+20 105 789 0123", "Congenital Foot Deformity", "Corrective surgery planned",
                    "Foot Deformity Correction", LocalDate.of(2026, 5, 25), CaseStatus.ACTIVE);
        };
    }

    private void seed(
            PatientRepository patientRepository,
            VisitHistoryRepository visitHistoryRepository,
            String name,
            Gender gender,
            int age,
            String mobile,
            String diagnosis,
            String surgeryDetails,
            String surgeryType,
            LocalDate lastVisit,
            CaseStatus status) {
        Patient patient = new Patient();
        patient.setMedicalRecordNumber(String.format("MRN-%06d", patientRepository.count() + 1));
        patient.setFullName(name);
        patient.setGender(gender);
        patient.setDateOfBirth(LocalDate.now().minusYears(age));
        patient.setMobileNumber(mobile);
        patient.setDiagnosis(diagnosis);
        patient.setSurgeryDetails(surgeryDetails);
        patient.setSurgeryType(surgeryType);
        patient.setLastVisitDate(lastVisit);
        patient.setCaseStatus(status);
        patient.setAddress("Heliopolis, Cairo, Egypt");
        Patient saved = patientRepository.save(patient);

        VisitHistory visit = new VisitHistory();
        visit.setPatient(saved);
        visit.setVisitDate(lastVisit);
        visit.setDiagnosis(diagnosis);
        visit.setNotes("Initial consultation and assessment");
        visit.setCreatedBy("system");
        visitHistoryRepository.save(visit);
    }
}
