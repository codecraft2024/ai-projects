package com.instasimulator.calls.healthcheck;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instasimulator.common.dto.CallExecution;
import com.instasimulator.common.dto.healthcheck.HealthCheckRequest;
import com.instasimulator.common.dto.healthcheck.HealthCheckResponse;
import com.instasimulator.common.exception.HealthCheckException;
import com.instasimulator.config.properties.SimulatorProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HealthCheckServiceTest {

    private HealthCheckService service;
    private SimulatorProperties properties;

    @BeforeEach
    void setUp() {
        properties = new SimulatorProperties();
        service = new HealthCheckService(properties, new ObjectMapper().findAndRegisterModules());
    }

    @Test
    void runSuccessReturnsHealthyPayload() {
        CallExecution<HealthCheckRequest, HealthCheckResponse> execution = service.runSuccess();

        assertEquals("success", execution.request().scenario());
        assertTrue(execution.response().success());
        assertEquals("Service is healthy", execution.response().message());
        assertNotNull(execution.startedAt());
        assertNotNull(execution.finishedAt());
        assertFalse(execution.timeline().isEmpty());
        assertTrue(execution.durationMs() >= 0);
    }

    @Test
    void runFailureReturnsUnavailablePayload() {
        CallExecution<HealthCheckRequest, HealthCheckResponse> execution = service.runFailure();

        assertEquals("failure", execution.request().scenario());
        assertFalse(execution.response().success());
        assertEquals("Service unavailable", execution.response().message());
    }

    @Test
    void failureBodyThrowsWhenConfigured() {
        properties.getHealthCheck().setFailureThrows(true);
        assertThrows(HealthCheckException.class, service::failureBody);
    }
}
