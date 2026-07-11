package com.instasimulator.common.context;

import com.instasimulator.common.dto.AccountInfo;
import com.instasimulator.common.dto.UserProfile;
import com.instasimulator.common.util.IdGenerator;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mutable per-execution context shared across calls in a scenario.
 * Stores tokens, accounts, correlation data, and arbitrary attributes.
 */
@Data
@Builder
public class SimulationContext {

    private String userId;
    private String username;
    private String password;
    private String accessToken;
    private String refreshToken;
    private String sessionId;
    private String correlationId;
    private String currentScenario;
    private UserProfile profile;

    @Builder.Default
    private List<AccountInfo> accounts = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> attributes = new ConcurrentHashMap<>();

    public static SimulationContext create(String userId, String username, String password) {
        return SimulationContext.builder()
                .userId(userId)
                .username(username)
                .password(password)
                .sessionId(IdGenerator.sessionId())
                .correlationId(IdGenerator.correlationId())
                .accounts(new ArrayList<>())
                .attributes(new ConcurrentHashMap<>())
                .build();
    }

    public void put(String key, Object value) {
        attributes.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(String key, Class<T> type) {
        Object value = attributes.get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(type.cast(value));
    }

    public Optional<AccountInfo> primaryAccount() {
        return accounts.stream().filter(AccountInfo::isPrimary).findFirst()
                .or(() -> accounts.stream().findFirst());
    }

    public BigDecimal primaryBalance() {
        return primaryAccount().map(AccountInfo::getBalance).orElse(BigDecimal.ZERO);
    }

    public boolean isAuthenticated() {
        return accessToken != null && !accessToken.isBlank();
    }
}
