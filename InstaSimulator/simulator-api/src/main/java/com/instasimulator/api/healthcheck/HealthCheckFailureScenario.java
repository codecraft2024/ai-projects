package com.instasimulator.api.healthcheck;

public enum HealthCheckFailureScenario {

    MISSING_DEVICE("missing-device", "100", "device details are mandatory"),
    EMPTY_DEVICE("empty-device", "100", "deviceId field is mandatory"),
    MISSING_DEVICE_ID("missing-deviceId", "100", "deviceId field is mandatory"),
    EMPTY_DEVICE_ID("empty-deviceId", "100", "deviceId field is mandatory"),
    INVALID_DEVICE_ID("invalid-deviceId", "100", "Invalid deviceId"),
    MISSING_APP_ID("missing-appId", "100", "appId field is mandatory"),
    WRONG_APP_ID("wrong-appId", "100", "Miss Match AppId"),
    MISSING_APP_NAME("missing-appName", "100", "appName field is mandatory"),
    MISSING_APP_VERSION("missing-appVersion", "100", "appVersion field is mandatory"),
    MISSING_GCM_ID("missing-gcmId", "100", "gcmId field is mandatory"),
    MISSING_GEO_CODE("missing-geoCode", "100", "geoCode field is mandatory"),
    MISSING_IP("missing-ip", "100", "ip field is mandatory"),
    MISSING_LOCATION("missing-location", "100", "location field is mandatory"),
    MISSING_MOBILE_NUMBER("missing-mobileNumber", "100", "mobile number field is mandatory"),
    INVALID_MOBILE_NUMBER("invalid-mobileNumber", "100", "Invalid mobile number"),
    MISSING_OS("missing-os", "100", "os field is mandatory"),
    MISSING_TYPE("missing-type", "100", "type field is mandatory"),
    INVALID_TXN_ID("invalid-txnId", "100", "invalid TxnId"),
    WRONG_X_APP_ID("wrong-x-appid", "100", "Miss Match AppId"),
    MISSING_X_APP_ID("missing-x-appid", "100", "invalid X-AppId"),
    MISSING_X_TIMESTAMP("missing-x-timestamp", "100", "invalid X-TimeStamp");

    private final String id;
    private final String expectedCode;
    private final String expectedResult;

    HealthCheckFailureScenario(String id, String expectedCode, String expectedResult) {
        this.id = id;
        this.expectedCode = expectedCode;
        this.expectedResult = expectedResult;
    }

    public String getId() {
        return id;
    }

    public String getExpectedCode() {
        return expectedCode;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public static HealthCheckFailureScenario fromId(String id) {
        for (HealthCheckFailureScenario scenario : values()) {
            if (scenario.id.equalsIgnoreCase(id) || scenario.name().equalsIgnoreCase(id)) {
                return scenario;
            }
        }
        throw new IllegalArgumentException("Unknown failure scenario: " + id);
    }
}
