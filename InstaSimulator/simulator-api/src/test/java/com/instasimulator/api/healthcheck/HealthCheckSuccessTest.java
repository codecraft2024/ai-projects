package com.instasimulator.api.healthcheck;




import com.instasimulator.common.http.HttpClientConfig;
import com.instasimulator.common.http.SimulatorHttpClient;
import com.instasimulator.config.properties.SimulatorProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HealthCheckSuccessTest {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckSuccessTest.class);

    private SimulatorHttpClient httpClient;
    private HealthCheckService healthCheckService;

    @BeforeEach
    void setUp() {
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

    @AfterEach
    void tearDown() throws Exception {
        if (httpClient != null) {
            httpClient.close();
        }
    }

    @Test
    void success_assertsAllResponseData() {
        HealthCheckApiResponse apiResponse = healthCheckService.runSuccess();
        log.info("SUCCESS response: {}", apiResponse);

        assertEquals("success", apiResponse.getScenario());
        assertEquals(200, apiResponse.getHttpStatus());
        assertTrue(apiResponse.getDurationMs() >= 0);
        assertNotNull(apiResponse.getCorrelationId());

        HealthCheckResponse body = apiResponse.getBody();
        assertNotNull(body);
        assertEquals("00000", body.getCode());
        assertTrue(body.getResult().contains("Your request has been processed successfully"));
        assertNotNull(body.getChecksum());
        assertFalse(body.getChecksum().isBlank());
        assertEquals(body.getChecksum(), body.getCheckSumValue());

        HealthCheckData data = body.getData();
        assertNotNull(data);
        assertEquals("NBEPay", data.getKeywordMsg());
        assertEquals(Integer.valueOf(0), data.getWaitTime());
        assertEquals("1.0", data.getMinVersion());
        assertEquals("5.0.0", data.getMaxVersion());
        assertNotNull(data.getAppPubKey());
        assertTrue(data.getAppPubKey().startsWith("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA"));
        assertEquals("Y", data.getLoginAllow());
        assertEquals("Y", data.getRegisterAllow());
        assertEquals("01227364439", data.getCustomerCareNumber());
        assertEquals("+201005824046", data.getCustomerCareWatsappNumber());
        assertEquals("1.0.0", data.getIosMinVersion());
        assertEquals("5.0.0", data.getIosMaxVersion());
        assertEquals("11.04", data.getListCategoryVersion());
        assertEquals("11.04", data.getListServiceVersion());
        assertEquals("11.04", data.getListServiceVersionAr());
        assertEquals("", data.getBillInqConfig());
        assertEquals("2.24", data.getAtmCashoutFee());
        assertEquals("y", data.getForgotIpaEnabled());
        assertNull(data.getCustom());

        assertEquals(1, data.getServiceProverderNum().size());
        ServiceProviderNum sp = data.getServiceProverderNum().getFirst();
        assertEquals("4551", sp.getOperatorNum());
        assertEquals("1", sp.getPriority());
        assertNull(sp.getKeywordMsg());
        assertNull(sp.getAlias());

        assertEquals(2, data.getLocalProviders().size());
        ServiceProviderNum local1 = data.getLocalProviders().get(0);
        assertEquals("4551", local1.getOperatorNum());
        assertEquals("1", local1.getPriority());
        assertEquals("NBE", local1.getKeywordMsg());
        assertEquals("abcd", local1.getAlias());
        ServiceProviderNum local2 = data.getLocalProviders().get(1);
        assertEquals("4551", local2.getOperatorNum());
        assertEquals("2", local2.getPriority());
        assertEquals("NBE", local2.getKeywordMsg());
        assertEquals("abc", local2.getAlias());

        assertTrue(data.getRoamingProviders().isEmpty());
    }
}
