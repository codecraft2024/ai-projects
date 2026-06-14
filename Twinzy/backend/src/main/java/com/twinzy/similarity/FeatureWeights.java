package com.twinzy.similarity;

import java.util.LinkedHashMap;
import java.util.Map;

import com.twinzy.model.FeatureBreakdown;

public final class FeatureWeights {

    private static final Map<String, Double> WEIGHTS = Map.of(
            "faceShape", 0.15,
            "eyes", 0.18,
            "eyebrows", 0.08,
            "nose", 0.14,
            "lips", 0.12,
            "smile", 0.10,
            "forehead", 0.08,
            "jawline", 0.10,
            "hairline", 0.05
    );

    private FeatureWeights() {
    }

    public static double computeOverallScore(FeatureBreakdown breakdown) {
        double score = breakdown.getFaceShape() * WEIGHTS.get("faceShape")
                + breakdown.getEyes() * WEIGHTS.get("eyes")
                + breakdown.getEyebrows() * WEIGHTS.get("eyebrows")
                + breakdown.getNose() * WEIGHTS.get("nose")
                + breakdown.getLips() * WEIGHTS.get("lips")
                + breakdown.getSmile() * WEIGHTS.get("smile")
                + breakdown.getForehead() * WEIGHTS.get("forehead")
                + breakdown.getJawline() * WEIGHTS.get("jawline")
                + breakdown.getHairline() * WEIGHTS.get("hairline");
        return Math.round(score * 10.0) / 10.0;
    }

    public static double cosineSimilarity(double[] a, double[] b) {
        if (a.length != b.length || a.length == 0) {
            return 0;
        }
        double dot = 0;
        double magA = 0;
        double magB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            magA += a[i] * a[i];
            magB += b[i] * b[i];
        }
        double magnitude = Math.sqrt(magA) * Math.sqrt(magB);
        return magnitude == 0 ? 0 : dot / magnitude;
    }

    public static double embeddingToPercent(double similarity) {
        double normalized = Math.max(0, Math.min(1, (similarity + 1) / 2));
        return Math.round(normalized * 1000.0) / 10.0;
    }

    public static FeatureBreakdown blendFeatures(FeatureBreakdown user, FeatureBreakdown stored) {
        FeatureBreakdown blended = new FeatureBreakdown();
        blended.setFaceShape(roundOneDecimal(user.getFaceShape() * 0.4 + stored.getFaceShape() * 0.6));
        blended.setEyes(roundOneDecimal(user.getEyes() * 0.4 + stored.getEyes() * 0.6));
        blended.setEyebrows(roundOneDecimal(user.getEyebrows() * 0.4 + stored.getEyebrows() * 0.6));
        blended.setNose(roundOneDecimal(user.getNose() * 0.4 + stored.getNose() * 0.6));
        blended.setLips(roundOneDecimal(user.getLips() * 0.4 + stored.getLips() * 0.6));
        blended.setSmile(roundOneDecimal(user.getSmile() * 0.4 + stored.getSmile() * 0.6));
        blended.setForehead(roundOneDecimal(user.getForehead() * 0.4 + stored.getForehead() * 0.6));
        blended.setJawline(roundOneDecimal(user.getJawline() * 0.4 + stored.getJawline() * 0.6));
        blended.setHairline(roundOneDecimal(user.getHairline() * 0.4 + stored.getHairline() * 0.6));
        return blended;
    }

    public static double[] toArray(java.util.List<Double> values) {
        if (values == null) {
            return new double[0];
        }
        return values.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private static double roundOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
