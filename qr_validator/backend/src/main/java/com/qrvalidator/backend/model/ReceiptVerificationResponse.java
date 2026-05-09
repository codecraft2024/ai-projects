package com.qrvalidator.backend.model;

public record ReceiptVerificationResponse(
        boolean valid,
        String message,
        ReceiptVerificationRequest payload
) {
}
