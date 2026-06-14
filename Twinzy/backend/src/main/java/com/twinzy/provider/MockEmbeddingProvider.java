package com.twinzy.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.twinzy.model.FeatureBreakdown;

@Component
public class MockEmbeddingProvider {

    public record FaceDetectionResult(List<Double> embedding, Map<String, LandmarkPoint> landmarks) {
    }

    public record LandmarkPoint(double x, double y) {
    }

    public FaceDetectionResult detectAndEmbed(byte[] image) {
        int seed = hashBuffer(image);
        return new FaceDetectionResult(seededVector(seed), mockLandmarks(seed));
    }

    public FeatureBreakdown analyzeFeatures(Map<String, LandmarkPoint> landmarks) {
        double seedSum = landmarks.values().stream().mapToDouble(point -> point.x() + point.y()).sum();
        return seededBreakdown((int) Math.floor(seedSum * 1000) % 10000);
    }

    private int hashBuffer(byte[] buffer) {
        int hash = 0;
        for (byte value : buffer) {
            hash = (hash * 31 + (value & 0xff)) % 2147483647;
        }
        return hash;
    }

    private List<Double> seededVector(int seed) {
        double[] vector = new double[512];
        int state = seed;
        for (int i = 0; i < 512; i++) {
            state = (state * 1664525 + 1013904223) % 2147483647;
            vector[i] = (state / 2147483647.0) * 2 - 1;
        }
        double magnitude = 0;
        for (double value : vector) {
            magnitude += value * value;
        }
        magnitude = Math.sqrt(magnitude);
        for (int i = 0; i < vector.length; i++) {
            vector[i] /= magnitude;
        }
        return java.util.Arrays.stream(vector).boxed().toList();
    }

    private FeatureBreakdown seededBreakdown(int seed) {
        FeatureBreakdown breakdown = new FeatureBreakdown();
        breakdown.setFaceShape(next(seed, 1));
        breakdown.setEyes(next(seed, 2));
        breakdown.setEyebrows(next(seed, 3));
        breakdown.setNose(next(seed, 4));
        breakdown.setLips(next(seed, 5));
        breakdown.setSmile(next(seed, 6));
        breakdown.setForehead(next(seed, 7));
        breakdown.setJawline(next(seed, 8));
        breakdown.setHairline(next(seed, 9));
        return breakdown;
    }

    private double next(int seed, int offset) {
        return 70 + ((seed + offset) % 31);
    }

    private Map<String, LandmarkPoint> mockLandmarks(int seed) {
        Map<String, LandmarkPoint> landmarks = new HashMap<>();
        landmarks.put("leftEye", new LandmarkPoint(0.35 + offset(seed, 1), 0.4));
        landmarks.put("rightEye", new LandmarkPoint(0.65 + offset(seed, 2), 0.4));
        landmarks.put("nose", new LandmarkPoint(0.5, 0.55 + offset(seed, 3)));
        landmarks.put("mouthLeft", new LandmarkPoint(0.4, 0.7));
        landmarks.put("mouthRight", new LandmarkPoint(0.6, 0.7));
        return landmarks;
    }

    private double offset(int seed, int n) {
        return (seed % 100) / 1000.0 + n * 0.01;
    }
}
