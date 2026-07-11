package com.instasimulator.api.demo;

import com.instasimulator.common.dto.AccountInfo;
import com.instasimulator.common.dto.UserProfile;
import com.instasimulator.common.util.IdGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-process demo mobile backend so the simulator is runnable without external services.
 */
@RestController
@RequestMapping("/api/v1")
public class DemoBackendController {

    private final Map<String, BigDecimal> balances = new ConcurrentHashMap<>();

    @PostMapping("/auth/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String username = body.getOrDefault("username", "demo.user");
        return Map.of(
                "accessToken", "access-" + IdGenerator.shortId(),
                "refreshToken", "refresh-" + IdGenerator.shortId(),
                "userId", "user-" + username.hashCode()
        );
    }

    @PostMapping("/auth/refresh")
    public Map<String, Object> refresh(@RequestBody Map<String, String> body) {
        return Map.of("accessToken", "access-" + IdGenerator.shortId());
    }

    @PostMapping("/auth/logout")
    public Map<String, Object> logout() {
        return Map.of("status", "logged_out");
    }

    @GetMapping("/users/me")
    public UserProfile profile(@RequestHeader(value = "Authorization", required = false) String auth) {
        requireAuth(auth);
        return UserProfile.builder()
                .userId("user-001")
                .username("demo.user")
                .email("demo.user@example.com")
                .phone("+15551234567")
                .fullName("Demo User")
                .build();
    }

    @GetMapping("/accounts")
    public List<AccountInfo> accounts(@RequestHeader(value = "Authorization", required = false) String auth) {
        requireAuth(auth);
        balances.putIfAbsent("acc-1001", new BigDecimal("1250.75"));
        return List.of(AccountInfo.builder()
                .accountId("acc-1001")
                .accountType("CHECKING")
                .currency("USD")
                .balance(balances.get("acc-1001"))
                .primary(true)
                .build());
    }

    @GetMapping("/accounts/{accountId}/balance")
    public Map<String, Object> balance(@PathVariable String accountId,
                                       @RequestHeader(value = "Authorization", required = false) String auth) {
        requireAuth(auth);
        BigDecimal balance = balances.computeIfAbsent(accountId, id -> new BigDecimal("1250.75"));
        return Map.of("accountId", accountId, "balance", balance, "currency", "USD");
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public List<Map<String, Object>> transactions(@PathVariable String accountId,
                                                  @RequestHeader(value = "Authorization", required = false) String auth) {
        requireAuth(auth);
        return List.of(
                Map.of("id", "txn-1", "amount", 25.00, "type", "DEBIT", "description", "Coffee"),
                Map.of("id", "txn-2", "amount", 500.00, "type", "CREDIT", "description", "Salary")
        );
    }

    @PostMapping("/transfers")
    public Map<String, Object> transfer(@RequestBody Map<String, Object> body,
                                        @RequestHeader(value = "Authorization", required = false) String auth) {
        requireAuth(auth);
        String from = String.valueOf(body.getOrDefault("fromAccountId", "acc-1001"));
        BigDecimal amount = new BigDecimal(String.valueOf(body.get("amount")));
        balances.compute(from, (id, current) -> {
            BigDecimal base = current == null ? new BigDecimal("1250.75") : current;
            return base.subtract(amount);
        });
        return Map.of(
                "transactionId", "xfer-" + IdGenerator.shortId(),
                "status", "COMPLETED",
                "amount", amount
        );
    }

    @PostMapping("/payments/bills")
    public Map<String, Object> bill(@RequestBody Map<String, Object> body,
                                    @RequestHeader(value = "Authorization", required = false) String auth) {
        requireAuth(auth);
        return Map.of("paymentId", "bill-" + IdGenerator.shortId(), "status", "PAID");
    }

    @PostMapping("/payments/donate")
    public Map<String, Object> donate(@RequestBody Map<String, Object> body,
                                      @RequestHeader(value = "Authorization", required = false) String auth) {
        requireAuth(auth);
        return Map.of("donationId", "don-" + IdGenerator.shortId(), "status", "RECEIVED");
    }

    @PostMapping("/payments/cashout")
    public Map<String, Object> cashout(@RequestBody Map<String, Object> body,
                                       @RequestHeader(value = "Authorization", required = false) String auth) {
        requireAuth(auth);
        return Map.of("cashOutId", "co-" + IdGenerator.shortId(), "status", "PENDING_ATM");
    }

    @PostMapping("/qr/generate")
    public Map<String, Object> qrGenerate(@RequestBody Map<String, Object> body,
                                          @RequestHeader(value = "Authorization", required = false) String auth) {
        requireAuth(auth);
        return Map.of("qrCode", "QR-" + IdGenerator.shortId(), "status", "ACTIVE");
    }

    @PostMapping("/qr/validate")
    public Map<String, Object> qrValidate(@RequestBody Map<String, Object> body,
                                          @RequestHeader(value = "Authorization", required = false) String auth) {
        requireAuth(auth);
        return Map.of("valid", true, "qrCode", body.get("qrCode"));
    }

    private void requireAuth(String auth) {
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new UnauthorizedException();
        }
    }

    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.UNAUTHORIZED)
    static class UnauthorizedException extends RuntimeException {
    }
}
