package com.tabeeby.service;

import com.tabeeby.model.AdminSession;
import com.tabeeby.repository.AdminSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AuthTokenStore {

    private static final long TOKEN_TTL_SECONDS = 24 * 60 * 60;

    private final AdminSessionRepository adminSessionRepository;

    public AuthTokenStore(AdminSessionRepository adminSessionRepository) {
        this.adminSessionRepository = adminSessionRepository;
    }

    public String issueToken(String username) {
        purgeExpired();

        String token = UUID.randomUUID().toString();
        AdminSession session = new AdminSession();
        session.setToken(token);
        session.setUsername(username);
        session.setExpiresAt(Instant.now().plusSeconds(TOKEN_TTL_SECONDS));
        adminSessionRepository.save(session);
        return token;
    }

    @Transactional(readOnly = true)
    public Optional<String> resolveUsername(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        return adminSessionRepository.findById(token)
                .filter(session -> session.getExpiresAt().isAfter(Instant.now()))
                .map(AdminSession::getUsername);
    }

    public void revoke(String token) {
        if (token != null && !token.isBlank()) {
            adminSessionRepository.deleteById(token);
        }
    }

    private void purgeExpired() {
        adminSessionRepository.deleteExpired(Instant.now());
    }
}
