package com.twinzy.controller;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.twinzy.media.ProfileImageStorageService;

@RestController
public class MediaController {

    private final ProfileImageStorageService profileImageStorageService;

    public MediaController(ProfileImageStorageService profileImageStorageService) {
        this.profileImageStorageService = profileImageStorageService;
    }

    @GetMapping("/api/media/profiles/{profileId}/{filename}")
    public ResponseEntity<byte[]> getProfileImage(
            @PathVariable String profileId,
            @PathVariable String filename
    ) {
        try {
            Path imagePath = profileImageStorageService.resolve(profileId, filename);
            if (!Files.exists(imagePath)) {
                return ResponseEntity.notFound().build();
            }
            byte[] bytes = Files.readAllBytes(imagePath);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000, immutable")
                    .contentType(resolveContentType(filename))
                    .body(bytes);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private MediaType resolveContentType(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        }
        if (lower.endsWith(".webp")) {
            return MediaType.parseMediaType("image/webp");
        }
        return MediaType.IMAGE_JPEG;
    }
}
