package com.twinzy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.twinzy.dto.ErrorResponseDto;
import com.twinzy.dto.ProfileDetailResponseDto;
import com.twinzy.dto.ProfileMatchDto;
import com.twinzy.dto.ProfileSummaryDto;
import com.twinzy.dto.RelatedProfileDto;
import com.twinzy.dto.SearchResponseDto;
import com.twinzy.model.StoredProfile;
import com.twinzy.repository.ProfileRepository;
import com.twinzy.service.SearchSessionService;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileRepository profileRepository;
    private final SearchSessionService searchSessionService;

    public ProfileController(ProfileRepository profileRepository, SearchSessionService searchSessionService) {
        this.profileRepository = profileRepository;
        this.searchSessionService = searchSessionService;
    }

    @GetMapping
    public ResponseEntity<List<ProfileSummaryDto>> listProfiles(
            @RequestParam(value = "funnyOnly", required = false) Boolean funnyOnly
    ) {
        List<StoredProfile> profiles = profileRepository.findAll().stream()
                .filter(profile -> {
                    if (funnyOnly == null) {
                        return true;
                    }
                    return funnyOnly ? profile.isFunnyObject() : !profile.isFunnyObject();
                })
                .toList();

        return ResponseEntity.ok(profiles.stream()
                .map(profile -> new ProfileSummaryDto(
                        profile,
                        profile.getImages().stream()
                                .filter(image -> image.isPrimary())
                                .map(image -> image.getUrl())
                                .findFirst()
                                .orElse(profile.getImages().isEmpty() ? null : profile.getImages().get(0).getUrl())
                ))
                .toList());
    }

    @GetMapping("/slugs")
    public ResponseEntity<List<String>> slugs() {
        return ResponseEntity.ok(profileRepository.findAllSlugs());
    }

    @GetMapping("/{slug}")
    public ResponseEntity<?> getProfile(
            @PathVariable String slug,
            @RequestParam(value = "sessionId", required = false) String sessionId
    ) {
        StoredProfile profile = profileRepository.findBySlug(slug).orElse(null);
        if (profile == null) {
            return ResponseEntity.status(404).body(new ErrorResponseDto("Profile not found."));
        }

        ProfileDetailResponseDto response = new ProfileDetailResponseDto();
        response.setProfile(profile);
        response.setRelated(profileRepository.findRelated(profile.getId(), 6).stream()
                .map(result -> new RelatedProfileDto(result.profile(), result.similarity()))
                .toList());

        if (sessionId != null) {
            SearchResponseDto session = searchSessionService.getSession(sessionId);
            if (session != null) {
                ProfileMatchDto match = session.getHumanMatches().stream()
                        .filter(item -> item.getProfile().getSlug().equals(slug))
                        .findFirst()
                        .orElse(null);
                response.setMatch(match);
            }
        }

        return ResponseEntity.ok(response);
    }
}
