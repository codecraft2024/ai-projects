package com.twinzy.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "twinzy")
public class TwinzyProperties {

    private String dataPath = "../data/profiles.json";
    private String faceProvider = "mock";
    private int matchLimit = 12;
    private int matchCandidatePool = 300;
    private Cors cors = new Cors();
    private Seed seed = new Seed();

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

    public int getMatchLimit() {
        return matchLimit;
    }

    public void setMatchLimit(int matchLimit) {
        this.matchLimit = matchLimit;
    }

    public int getMatchCandidatePool() {
        return matchCandidatePool;
    }

    public void setMatchCandidatePool(int matchCandidatePool) {
        this.matchCandidatePool = matchCandidatePool;
    }

    public Cors getCors() {
        return cors;
    }

    public void setCors(Cors cors) {
        this.cors = cors;
    }

    public Seed getSeed() {
        return seed;
    }

    public void setSeed(Seed seed) {
        this.seed = seed;
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

    public static class Seed {
        private int totalProfiles = 20000;
        private int funnyCount = 20;

        public int getTotalProfiles() {
            return totalProfiles;
        }

        public void setTotalProfiles(int totalProfiles) {
            this.totalProfiles = totalProfiles;
        }

        public int getFunnyCount() {
            return funnyCount;
        }

        public void setFunnyCount(int funnyCount) {
            this.funnyCount = funnyCount;
        }
    }
}
