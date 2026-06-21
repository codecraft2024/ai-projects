package com.patienthub.service;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuthTokenStore {

    private static final long TOKEN_TTL_SECONDS = 24 * 60 * 60;

    private final Map<String, TokenEntry> tokens = new ConcurrentHashMap<>();

    public String issueToken(String username) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, new TokenEntry(username, Instant.now().plusSeconds(TOKEN_TTL_SECONDS)));
        purgeExpired();
        return token;
    }

    public Optional<String> resolveUsername(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        TokenEntry entry = tokens.get(token);
        if (entry == null || entry.expiresAt().isBefore(Instant.now())) {
            if (entry != null) {
                tokens.remove(token);
            }
            return Optional.empty();
        }
        return Optional.of(entry.username());
    }

    public void revoke(String token) {
        if (token != null) {
            tokens.remove(token);
        }
    }

    private void purgeExpired() {
        Instant now = Instant.now();
        tokens.entrySet().removeIf(e -> e.getValue().expiresAt().isBefore(now));
    }

    private record TokenEntry(String username, Instant expiresAt) {
    }
}
