package com.twinzy.matching;

import com.twinzy.domain.FaceAnalysisResult;
import com.twinzy.domain.VisualTraits;
import com.twinzy.model.FeatureBreakdown;
import com.twinzy.model.StoredProfile;
import com.twinzy.similarity.FeatureWeights;

public final class MatchScorer {

    private MatchScorer() {
    }

    public static double score(
            FaceAnalysisResult userAnalysis,
            StoredProfile profile,
            FeatureBreakdown storedFeatures,
            VisualTraits profileTraits
    ) {
        double embeddingScore = FeatureWeights.embeddingToPercent(
                FeatureWeights.cosineSimilarity(
                        FeatureWeights.toArray(userAnalysis.getEmbedding()),
                        FeatureWeights.toArray(profile.getFaceEmbedding())
                )
        );

        FeatureBreakdown blended = FeatureWeights.blendFeatures(userAnalysis.getFeatures(), storedFeatures);
        double featureScore = FeatureWeights.computeOverallScore(blended);
        double traitScore = traitAffinity(userAnalysis.getTraits(), profileTraits) * 100;
        double metadataScore = metadataAffinity(userAnalysis.getTraits(), profile) * 100;

        double finalScore = embeddingScore * 0.55
                + featureScore * 0.25
                + traitScore * 0.12
                + metadataScore * 0.08;

        return Math.round(finalScore * 10.0) / 10.0;
    }

    public static FeatureBreakdown blendedFeatures(FaceAnalysisResult userAnalysis, FeatureBreakdown storedFeatures) {
        return FeatureWeights.blendFeatures(userAnalysis.getFeatures(), storedFeatures);
    }

    private static double traitAffinity(VisualTraits user, VisualTraits profile) {
        double brightnessDiff = 1 - Math.min(1, Math.abs(user.getBrightness() - profile.getBrightness()) * 2.5);
        double warmthDiff = 1 - Math.min(1, Math.abs(user.getWarmth() - profile.getWarmth()) * 3);
        double contrastDiff = 1 - Math.min(1, Math.abs(user.getContrast() - profile.getContrast()) * 2);
        double skinMatch = user.getSkinToneBucket().equals(profile.getSkinToneBucket()) ? 1.0 : 0.55;
        return (brightnessDiff * 0.3 + warmthDiff * 0.25 + contrastDiff * 0.2 + skinMatch * 0.25);
    }

    private static double metadataAffinity(VisualTraits user, StoredProfile profile) {
        double genderMatch = profile.getGender().equalsIgnoreCase(user.getEstimatedGender())
                || profile.getGender().equalsIgnoreCase("other")
                ? 1.0
                : 0.35;

        double ethnicityMatch = switch (profile.getEthnicityCategory()) {
            case "Middle Eastern", "South Asian" -> user.getSkinToneBucket().equals("warm") || user.getSkinToneBucket().equals("deep") ? 1.0 : 0.6;
            case "African" -> user.getSkinToneBucket().equals("deep") ? 1.0 : 0.55;
            case "Caucasian" -> user.getSkinToneBucket().equals("light") ? 1.0 : 0.65;
            case "Asian" -> user.getSkinToneBucket().equals("medium") ? 1.0 : 0.7;
            case "Latino" -> user.getSkinToneBucket().equals("warm") ? 1.0 : 0.68;
            default -> 0.75;
        };

        return genderMatch * 0.55 + ethnicityMatch * 0.45;
    }
}
