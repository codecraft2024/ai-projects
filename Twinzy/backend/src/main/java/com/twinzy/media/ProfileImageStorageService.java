package com.twinzy.media;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.twinzy.config.TwinzyProperties;

@Service
public class ProfileImageStorageService {

    private static final Logger log = LoggerFactory.getLogger(ProfileImageStorageService.class);

    private final Path storageRoot;
    private final String publicBaseUrl;

    public ProfileImageStorageService(TwinzyProperties properties) {
        this.storageRoot = Paths.get(properties.getMedia().getStoragePath()).toAbsolutePath().normalize();
        this.publicBaseUrl = trimTrailingSlash(properties.getMedia().getPublicBaseUrl());
        try {
            Files.createDirectories(storageRoot);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed creating image storage directory: " + storageRoot, ex);
        }
    }

    public String store(String profileId, String imageId, byte[] imageBytes) {
        try {
            Path profileDir = storageRoot.resolve(sanitize(profileId));
            Files.createDirectories(profileDir);
            String filename = sanitize(imageId) + ".jpg";
            Path target = profileDir.resolve(filename);
            Files.write(target, imageBytes);
            return publicBaseUrl + "/api/media/profiles/" + sanitize(profileId) + "/" + filename;
        } catch (IOException ex) {
            throw new IllegalStateException("Failed storing image for profile " + profileId, ex);
        }
    }

    public Path resolve(String profileId, String filename) {
        Path target = storageRoot.resolve(sanitize(profileId)).resolve(sanitize(filename)).normalize();
        if (!target.startsWith(storageRoot)) {
            throw new IllegalArgumentException("Invalid media path");
        }
        return target;
    }

    public void clearAll() {
        if (!Files.exists(storageRoot)) {
            return;
        }
        try (Stream<Path> paths = Files.walk(storageRoot)) {
            paths.sorted(Comparator.reverseOrder())
                    .filter(path -> !path.equals(storageRoot))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ex) {
                            log.warn("Failed deleting {}: {}", path, ex.getMessage());
                        }
                    });
        } catch (IOException ex) {
            log.warn("Failed clearing image storage: {}", ex.getMessage());
        }
    }

    private String sanitize(String value) {
        return value.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private String trimTrailingSlash(String value) {
        if (value == null || value.isBlank()) {
            return "http://localhost:8081";
        }
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }
}
