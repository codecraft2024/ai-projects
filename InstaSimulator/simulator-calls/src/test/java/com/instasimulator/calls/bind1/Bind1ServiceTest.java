package com.instasimulator.calls.bind1;

import com.instasimulator.common.dto.CallExecution;
import com.instasimulator.common.dto.bind1.Bind1Request;
import com.instasimulator.common.dto.bind1.Bind1Response;
import com.instasimulator.config.ConfigAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = Bind1ServiceTest.TestApp.class)
class Bind1ServiceTest {

    @Autowired
    private Bind1Service bind1Service;

    @Test
    void runSuccessHitsStaging() {
        CallExecution<Bind1Request, Bind1Response> execution = bind1Service.runSuccess();

        assertNotNull(execution.request().device());
        assertNotNull(execution.request().encString());
        assertEquals("00000", execution.response().code());
        assertTrue(execution.response().isSuccess());
        assertNotNull(execution.response().data());
        assertNotNull(execution.response().data().action());
        assertEquals("mina", execution.response().data().name());
        assertTrue(execution.durationMs() >= 0);
    }

    @Test
    void runFailureMissingEncString() {
        CallExecution<Bind1Request, Bind1Response> execution = bind1Service.runFailure();

        assertNull(execution.request().encString());
        assertFalse(execution.response().isSuccess());
        assertEquals("99994", execution.response().code());
        assertNull(execution.response().data());
    }

    @SpringBootApplication
    @Import({ConfigAutoConfiguration.class, Bind1Service.class, Bind1Client.class})
    static class TestApp {
    }
}
