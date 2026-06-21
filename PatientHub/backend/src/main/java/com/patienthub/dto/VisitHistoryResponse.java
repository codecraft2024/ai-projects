package com.patienthub.dto;

import java.time.Instant;
import java.time.LocalDate;

public record VisitHistoryResponse(
        Long id,
        LocalDate visitDate,
        String notes,
        String diagnosis,
        String createdBy,
        Instant createdAt
) {
}
