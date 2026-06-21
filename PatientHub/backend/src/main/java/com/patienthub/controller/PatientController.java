package com.patienthub.controller;

import com.patienthub.dto.*;
import com.patienthub.model.CaseStatus;
import com.patienthub.model.MedicalFile;
import com.patienthub.model.MedicalFileType;
import com.patienthub.service.MedicalFileService;
import com.patienthub.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;
    private final MedicalFileService medicalFileService;

    public PatientController(PatientService patientService, MedicalFileService medicalFileService) {
        this.patientService = patientService;
        this.medicalFileService = medicalFileService;
    }

    @GetMapping
    public PageResponse<PatientSummaryResponse> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String mobile,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) CaseStatus caseStatus,
            @RequestParam(required = false) String diagnosis,
            @RequestParam(required = false) String surgeryType,
            @RequestParam(required = false) LocalDate lastVisitFrom,
            @RequestParam(required = false) LocalDate lastVisitTo,
            @RequestParam(required = false) String medicalRecordNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return patientService.search(
                name, mobile, age, caseStatus, diagnosis, surgeryType,
                lastVisitFrom, lastVisitTo, medicalRecordNumber, page, size);
    }

    @GetMapping("/{id}")
    public PatientResponse getById(@PathVariable Long id) {
        return patientService.getById(id);
    }

    @PostMapping
    public ResponseEntity<PatientResponse> create(@Valid @RequestBody PatientRequest request) {
        return ResponseEntity.status(201).body(patientService.create(request));
    }

    @PutMapping("/{id}")
    public PatientResponse update(@PathVariable Long id, @Valid @RequestBody PatientRequest request) {
        return patientService.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public PatientResponse updateStatus(@PathVariable Long id, @RequestBody Map<String, CaseStatus> body) {
        CaseStatus status = body.get("caseStatus");
        if (status == null) {
            throw new IllegalArgumentException("caseStatus is required");
        }
        return patientService.updateCaseStatus(id, status);
    }

    @PostMapping("/{id}/notes")
    public MedicalNoteResponse addNote(
            @PathVariable Long id,
            @Valid @RequestBody MedicalNoteRequest request,
            @RequestAttribute("adminUser") String adminUser) {
        return patientService.addNote(id, request, adminUser);
    }

    @PostMapping("/{id}/visits")
    public VisitHistoryResponse addVisit(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @RequestAttribute("adminUser") String adminUser) {
        LocalDate visitDate = body.get("visitDate") != null ? LocalDate.parse(body.get("visitDate")) : null;
        return patientService.addVisit(id, visitDate, body.get("notes"), body.get("diagnosis"), adminUser);
    }

    @GetMapping("/{id}/files")
    public List<MedicalFileResponse> listFiles(@PathVariable Long id) {
        return medicalFileService.listFiles(id);
    }

    @PostMapping(value = "/{id}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MedicalFileResponse uploadFile(
            @PathVariable Long id,
            @RequestParam MedicalFileType fileType,
            @RequestParam("file") MultipartFile file,
            @RequestAttribute("adminUser") String adminUser) {
        return medicalFileService.upload(id, fileType, file, adminUser);
    }

    @GetMapping("/{id}/files/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id, @PathVariable Long fileId) {
        MedicalFile meta = medicalFileService.getFileMeta(id, fileId);
        Resource resource = medicalFileService.loadFile(id, fileId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + meta.getOriginalFilename() + "\"")
                .contentType(meta.getContentType() != null
                        ? MediaType.parseMediaType(meta.getContentType())
                        : MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @DeleteMapping("/{id}/files/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id, @PathVariable Long fileId) {
        medicalFileService.delete(id, fileId);
        return ResponseEntity.noContent().build();
    }
}
