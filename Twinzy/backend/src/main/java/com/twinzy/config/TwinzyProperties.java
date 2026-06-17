package com.twinzy.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "twinzy")
public class TwinzyProperties {

    private String faceProvider = "analysis";
    private int matchLimit = 12;
    private int matchCandidatePool = 300;
    private Cors cors = new Cors();
    private Database database = new Database();
    private Seed seed = new Seed();
    private Media media = new Media();

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

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public Seed getSeed() {
        return seed;
    }

    public void setSeed(Seed seed) {
        this.seed = seed;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
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

    public static class Database {
        private String seedPath = "../database/seed";
        private String migrationsPath = "../database/migrations";

        public String getSeedPath() {
            return seedPath;
        }

        public void setSeedPath(String seedPath) {
            this.seedPath = seedPath;
        }

        public String getMigrationsPath() {
            return migrationsPath;
        }

        public void setMigrationsPath(String migrationsPath) {
            this.migrationsPath = migrationsPath;
        }
    }

    public static class Media {
        private String storagePath = "../database/images";
        private String publicBaseUrl = "http://localhost:8081";

        public String getStoragePath() {
            return storagePath;
        }

        public void setStoragePath(String storagePath) {
            this.storagePath = storagePath;
        }

        public String getPublicBaseUrl() {
            return publicBaseUrl;
        }

        public void setPublicBaseUrl(String publicBaseUrl) {
            this.publicBaseUrl = publicBaseUrl;
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
