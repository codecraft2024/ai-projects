package com.twinzy.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.twinzy.model.FeatureBreakdown;
import com.twinzy.model.StoredProfile;
import com.twinzy.similarity.FeatureWeights;

@Repository
public class ProfileRepository {

    private final List<StoredProfile> profiles;

    public ProfileRepository(List<StoredProfile> profiles) {
        this.profiles = List.copyOf(profiles);
    }

    public int count() {
        return profiles.size();
    }

    public List<String> findAllSlugs() {
        return profiles.stream().map(StoredProfile::getSlug).toList();
    }

    public Optional<StoredProfile> findBySlug(String slug) {
        return profiles.stream().filter(profile -> profile.getSlug().equals(slug)).findFirst();
    }

    public List<StoredProfile> findAllFunny() {
        return profiles.stream().filter(StoredProfile::isFunnyObject).toList();
    }

    public List<StoredProfile> findAllHumans() {
        return profiles.stream().filter(profile -> !profile.isFunnyObject()).toList();
    }

    public List<StoredProfile> findAll() {
        return profiles;
    }

    public List<RankedProfile> findTopK(double[] embedding, int limit, boolean funnyOnly) {
        return profiles.stream()
                .filter(profile -> funnyOnly ? profile.isFunnyObject() : !profile.isFunnyObject())
                .map(profile -> new RankedProfile(
                        profile,
                        profile.getStoredFeatures(),
                        FeatureWeights.cosineSimilarity(embedding, FeatureWeights.toArray(profile.getFaceEmbedding()))
                ))
                .sorted(Comparator.comparingDouble(RankedProfile::similarity).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<RankedProfile> findRelated(String profileId, int limit) {
        Optional<StoredProfile> source = profiles.stream()
                .filter(profile -> profile.getId().equals(profileId))
                .findFirst();

        if (source.isEmpty()) {
            return List.of();
        }

        StoredProfile sourceProfile = source.get();
        double[] sourceEmbedding = FeatureWeights.toArray(sourceProfile.getFaceEmbedding());

        return profiles.stream()
                .filter(profile -> !profile.getId().equals(profileId))
                .filter(profile -> !profile.isFunnyObject())
                .filter(profile -> profile.getPublicFigureCategory().equals(sourceProfile.getPublicFigureCategory()))
                .map(profile -> new RankedProfile(
                        profile,
                        profile.getStoredFeatures(),
                        FeatureWeights.cosineSimilarity(sourceEmbedding, FeatureWeights.toArray(profile.getFaceEmbedding()))
                ))
                .sorted(Comparator.comparingDouble(RankedProfile::similarity).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public record RankedProfile(StoredProfile profile, FeatureBreakdown storedFeatures, double similarity) {
    }
}
