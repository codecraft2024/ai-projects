package com.patienthub.service;

import com.patienthub.dto.*;
import com.patienthub.exception.ResourceNotFoundException;
import com.patienthub.model.*;
import com.patienthub.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;
    private final MedicalFileRepository medicalFileRepository;
    private final VisitHistoryRepository visitHistoryRepository;
    private final MedicalNoteRepository medicalNoteRepository;

    public PatientService(
            PatientRepository patientRepository,
            MedicalFileRepository medicalFileRepository,
            VisitHistoryRepository visitHistoryRepository,
            MedicalNoteRepository medicalNoteRepository) {
        this.patientRepository = patientRepository;
        this.medicalFileRepository = medicalFileRepository;
        this.visitHistoryRepository = visitHistoryRepository;
        this.medicalNoteRepository = medicalNoteRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<PatientSummaryResponse> search(
            String name,
            String mobile,
            Integer age,
            CaseStatus caseStatus,
            String diagnosis,
            String surgeryType,
            LocalDate lastVisitFrom,
            LocalDate lastVisitTo,
            String medicalRecordNumber,
            int page,
            int size) {
        Specification<Patient> spec = PatientSpecifications.withFilters(
                name, mobile, age, caseStatus, diagnosis, surgeryType,
                lastVisitFrom, lastVisitTo, medicalRecordNumber);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<Patient> result = patientRepository.findAll(spec, pageable);

        List<PatientSummaryResponse> content = result.getContent().stream()
                .map(this::toSummary)
                .toList();

        return new PageResponse<>(
                content,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isLast());
    }

    @Transactional(readOnly = true)
    public PatientResponse getById(Long id) {
        Patient patient = findPatient(id);
        return toDetail(patient);
    }

    public PatientResponse create(PatientRequest request) {
        Patient patient = new Patient();
        applyRequest(patient, request);
        patient.setMedicalRecordNumber(generateMrn());
        if (patient.getLastVisitDate() == null) {
            patient.setLastVisitDate(LocalDate.now());
        }
        Patient saved = patientRepository.save(patient);

        VisitHistory initialVisit = new VisitHistory();
        initialVisit.setPatient(saved);
        initialVisit.setVisitDate(saved.getLastVisitDate());
        initialVisit.setDiagnosis(saved.getDiagnosis());
        initialVisit.setNotes("Initial patient registration");
        initialVisit.setCreatedBy("system");
        visitHistoryRepository.save(initialVisit);

        return toDetail(saved);
    }

    public PatientResponse update(Long id, PatientRequest request) {
        Patient patient = findPatient(id);
        applyRequest(patient, request);
        return toDetail(patientRepository.save(patient));
    }

    public PatientResponse updateCaseStatus(Long id, CaseStatus status) {
        Patient patient = findPatient(id);
        patient.setCaseStatus(status);
        return toDetail(patientRepository.save(patient));
    }

    public MedicalNoteResponse addNote(Long patientId, MedicalNoteRequest request, String createdBy) {
        Patient patient = findPatient(patientId);
        MedicalNote note = new MedicalNote();
        note.setPatient(patient);
        note.setContent(request.content());
        note.setCreatedBy(createdBy != null ? createdBy : "admin");
        MedicalNote saved = medicalNoteRepository.save(note);
        return toNoteResponse(saved);
    }

    public VisitHistoryResponse addVisit(Long patientId, LocalDate visitDate, String notes, String diagnosis, String createdBy) {
        Patient patient = findPatient(patientId);
        VisitHistory visit = new VisitHistory();
        visit.setPatient(patient);
        visit.setVisitDate(visitDate != null ? visitDate : LocalDate.now());
        visit.setNotes(notes);
        visit.setDiagnosis(diagnosis);
        visit.setCreatedBy(createdBy != null ? createdBy : "admin");
        VisitHistory saved = visitHistoryRepository.save(visit);

        if (visitDate != null && (patient.getLastVisitDate() == null || !visitDate.isBefore(patient.getLastVisitDate()))) {
            patient.setLastVisitDate(visitDate);
        }
        if (diagnosis != null && !diagnosis.isBlank()) {
            patient.setDiagnosis(diagnosis);
        }
        patientRepository.save(patient);

        return toVisitResponse(saved);
    }

    private Patient findPatient(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id));
    }

    private void applyRequest(Patient patient, PatientRequest request) {
        patient.setFullName(request.fullName().trim());
        patient.setGender(request.gender());
        patient.setMobileNumber(request.mobileNumber().trim());
        patient.setAddress(trimToNull(request.address()));
        patient.setEmergencyContact(trimToNull(request.emergencyContact()));
        patient.setDiagnosis(trimToNull(request.diagnosis()));
        patient.setMedicalHistory(trimToNull(request.medicalHistory()));
        patient.setCurrentMedications(trimToNull(request.currentMedications()));
        patient.setAllergies(trimToNull(request.allergies()));
        patient.setSurgeryDetails(trimToNull(request.surgeryDetails()));
        patient.setSurgeryType(trimToNull(request.surgeryType()));
        patient.setNotes(trimToNull(request.notes()));

        if (request.caseStatus() != null) {
            patient.setCaseStatus(request.caseStatus());
        } else if (patient.getCaseStatus() == null) {
            patient.setCaseStatus(CaseStatus.ACTIVE);
        }

        if (request.lastVisitDate() != null) {
            patient.setLastVisitDate(request.lastVisitDate());
        }

        if (request.dateOfBirth() != null) {
            patient.setDateOfBirth(request.dateOfBirth());
        } else if (request.age() != null) {
            patient.setDateOfBirth(LocalDate.now().minusYears(request.age()));
        }
    }

    private String generateMrn() {
        return String.format("MRN-%06d", patientRepository.count() + 1);
    }

    private String trimToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    static Integer calculateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) return null;
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    private PatientSummaryResponse toSummary(Patient patient) {
        return new PatientSummaryResponse(
                patient.getId(),
                patient.getMedicalRecordNumber(),
                patient.getFullName(),
                calculateAge(patient.getDateOfBirth()),
                patient.getMobileNumber(),
                patient.getDiagnosis(),
                patient.getSurgeryType(),
                patient.getLastVisitDate(),
                patient.getCaseStatus());
    }

    private PatientResponse toDetail(Patient patient) {
        List<MedicalFileResponse> files = medicalFileRepository
                .findByPatientIdOrderByUploadedAtDesc(patient.getId()).stream()
                .map(f -> new MedicalFileResponse(
                        f.getId(),
                        f.getFileType(),
                        f.getOriginalFilename(),
                        f.getContentType(),
                        f.getFileSize(),
                        f.getUploadedAt(),
                        f.getUploadedBy(),
                        "/api/patients/" + patient.getId() + "/files/" + f.getId()))
                .toList();

        List<VisitHistoryResponse> visits = visitHistoryRepository
                .findByPatientIdOrderByVisitDateDescCreatedAtDesc(patient.getId()).stream()
                .map(this::toVisitResponse)
                .toList();

        List<MedicalNoteResponse> notes = medicalNoteRepository
                .findByPatientIdOrderByCreatedAtDesc(patient.getId()).stream()
                .map(this::toNoteResponse)
                .toList();

        return new PatientResponse(
                patient.getId(),
                patient.getMedicalRecordNumber(),
                patient.getFullName(),
                patient.getGender(),
                patient.getDateOfBirth(),
                calculateAge(patient.getDateOfBirth()),
                patient.getMobileNumber(),
                patient.getAddress(),
                patient.getEmergencyContact(),
                patient.getDiagnosis(),
                patient.getMedicalHistory(),
                patient.getCurrentMedications(),
                patient.getAllergies(),
                patient.getSurgeryDetails(),
                patient.getSurgeryType(),
                patient.getNotes(),
                patient.getCaseStatus(),
                patient.getLastVisitDate(),
                patient.getCreatedAt(),
                patient.getUpdatedAt(),
                files,
                visits,
                notes);
    }

    private VisitHistoryResponse toVisitResponse(VisitHistory visit) {
        return new VisitHistoryResponse(
                visit.getId(),
                visit.getVisitDate(),
                visit.getNotes(),
                visit.getDiagnosis(),
                visit.getCreatedBy(),
                visit.getCreatedAt());
    }

    private MedicalNoteResponse toNoteResponse(MedicalNote note) {
        return new MedicalNoteResponse(
                note.getId(),
                note.getContent(),
                note.getCreatedBy(),
                note.getCreatedAt());
    }
}
