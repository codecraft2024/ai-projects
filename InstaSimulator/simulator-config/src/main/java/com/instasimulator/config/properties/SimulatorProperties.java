package com.instasimulator.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Externalized endpoint and timeout configuration.
 */
@Data
@ConfigurationProperties(prefix = "simulator")
public class SimulatorProperties {

    private String baseUrl = "http://localhost:8089";
    private Auth auth = new Auth();
    private Timeouts timeouts = new Timeouts();
    private Retry retry = new Retry();
    private Stress stress = new Stress();
    private Instapay instapay = new Instapay();
    private String reportsDir = "reports";

    @Data
    public static class Auth {
        private String loginPath = "/api/v1/auth/login";
        private String refreshPath = "/api/v1/auth/refresh";
        private String logoutPath = "/api/v1/auth/logout";
        private String profilePath = "/api/v1/users/me";
    }

    @Data
    public static class Instapay {
        private String apiBaseUrl = "https://instapaystaging.egyptianbanks.com:5443";
        private String domain = "instapayqc.egyptianbanks.com";
        private String certHashSha256 = "sha256/cyLYqFigYmyDcPcyATlTzFegqhEqawr0ofZRoRckjQo=";
        private String handler = "@qnbpay";
        private String deviceId = "4404f62d1ca7ae83";
        private String host = "qr.instapayqc.egyptianbanks.com";
        private String firebaseSpecialLogging = "y";
        private String appId = "com.olive.ebc";
        private String healthCheckPath = "/v1/healthCheck/84f7f743-722f-4683-8919-3f95e1f88af5";
        private Device device = new Device();
    }

    @Data
    public static class Device {
        private String appId = "com.olive.ebc";
        private String appName = "EBCPSP";
        private String appVersion = "1.12.1";
        private String deviceId = "4404f62d1ca7ae83";
        private String gcmId = "c4n_NM9cTO-_aavEmEWrog:APA91bEVs5JQoKssJna9ZMNjum2GYY8biCuMss-Gj38oQbkFs3likY5_dZSOV7qafapn960qfCvPW0OsJdrDQ599bqDzFip1GEK7OGyn292wN7rRnHEjEq8";
        private String geoCode = "26.8206,30.8025";
        private String ip = "10.0.2.15";
        private String language = "en";
        private String location = "Cairo";
        private String mobileNumber = "00201000000000";
        private String os = "Android16";
        private String type = "sdk_gphone16k_arm64";
    }

    @Data
    public static class Timeouts {
        private int connectMs = 5_000;
        private int readMs = 30_000;
    }

    @Data
    public static class Retry {
        private int maxAttempts = 3;
        private long backoffMs = 500L;
    }

    @Data
    public static class Stress {
        private int defaultUsers = 10;
        private long thinkTimeMinMs = 200L;
        private long thinkTimeMaxMs = 1_500L;
        private long rampUpSeconds = 10L;
        private long durationSeconds = 60L;
    }
}
