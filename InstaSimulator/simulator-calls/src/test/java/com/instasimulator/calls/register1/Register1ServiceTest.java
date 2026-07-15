package com.instasimulator.calls.register1;

import com.instasimulator.common.dto.CallExecution;
import com.instasimulator.common.dto.register1.Register1Request;
import com.instasimulator.common.dto.register1.Register1Response;
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

@SpringBootTest(classes = Register1ServiceTest.TestApp.class)
class Register1ServiceTest {

    @Autowired
    private Register1Service register1Service;

    @Test
    void runSuccessHitsStaging() {
        CallExecution<Register1Request, Register1Response> execution = register1Service.runSuccess();

        assertNotNull(execution.request().appPin());
        assertNotNull(execution.request().deviceDetails());
        assertNotNull(execution.response().code());
        assertNotNull(execution.response().result());
        // Staging may return 00000 when salt is available, or 99994 missing salt.
        assertFalse(execution.response().code().isBlank());
    }

    @Test
    void runFailureMissingAppPin() {
        CallExecution<Register1Request, Register1Response> execution = register1Service.runFailure();

        assertNull(execution.request().appPin());
        assertFalse(execution.response().isSuccess());
        assertEquals("100", execution.response().code());
    }

    @SpringBootApplication
    @Import({ConfigAutoConfiguration.class, Register1Service.class, Register1Client.class})
    static class TestApp {
    }
}
