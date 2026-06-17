package com.twinzy.controller;

import java.io.IOException;
import java.util.Base64;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.twinzy.dto.ErrorResponseDto;
import com.twinzy.dto.SearchResponseDto;
import com.twinzy.service.SearchSessionService;
import com.twinzy.service.SimilarityService;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SimilarityService similarityService;
    private final SearchSessionService searchSessionService;

    public SearchController(SimilarityService similarityService, SearchSessionService searchSessionService) {
        this.similarityService = similarityService;
        this.searchSessionService = searchSessionService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> search(@RequestParam("image") MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto("Image file is required."));
        }

        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto("Only image uploads are supported."));
        }

        if (image.getSize() > 10 * 1024 * 1024) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto("Image must be under 10MB."));
        }

        SearchResponseDto result = similarityService.search(image.getBytes());
        String contentTypeSafe = contentType != null ? contentType : "image/jpeg";
        String base64 = Base64.getEncoder().encodeToString(image.getBytes());
        result.setUserImageDataUrl("data:" + contentTypeSafe + ";base64," + base64);
        String sessionId = searchSessionService.createSession(result);
        result.setSessionId(sessionId);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<?> getSession(@RequestParam("sessionId") String sessionId) {
        SearchResponseDto session = searchSessionService.getSession(sessionId);
        if (session == null) {
            return ResponseEntity.status(404).body(new ErrorResponseDto("Session not found or expired."));
        }
        session.setSessionId(sessionId);
        return ResponseEntity.ok(session);
    }
}
