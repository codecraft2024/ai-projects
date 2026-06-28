package com.twinzy.matching;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.twinzy.analysis.FaceAnalysisService;
import com.twinzy.analysis.ImageAnalyzer;
import com.twinzy.config.TwinzyProperties;
import com.twinzy.domain.FaceAnalysisResult;
import com.twinzy.domain.VisualTraits;
import com.twinzy.dto.ProfileMatchDto;
import com.twinzy.dto.SearchResponseDto;
import com.twinzy.model.FeatureBreakdown;
import com.twinzy.model.StoredProfile;
import com.twinzy.repository.ProfileRepository;
import com.twinzy.repository.ProfileRepository.RankedProfile;
import com.twinzy.similarity.FeatureWeights;

@Service
public class MatchingEngine {

    private final FaceAnalysisService faceAnalysisService;
    private final ImageAnalyzer imageAnalyzer;
    private final ProfileRepository profileRepository;
    private final TwinzyProperties properties;

    public MatchingEngine(
            FaceAnalysisService faceAnalysisService,
            ImageAnalyzer imageAnalyzer,
            ProfileRepository profileRepository,
            TwinzyProperties properties
    ) {
        this.faceAnalysisService = faceAnalysisService;
        this.imageAnalyzer = imageAnalyzer;
        this.profileRepository = profileRepository;
        this.properties = properties;
    }

    public SearchResponseDto search(byte[] imageBytes) {
        FaceAnalysisResult analysis = faceAnalysisService.analyze(imageBytes);
        double[] userEmbedding = FeatureWeights.toArray(analysis.getEmbedding());

        List<StoredProfile> funnyPool = profileRepository.findAllFunny();
        if (funnyPool.isEmpty()) {
            throw new IllegalStateException("No funny object profiles found in dataset.");
        }

        StoredProfile funnyProfile = funnyPool.get(Math.floorMod(hashImage(imageBytes), funnyPool.size()));
        ProfileMatchDto funnyMatch = buildMatch(analysis, funnyProfile, true);

        List<ProfileMatchDto> humanMatches = profileRepository
                .findTopK(userEmbedding, properties.getMatchCandidatePool(), false)
                .stream()
                .map(RankedProfile::profile)
                .map(profile -> buildMatch(analysis, profile, false))
                .sorted(Comparator.comparingDouble(ProfileMatchDto::getOverallScore).reversed())
                .limit(properties.getMatchLimit())
                .toList();

        SearchResponseDto response = new SearchResponseDto();
        response.setFunnyMatch(funnyMatch);
        response.setHumanMatches(humanMatches);
        return response;
    }

    private ProfileMatchDto buildMatch(FaceAnalysisResult analysis, StoredProfile profile, boolean funny) {
        VisualTraits profileTraits = imageAnalyzer.traitsFromProfile(
                profile.getGender(),
                profile.getEthnicityCategory(),
                profile.getHairColor(),
                profile.getEyeColor()
        );

        double score = MatchScorer.score(analysis, profile, profile.getStoredFeatures(), profileTraits);
        FeatureBreakdown breakdown = MatchScorer.blendedFeatures(analysis, profile.getStoredFeatures());

        String reason = funny
                ? "Your face shape and proportions resemble this " + profile.getFullName().toLowerCase() + "."
                : "You are " + score + "% similar to " + profile.getFullName()
                        + " based on facial structure, visual traits, and feature alignment.";

        return new ProfileMatchDto(profile, score, breakdown, reason);
    }

    public ProfileMatchDto buildSessionMatch(FaceAnalysisResult analysis, StoredProfile profile) {
        return buildMatch(analysis, profile, profile.isFunnyObject());
    }

    private int hashImage(byte[] imageBytes) {
        int hash = 0;
        for (byte value : imageBytes) {
            hash = (hash * 31 + (value & 0xff)) % 2147483647;
        }
        return hash;
    }
}
