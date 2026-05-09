package com.qrvalidator.backend.model;

import jakarta.validation.constraints.NotBlank;

public record ReceiptVerificationRequest(
        @NotBlank String transactionId,
        @NotBlank String amount,
        @NotBlank String currency,
        @NotBlank String from,
        @NotBlank String to,
        @NotBlank String timestamp,
        @NotBlank String status,
        @NotBlank String signature
) {
}
