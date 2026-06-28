package com.tabeeby.dto;

import jakarta.validation.constraints.NotBlank;

public record MedicalNoteRequest(@NotBlank String content) {
}
