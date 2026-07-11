package com.instasimulator.engine.session;

import com.instasimulator.common.context.SimulationContext;
import com.instasimulator.common.util.IdGenerator;
import com.instasimulator.config.loader.ConfigLoader;
import com.instasimulator.config.properties.UserCredentials;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Creates and tracks simulation sessions / contexts.
 */
@Component
public class SessionManager {

    private final ConfigLoader configLoader;
    private final Map<String, SimulationContext> sessions = new ConcurrentHashMap<>();
    private final AtomicInteger userCursor = new AtomicInteger();

    public SessionManager(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    public SimulationContext createContext() {
        UserCredentials user = nextUser();
        SimulationContext context = SimulationContext.create(
                user.getUserId(), user.getUsername(), user.getPassword());
        sessions.put(context.getSessionId(), context);
        return context;
    }

    public SimulationContext createContext(String username, String password) {
        SimulationContext context = SimulationContext.create(
                IdGenerator.shortId(), username, password);
        sessions.put(context.getSessionId(), context);
        return context;
    }

    public SimulationContext get(String sessionId) {
        return sessions.get(sessionId);
    }

    public void remove(String sessionId) {
        sessions.remove(sessionId);
    }

    public int activeSessions() {
        return sessions.size();
    }

    private UserCredentials nextUser() {
        List<UserCredentials> pool = configLoader.loadUserPool();
        if (pool.isEmpty()) {
            UserCredentials fallback = new UserCredentials();
            fallback.setUserId("user-demo");
            fallback.setUsername("demo.user");
            fallback.setPassword("Password123!");
            return fallback;
        }
        int index = Math.floorMod(userCursor.getAndIncrement(), pool.size());
        return pool.get(index);
    }
}
