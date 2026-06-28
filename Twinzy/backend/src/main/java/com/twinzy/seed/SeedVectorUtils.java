package com.twinzy.seed;

import java.util.ArrayList;
import java.util.List;

import com.twinzy.model.FeatureBreakdown;

public final class SeedVectorUtils {

    private SeedVectorUtils() {
    }

    public static int hashString(String value) {
        int hash = 0;
        for (int i = 0; i < value.length(); i++) {
            hash = (hash * 31 + value.charAt(i)) % 2147483647;
        }
        return hash;
    }

    public static List<Double> seededVector(int seed, int dimensions) {
        List<Double> vector = new ArrayList<>(dimensions);
        int state = seed;
        for (int i = 0; i < dimensions; i++) {
            state = (state * 1664525 + 1013904223) % 2147483647;
            vector.add((state / 2147483647.0) * 2 - 1);
        }
        double magnitude = 0;
        for (double value : vector) {
            magnitude += value * value;
        }
        magnitude = Math.sqrt(magnitude);
        if (magnitude == 0) {
            return vector;
        }
        List<Double> normalized = new ArrayList<>(dimensions);
        for (double value : vector) {
            normalized.add(value / magnitude);
        }
        return normalized;
    }

    public static FeatureBreakdown seededBreakdown(int seed) {
        FeatureBreakdown breakdown = new FeatureBreakdown();
        breakdown.setFaceShape(nextScore(seed, 1));
        breakdown.setEyes(nextScore(seed, 2));
        breakdown.setEyebrows(nextScore(seed, 3));
        breakdown.setNose(nextScore(seed, 4));
        breakdown.setLips(nextScore(seed, 5));
        breakdown.setSmile(nextScore(seed, 6));
        breakdown.setForehead(nextScore(seed, 7));
        breakdown.setJawline(nextScore(seed, 8));
        breakdown.setHairline(nextScore(seed, 9));
        return breakdown;
    }

    private static double nextScore(int seed, int offset) {
        return 70 + ((seed + offset) % 31);
    }

    public static String slugify(String value) {
        return value
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}
