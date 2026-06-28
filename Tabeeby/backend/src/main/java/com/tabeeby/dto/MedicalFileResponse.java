package com.tabeeby.dto;

import com.tabeeby.model.MedicalFileType;

import java.time.Instant;

public record MedicalFileResponse(
        Long id,
        MedicalFileType fileType,
        String originalFilename,
        String contentType,
        long fileSize,
        Instant uploadedAt,
        String uploadedBy,
        String downloadUrl
) {
}
