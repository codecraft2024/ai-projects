package com.instasimulator.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "simulator")
public class SimulatorProperties {

    private String baseUrl = "http://localhost:8080";
    private Timeouts timeouts = new Timeouts();
    private HealthCheck healthCheck = new HealthCheck();

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Timeouts getTimeouts() {
        return timeouts;
    }

    public void setTimeouts(Timeouts timeouts) {
        this.timeouts = timeouts;
    }

    public HealthCheck getHealthCheck() {
        return healthCheck;
    }

    public void setHealthCheck(HealthCheck healthCheck) {
        this.healthCheck = healthCheck;
    }

    public static class Timeouts {
        private int connectMs = 5_000;
        private int readMs = 15_000;

        public int getConnectMs() {
            return connectMs;
        }

        public void setConnectMs(int connectMs) {
            this.connectMs = connectMs;
        }

        public int getReadMs() {
            return readMs;
        }

        public void setReadMs(int readMs) {
            this.readMs = readMs;
        }
    }

    public static class HealthCheck {
        private String successMessage = "Service is healthy";
        private String failureMessage = "Service unavailable";
        private boolean failureThrows = false;

        public String getSuccessMessage() {
            return successMessage;
        }

        public void setSuccessMessage(String successMessage) {
            this.successMessage = successMessage;
        }

        public String getFailureMessage() {
            return failureMessage;
        }

        public void setFailureMessage(String failureMessage) {
            this.failureMessage = failureMessage;
        }

        public boolean isFailureThrows() {
            return failureThrows;
        }

        public void setFailureThrows(boolean failureThrows) {
            this.failureThrows = failureThrows;
        }
    }
}
