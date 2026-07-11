package com.instasimulator.calls.qr;

import com.instasimulator.common.context.SimulationContext;
import com.instasimulator.common.dto.CallResult;
import com.instasimulator.common.exception.CallValidationException;
import com.instasimulator.common.http.HttpResponse;
import com.instasimulator.common.http.SimulatorHttpClient;
import com.instasimulator.common.util.JsonUtils;
import com.instasimulator.common.util.RandomDataGenerator;
import com.instasimulator.config.properties.SimulatorProperties;
import com.instasimulator.core.call.AbstractApiCall;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Generates a payment QR code payload.
 */
@Component
public class QRGenerateCall extends AbstractApiCall {

    public QRGenerateCall(SimulatorHttpClient httpClient, SimulatorProperties properties) {
        super(httpClient, properties);
    }

    @Override
    public String name() {
        return "qr-generate";
    }

    @Override
    protected HttpResponse doExecute(SimulationContext context) {
        String body = JsonUtils.toJson(Map.of(
                "amount", RandomDataGenerator.amount(1, 75),
                "currency", "USD"
        ));
        return httpClient.post(url("/api/v1/qr/generate"), body, defaultHeaders(context));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void validate(SimulationContext context, CallResult result) {
        requireSuccess(result, "QR generate failed");
        Map<String, Object> payload = JsonUtils.fromJson(result.getResponseBody(), Map.class);
        Object qrCode = payload.get("qrCode");
        if (qrCode == null) {
            throw new CallValidationException(name(), "Missing qrCode");
        }
        context.put("qrCode", String.valueOf(qrCode));
    }
}
