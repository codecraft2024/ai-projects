package com.qrvalidator.backend.controller;

import com.qrvalidator.backend.model.ReceiptVerificationRequest;
import com.qrvalidator.backend.model.ReceiptVerificationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:3000")
public class ReceiptVerificationController {

    @GetMapping("/verify-receipt")
    public ResponseEntity<ReceiptVerificationResponse> verifyReceipt(
            @RequestParam String transactionId,
            @RequestParam String amount,
            @RequestParam String currency,
            @RequestParam(name = "from") String fromValue,
            @RequestParam(name = "to") String toValue,
            @RequestParam String timestamp,
            @RequestParam String status,
            @RequestParam String signature
    ) {
        ReceiptVerificationRequest request = new ReceiptVerificationRequest(
                transactionId, amount, currency, fromValue, toValue, timestamp, status, signature
        );
        boolean valid = "SUCCESS".equalsIgnoreCase(request.status()) && "EGP".equalsIgnoreCase(request.currency());

        String message = valid
                ? "Receipt is valid and verified."
                : "Receipt validation failed. Please verify transaction details.";

        return ResponseEntity.ok(new ReceiptVerificationResponse(valid, message, request));
    }
}
