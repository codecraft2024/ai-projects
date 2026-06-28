package com.tabeeby.dto;

import java.time.Instant;

public record MedicalNoteResponse(
        Long id,
        String content,
        String createdBy,
        Instant createdAt
) {
}
