package com.twinzy.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "twinzy")
public class TwinzyProperties {

    private String dataPath = "../data/profiles.json";
    private String faceProvider = "mock";
    private Cors cors = new Cors();

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getFaceProvider() {
        return faceProvider;
    }

    public void setFaceProvider(String faceProvider) {
        this.faceProvider = faceProvider;
    }

    public Cors getCors() {
        return cors;
    }

    public void setCors(Cors cors) {
        this.cors = cors;
    }

    public static class Cors {
        private List<String> allowedOrigins = List.of("http://localhost:3001");

        public List<String> getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }
    }
}
