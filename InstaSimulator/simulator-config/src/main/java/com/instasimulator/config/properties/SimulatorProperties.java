package com.instasimulator.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "simulator")
public class SimulatorProperties {

    private String baseUrl = "http://localhost:8080";
    private Timeouts timeouts = new Timeouts();
    private HealthCheck healthCheck = new HealthCheck();
    private Instapay instapay = new Instapay();

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

    public Instapay getInstapay() {
        return instapay;
    }

    public void setInstapay(Instapay instapay) {
        this.instapay = instapay;
    }

    public static class Timeouts {
        private int connectMs = 5_000;
        private int readMs = 30_000;

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

    public static class Instapay {
        private String apiBaseUrl = "https://instapaystaging.egyptianbanks.com:5443";
        private String appId = "com.olive.ebc";
        private Device device = new Device();
        private Bind1 bind1 = new Bind1();
        private Register1 register1 = new Register1();

        public String getApiBaseUrl() {
            return apiBaseUrl;
        }

        public void setApiBaseUrl(String apiBaseUrl) {
            this.apiBaseUrl = apiBaseUrl;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public Device getDevice() {
            return device;
        }

        public void setDevice(Device device) {
            this.device = device;
        }

        public Bind1 getBind1() {
            return bind1;
        }

        public void setBind1(Bind1 bind1) {
            this.bind1 = bind1;
        }

        public Register1 getRegister1() {
            return register1;
        }

        public void setRegister1(Register1 register1) {
            this.register1 = register1;
        }
    }

    public static class Bind1 {
        private String path = "/v1/bind1/717a1baf-4e60-4bb4-a44f-958b7e41c3ca";
        private String encString = "nxmrl/iCgRL2kbjE5sCwcIk/wKls2QgEUNlLq+pcwUAHgEbm2sZKH4PrGj+k/ARphXIx71ewHYQuJ0cA4914eXAjdHx0ht/7TNFHsgMnjxKBiKpE/x2rm94tTlN2OS0ky5rqF/FvCx2eHeMEFTWqMHOqZ3db+MvvkHTOqhU27ClLvAnsgS9vdB2s/+9Fd1+mF4xlCexLQsAskPDQR3oZx/RvOVOWVy6kp/g91Cr0rtHKWHNR+vjizZttVLBl79/vh0p2vu1+HwAPY3PMIXjMPBsFMVkTXxlvYNWDvRbZPc3xUi0U7/kxPf16+PVBs5xyNOz30oDuEYcJKherAfL94w==";

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getEncString() {
            return encString;
        }

        public void setEncString(String encString) {
            this.encString = encString;
        }
    }

    public static class Register1 {
        private String path = "/v1/customer/register1/ec6251b0-eaf1-4a14-b96f-98c759284418";
        private String appPin = "DDD703CB217E976073C79F57880D9C279D9BD7FC04D87687D34A0E131021A21867D64864C215B00D8DB3BF64E13FB549";
        private String dob = "";
        private String email = "";
        private String existingIpa = "mina_6@qnbpay";
        private String isNewVersion = "Y";
        private String name = "mina";
        private String randomSmsText = "NBE,abcd,967f772655a34056a836d3fa2b057eae";
        private String refereeIpa = "";
        private String regType = "DEVCHNG";

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getAppPin() {
            return appPin;
        }

        public void setAppPin(String appPin) {
            this.appPin = appPin;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getExistingIpa() {
            return existingIpa;
        }

        public void setExistingIpa(String existingIpa) {
            this.existingIpa = existingIpa;
        }

        public String getIsNewVersion() {
            return isNewVersion;
        }

        public void setIsNewVersion(String isNewVersion) {
            this.isNewVersion = isNewVersion;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRandomSmsText() {
            return randomSmsText;
        }

        public void setRandomSmsText(String randomSmsText) {
            this.randomSmsText = randomSmsText;
        }

        public String getRefereeIpa() {
            return refereeIpa;
        }

        public void setRefereeIpa(String refereeIpa) {
            this.refereeIpa = refereeIpa;
        }

        public String getRegType() {
            return regType;
        }

        public void setRegType(String regType) {
            this.regType = regType;
        }
    }

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
        private String mobileNumber = "00201221258777";
        private String os = "Android16";
        private String type = "sdk_gphone16k_arm64";

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getGcmId() {
            return gcmId;
        }

        public void setGcmId(String gcmId) {
            this.gcmId = gcmId;
        }

        public String getGeoCode() {
            return geoCode;
        }

        public void setGeoCode(String geoCode) {
            this.geoCode = geoCode;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getOs() {
            return os;
        }

        public void setOs(String os) {
            this.os = os;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
