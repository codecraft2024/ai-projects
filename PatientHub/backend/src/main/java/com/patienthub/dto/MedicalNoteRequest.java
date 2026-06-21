package com.patienthub.dto;

import jakarta.validation.constraints.NotBlank;

public record MedicalNoteRequest(@NotBlank String content) {
}
