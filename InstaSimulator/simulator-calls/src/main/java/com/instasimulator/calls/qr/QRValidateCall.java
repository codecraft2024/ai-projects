package com.instasimulator.calls.qr;

import com.instasimulator.common.context.SimulationContext;
import com.instasimulator.common.dto.CallResult;
import com.instasimulator.common.http.HttpResponse;
import com.instasimulator.common.http.SimulatorHttpClient;
import com.instasimulator.common.util.JsonUtils;
import com.instasimulator.config.properties.SimulatorProperties;
import com.instasimulator.core.call.AbstractApiCall;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Validates a previously generated QR code.
 */
@Component
public class QRValidateCall extends AbstractApiCall {

    public QRValidateCall(SimulatorHttpClient httpClient, SimulatorProperties properties) {
        super(httpClient, properties);
    }

    @Override
    public String name() {
        return "qr-validate";
    }

    @Override
    protected HttpResponse doExecute(SimulationContext context) {
        String qrCode = context.get("qrCode", String.class).orElse("UNKNOWN");
        String body = JsonUtils.toJson(Map.of("qrCode", qrCode));
        return httpClient.post(url("/api/v1/qr/validate"), body, defaultHeaders(context));
    }

    @Override
    public void validate(SimulationContext context, CallResult result) {
        requireSuccess(result, "QR validate failed");
        context.put("qrValidation", result.getResponseBody());
    }
}
