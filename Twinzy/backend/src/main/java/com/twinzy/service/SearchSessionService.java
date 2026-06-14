package com.twinzy.service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.twinzy.dto.SearchResponseDto;

@Service
public class SearchSessionService {

    private static final long TTL_MS = 24 * 60 * 60 * 1000L;

    private final Map<String, SessionEntry> sessions = new ConcurrentHashMap<>();

    public String createSession(SearchResponseDto response) {
        pruneSessions();
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, new SessionEntry(Instant.now().toEpochMilli(), response));
        return sessionId;
    }

    public SearchResponseDto getSession(String sessionId) {
        pruneSessions();
        SessionEntry entry = sessions.get(sessionId);
        return entry == null ? null : entry.response();
    }

    private void pruneSessions() {
        long now = Instant.now().toEpochMilli();
        sessions.entrySet().removeIf(entry -> now - entry.getValue().createdAt() > TTL_MS);
    }

    private record SessionEntry(long createdAt, SearchResponseDto response) {
    }
}
