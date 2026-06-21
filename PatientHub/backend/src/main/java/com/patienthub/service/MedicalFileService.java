package com.patienthub.service;

import com.patienthub.config.StorageProperties;
import com.patienthub.dto.MedicalFileResponse;
import com.patienthub.exception.ResourceNotFoundException;
import com.patienthub.model.MedicalFile;
import com.patienthub.model.MedicalFileType;
import com.patienthub.model.Patient;
import com.patienthub.repository.MedicalFileRepository;
import com.patienthub.repository.PatientRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class MedicalFileService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp",
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    private static final long MAX_FILE_SIZE = 25L * 1024 * 1024;

    private final MedicalFileRepository medicalFileRepository;
    private final PatientRepository patientRepository;
    private final Path uploadRoot;

    public MedicalFileService(
            MedicalFileRepository medicalFileRepository,
            PatientRepository patientRepository,
            StorageProperties storageProperties) throws IOException {
        this.medicalFileRepository = medicalFileRepository;
        this.patientRepository = patientRepository;
        this.uploadRoot = Paths.get(storageProperties.uploadDir()).toAbsolutePath().normalize();
        Files.createDirectories(uploadRoot);
    }

    @Transactional(readOnly = true)
    public List<MedicalFileResponse> listFiles(Long patientId) {
        findPatient(patientId);
        return medicalFileRepository.findByPatientIdOrderByUploadedAtDesc(patientId).stream()
                .map(f -> toResponse(patientId, f))
                .toList();
    }

    public MedicalFileResponse upload(Long patientId, MedicalFileType fileType, MultipartFile file, String uploadedBy) {
        Patient patient = findPatient(patientId);
        validateFile(file);

        String storedName = UUID.randomUUID() + sanitizeExtension(file.getOriginalFilename());
        Path target = uploadRoot.resolve(storedName);

        try {
            file.transferTo(target);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to store file: " + ex.getMessage());
        }

        MedicalFile entity = new MedicalFile();
        entity.setPatient(patient);
        entity.setFileType(fileType);
        entity.setOriginalFilename(file.getOriginalFilename());
        entity.setStoredFilename(storedName);
        entity.setContentType(file.getContentType());
        entity.setFileSize(file.getSize());
        entity.setUploadedBy(uploadedBy != null ? uploadedBy : "admin");

        return toResponse(patientId, medicalFileRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public Resource loadFile(Long patientId, Long fileId) {
        MedicalFile file = findFile(patientId, fileId);
        try {
            Path path = uploadRoot.resolve(file.getStoredFilename());
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("File not found on disk");
            }
            return resource;
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("File not found");
        }
    }

    @Transactional(readOnly = true)
    public MedicalFile getFileMeta(Long patientId, Long fileId) {
        return findFile(patientId, fileId);
    }

    public void delete(Long patientId, Long fileId) {
        MedicalFile file = findFile(patientId, fileId);
        try {
            Files.deleteIfExists(uploadRoot.resolve(file.getStoredFilename()));
        } catch (IOException ignored) {
            // continue with DB delete
        }
        medicalFileRepository.delete(file);
    }

    private Patient findPatient(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id));
    }

    private MedicalFile findFile(Long patientId, Long fileId) {
        MedicalFile file = medicalFileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found: " + fileId));
        if (!file.getPatient().getId().equals(patientId)) {
            throw new ResourceNotFoundException("File not found for patient");
        }
        return file;
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File exceeds maximum size of 25 MB");
        }
        String contentType = file.getContentType();
        if (contentType != null && !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Unsupported file type: " + contentType);
        }
    }

    private String sanitizeExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return "";
        }
        String ext = originalFilename.substring(originalFilename.lastIndexOf('.'));
        return ext.length() > 10 ? "" : ext.toLowerCase();
    }

    private MedicalFileResponse toResponse(Long patientId, MedicalFile file) {
        return new MedicalFileResponse(
                file.getId(),
                file.getFileType(),
                file.getOriginalFilename(),
                file.getContentType(),
                file.getFileSize(),
                file.getUploadedAt(),
                file.getUploadedBy(),
                "/api/patients/" + patientId + "/files/" + file.getId());
    }
}
