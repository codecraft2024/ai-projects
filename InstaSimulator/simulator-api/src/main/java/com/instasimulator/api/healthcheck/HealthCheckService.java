package com.instasimulator.api.healthcheck;


import com.instasimulator.common.http.HttpResponse;
import com.instasimulator.common.http.SimulatorHttpClient;
import com.instasimulator.common.util.IdGenerator;
import com.instasimulator.common.util.JsonUtils;
import com.instasimulator.config.properties.SimulatorProperties;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class HealthCheckService {

    private static final DateTimeFormatter TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private final SimulatorHttpClient httpClient;
    private final SimulatorProperties properties;

    public HealthCheckService(SimulatorHttpClient httpClient, SimulatorProperties properties) {
        this.httpClient = httpClient;
        this.properties = properties;
    }

    public HealthCheckApiResponse runSuccess() {
        return execute("success", RequestMutation.none());
    }

    public HealthCheckApiResponse runFailure() {
        return runFailure(HealthCheckFailureScenario.INVALID_DEVICE_ID);
    }

    public HealthCheckApiResponse runFailure(HealthCheckFailureScenario scenario) {
        return execute(scenario.getId(), mutationFor(scenario));
    }

    public List<String> failureScenarioIds() {
        return Arrays.stream(HealthCheckFailureScenario.values())
                .map(HealthCheckFailureScenario::getId)
                .toList();
    }

    private HealthCheckApiResponse execute(String scenario, RequestMutation mutation) {
        String correlationId = IdGenerator.correlationId();
        SimulatorProperties.Instapay instapay = properties.getInstapay();

        String path = mutation.pathOverride != null ? mutation.pathOverride : instapay.getHealthCheckPath();
        Map<String, String> headers = buildHeaders(instapay, mutation);
        String body = buildBody(instapay.getDevice(), mutation);

        HttpResponse response = httpClient.post(instapayUrl(path), body, headers);
        HealthCheckResponse parsed = JsonUtils.fromJson(response.getBody(), HealthCheckResponse.class);

        return HealthCheckApiResponse.builder()
                .scenario(scenario)
                .httpStatus(response.getStatusCode())
                .durationMs(response.getDurationMs())
                .correlationId(correlationId)
                .body(parsed)
                .build();
    }

    private Map<String, String> buildHeaders(SimulatorProperties.Instapay instapay, RequestMutation mutation) {
        Map<String, String> headers = new LinkedHashMap<>();
        putHeader(headers, mutation.omitHeaders, "Accept-Encoding", "gzip");
        putHeader(headers, mutation.omitHeaders, "Connection", "Keep-Alive");
        putHeader(headers, mutation.omitHeaders, "Content-Type", "application/json; charset=UTF-8");
        putHeader(headers, mutation.omitHeaders, "Host", hostHeader(instapay.getApiBaseUrl()));
        putHeader(headers, mutation.omitHeaders, "User-Agent", "okhttp/5.0.0-alpha.2");
        putHeader(headers, mutation.omitHeaders, "x-appid", instapay.getAppId());
        putHeader(headers, mutation.omitHeaders, "x-timestamp", LocalDateTime.now().format(TIMESTAMP));
        mutation.headerOverrides.forEach(headers::put);
        return headers;
    }

    private String buildBody(SimulatorProperties.Device device, RequestMutation mutation) {
        if (mutation.omitDevice) {
            return "{}";
        }
        if (mutation.emptyDevice) {
            return JsonUtils.toJson(Map.of("device", Map.of()));
        }

        Map<String, Object> payload = new LinkedHashMap<>();
        putDeviceField(payload, mutation.omitDeviceFields, "appId", device.getAppId());
        putDeviceField(payload, mutation.omitDeviceFields, "appName", device.getAppName());
        putDeviceField(payload, mutation.omitDeviceFields, "appVersion", device.getAppVersion());
        putDeviceField(payload, mutation.omitDeviceFields, "deviceId", device.getDeviceId());
        putDeviceField(payload, mutation.omitDeviceFields, "gcmId", device.getGcmId());
        putDeviceField(payload, mutation.omitDeviceFields, "geoCode", device.getGeoCode());
        putDeviceField(payload, mutation.omitDeviceFields, "ip", device.getIp());
        putDeviceField(payload, mutation.omitDeviceFields, "language", device.getLanguage());
        putDeviceField(payload, mutation.omitDeviceFields, "location", device.getLocation());
        putDeviceField(payload, mutation.omitDeviceFields, "mobileNumber", device.getMobileNumber());
        putDeviceField(payload, mutation.omitDeviceFields, "os", device.getOs());
        putDeviceField(payload, mutation.omitDeviceFields, "type", device.getType());
        mutation.deviceFieldOverrides.forEach(payload::put);
        return JsonUtils.toJson(Map.of("device", payload));
    }

    private static RequestMutation mutationFor(HealthCheckFailureScenario scenario) {
        return switch (scenario) {
            case MISSING_DEVICE -> RequestMutation.builder().omitDevice(true).build();
            case EMPTY_DEVICE -> RequestMutation.builder().emptyDevice(true).build();
            case MISSING_DEVICE_ID -> RequestMutation.builder().omitDeviceFields(Set.of("deviceId")).build();
            case EMPTY_DEVICE_ID -> RequestMutation.builder()
                    .deviceFieldOverrides(Map.of("deviceId", "")).build();
            case INVALID_DEVICE_ID -> RequestMutation.builder()
                    .deviceFieldOverrides(Map.of("deviceId", "invalid-device")).build();
            case MISSING_APP_ID -> RequestMutation.builder().omitDeviceFields(Set.of("appId")).build();
            case WRONG_APP_ID -> RequestMutation.builder()
                    .deviceFieldOverrides(Map.of("appId", "com.wrong.app")).build();
            case MISSING_APP_NAME -> RequestMutation.builder().omitDeviceFields(Set.of("appName")).build();
            case MISSING_APP_VERSION -> RequestMutation.builder().omitDeviceFields(Set.of("appVersion")).build();
            case MISSING_GCM_ID -> RequestMutation.builder().omitDeviceFields(Set.of("gcmId")).build();
            case MISSING_GEO_CODE -> RequestMutation.builder().omitDeviceFields(Set.of("geoCode")).build();
            case MISSING_IP -> RequestMutation.builder().omitDeviceFields(Set.of("ip")).build();
            case MISSING_LOCATION -> RequestMutation.builder().omitDeviceFields(Set.of("location")).build();
            case MISSING_MOBILE_NUMBER -> RequestMutation.builder().omitDeviceFields(Set.of("mobileNumber")).build();
            case INVALID_MOBILE_NUMBER -> RequestMutation.builder()
                    .deviceFieldOverrides(Map.of("mobileNumber", "bad")).build();
            case MISSING_OS -> RequestMutation.builder().omitDeviceFields(Set.of("os")).build();
            case MISSING_TYPE -> RequestMutation.builder().omitDeviceFields(Set.of("type")).build();
            case INVALID_TXN_ID -> RequestMutation.builder()
                    .pathOverride("/v1/healthCheck/not-a-uuid").build();
            case WRONG_X_APP_ID -> RequestMutation.builder()
                    .headerOverrides(Map.of("x-appid", "com.wrong.app")).build();
            case MISSING_X_APP_ID -> RequestMutation.builder().omitHeaders(Set.of("x-appid")).build();
            case MISSING_X_TIMESTAMP -> RequestMutation.builder().omitHeaders(Set.of("x-timestamp")).build();
        };
    }

    private String instapayUrl(String path) {
        String base = properties.getInstapay().getApiBaseUrl();
        if (base.endsWith("/") && path.startsWith("/")) {
            return base.substring(0, base.length() - 1) + path;
        }
        return base + path;
    }

    private static String hostHeader(String apiBaseUrl) {
        String withoutScheme = apiBaseUrl.replace("https://", "").replace("http://", "");
        int slash = withoutScheme.indexOf('/');
        return slash >= 0 ? withoutScheme.substring(0, slash) : withoutScheme;
    }

    private static void putHeader(Map<String, String> headers, Set<String> omit, String name, String value) {
        if (!omit.contains(name) && !omit.contains(name.toLowerCase())) {
            headers.put(name, value);
        }
    }

    private static void putDeviceField(Map<String, Object> payload, Set<String> omit, String name, Object value) {
        if (!omit.contains(name)) {
            payload.put(name, value);
        }
    }

    private static final class RequestMutation {
        private final boolean omitDevice;
        private final boolean emptyDevice;
        private final Set<String> omitDeviceFields;
        private final Map<String, Object> deviceFieldOverrides;
        private final Set<String> omitHeaders;
        private final Map<String, String> headerOverrides;
        private final String pathOverride;

        private RequestMutation(Builder builder) {
            this.omitDevice = builder.omitDevice;
            this.emptyDevice = builder.emptyDevice;
            this.omitDeviceFields = builder.omitDeviceFields;
            this.deviceFieldOverrides = builder.deviceFieldOverrides;
            this.omitHeaders = builder.omitHeaders;
            this.headerOverrides = builder.headerOverrides;
            this.pathOverride = builder.pathOverride;
        }

        static RequestMutation none() {
            return builder().build();
        }

        static Builder builder() {
            return new Builder();
        }

        private static final class Builder {
            private boolean omitDevice;
            private boolean emptyDevice;
            private Set<String> omitDeviceFields = Set.of();
            private Map<String, Object> deviceFieldOverrides = Map.of();
            private Set<String> omitHeaders = Set.of();
            private Map<String, String> headerOverrides = Map.of();
            private String pathOverride;

            Builder omitDevice(boolean value) {
                this.omitDevice = value;
                return this;
            }

            Builder emptyDevice(boolean value) {
                this.emptyDevice = value;
                return this;
            }

            Builder omitDeviceFields(Set<String> value) {
                this.omitDeviceFields = value;
                return this;
            }

            Builder deviceFieldOverrides(Map<String, Object> value) {
                this.deviceFieldOverrides = value;
                return this;
            }

            Builder omitHeaders(Set<String> value) {
                this.omitHeaders = value;
                return this;
            }

            Builder headerOverrides(Map<String, String> value) {
                this.headerOverrides = value;
                return this;
            }

            Builder pathOverride(String value) {
                this.pathOverride = value;
                return this;
            }

            RequestMutation build() {
                return new RequestMutation(this);
            }
        }
    }
}
