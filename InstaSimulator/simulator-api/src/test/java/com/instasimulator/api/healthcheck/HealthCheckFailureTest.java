package com.instasimulator.api.healthcheck;


import com.instasimulator.common.http.HttpClientConfig;
import com.instasimulator.common.http.SimulatorHttpClient;
import com.instasimulator.config.properties.SimulatorProperties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HealthCheckFailureTest {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckFailureTest.class);

    private static SimulatorHttpClient httpClient;
    private static HealthCheckService healthCheckService;

    @BeforeAll
    static void setUp() {
        SimulatorProperties properties = new SimulatorProperties();
        properties.getTimeouts().setConnectMs(15_000);
        properties.getTimeouts().setReadMs(30_000);

        httpClient = new SimulatorHttpClient(HttpClientConfig.builder()
                .connectTimeoutMs(15_000)
                .responseTimeoutMs(30_000)
                .maxRetries(0)
                .logRequests(true)
                .logResponses(true)
                .build());
        healthCheckService = new HealthCheckService(httpClient, properties);
    }

    @AfterAll
    static void tearDown() throws Exception {
        if (httpClient != null) {
            httpClient.close();
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @EnumSource(HealthCheckFailureScenario.class)
    void failure_assertsAllResponseData(HealthCheckFailureScenario scenario) {
        HealthCheckApiResponse apiResponse = healthCheckService.runFailure(scenario);
        log.info("FAILURE scenario={} response={}", scenario.getId(), apiResponse);

        assertEquals(scenario.getId(), apiResponse.getScenario());
        assertEquals(200, apiResponse.getHttpStatus());
        assertTrue(apiResponse.getDurationMs() >= 0);
        assertNotNull(apiResponse.getCorrelationId());

        HealthCheckResponse body = apiResponse.getBody();
        assertNotNull(body);
        assertEquals(scenario.getExpectedCode(), body.getCode());
        assertFailureResult(scenario, body.getResult());
        assertNull(body.getData());
        assertNull(body.getChecksum());
        assertNull(body.getCheckSumValue());
    }

    private static void assertFailureResult(HealthCheckFailureScenario scenario, String actualResult) {
        assertNotNull(actualResult);

        if (scenario == HealthCheckFailureScenario.EMPTY_DEVICE) {
            assertTrue(actualResult.endsWith("field is mandatory"),
                    "Expected mandatory-field error, got: " + actualResult);
            return;
        }

        if (scenario == HealthCheckFailureScenario.EMPTY_DEVICE_ID) {
            assertTrue(
                    Set.of("deviceId field is mandatory", "Invalid deviceId").contains(actualResult),
                    "Unexpected result for empty-deviceId: " + actualResult);
            return;
        }

        assertEquals(scenario.getExpectedResult(), actualResult);
    }
}
