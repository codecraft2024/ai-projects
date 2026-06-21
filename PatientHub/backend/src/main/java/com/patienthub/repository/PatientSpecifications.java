package com.patienthub.repository;

import com.patienthub.model.CaseStatus;
import com.patienthub.model.Patient;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public final class PatientSpecifications {

    private PatientSpecifications() {
    }

    public static Specification<Patient> withFilters(
            String name,
            String mobile,
            Integer age,
            CaseStatus caseStatus,
            String diagnosis,
            String surgeryType,
            LocalDate lastVisitFrom,
            LocalDate lastVisitTo,
            String medicalRecordNumber
    ) {
        return Specification.allOf(
                nameLike(name),
                mobileLike(mobile),
                ageEquals(age),
                statusEquals(caseStatus),
                diagnosisLike(diagnosis),
                surgeryTypeLike(surgeryType),
                lastVisitFrom(lastVisitFrom),
                lastVisitTo(lastVisitTo),
                mrnLike(medicalRecordNumber)
        );
    }

    private static Specification<Patient> nameLike(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("fullName")), "%" + name.trim().toLowerCase() + "%");
        };
    }

    private static Specification<Patient> mobileLike(String mobile) {
        return (root, query, cb) -> {
            if (mobile == null || mobile.isBlank()) return cb.conjunction();
            return cb.like(root.get("mobileNumber"), "%" + mobile.trim() + "%");
        };
    }

    private static Specification<Patient> ageEquals(Integer age) {
        return (root, query, cb) -> {
            if (age == null) return cb.conjunction();
            LocalDate from = LocalDate.now().minusYears(age + 1).plusDays(1);
            LocalDate to = LocalDate.now().minusYears(age);
            return cb.between(root.get("dateOfBirth"), from, to);
        };
    }

    private static Specification<Patient> statusEquals(CaseStatus status) {
        return (root, query, cb) ->
                status == null ? cb.conjunction() : cb.equal(root.get("caseStatus"), status);
    }

    private static Specification<Patient> diagnosisLike(String diagnosis) {
        return (root, query, cb) -> {
            if (diagnosis == null || diagnosis.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("diagnosis")), "%" + diagnosis.trim().toLowerCase() + "%");
        };
    }

    private static Specification<Patient> surgeryTypeLike(String surgeryType) {
        return (root, query, cb) -> {
            if (surgeryType == null || surgeryType.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("surgeryType")), "%" + surgeryType.trim().toLowerCase() + "%");
        };
    }

    private static Specification<Patient> lastVisitFrom(LocalDate from) {
        return (root, query, cb) ->
                from == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("lastVisitDate"), from);
    }

    private static Specification<Patient> lastVisitTo(LocalDate to) {
        return (root, query, cb) ->
                to == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("lastVisitDate"), to);
    }

    private static Specification<Patient> mrnLike(String mrn) {
        return (root, query, cb) -> {
            if (mrn == null || mrn.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("medicalRecordNumber")), "%" + mrn.trim().toLowerCase() + "%");
        };
    }
}
