package com.twinzy.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.twinzy.domain.FaceAnalysisResult;
import com.twinzy.domain.VisualTraits;
import com.twinzy.model.FeatureBreakdown;

@Service
public class FaceAnalysisService {

    private final ImageAnalyzer imageAnalyzer;

    public FaceAnalysisService(ImageAnalyzer imageAnalyzer) {
        this.imageAnalyzer = imageAnalyzer;
    }

    public FaceAnalysisResult analyze(byte[] imageBytes) {
        ImageAnalyzer.PixelStats stats = imageAnalyzer.analyzePixels(imageBytes);
        int hash = hashBuffer(imageBytes);
        List<Double> embedding = imageAnalyzer.buildEmbedding(imageBytes, stats);
        VisualTraits traits = imageAnalyzer.deriveTraits(stats, hash);

        FaceAnalysisResult result = new FaceAnalysisResult();
        result.setEmbedding(embedding);
        result.setTraits(traits);
        result.setFeatures(buildFeatures(stats, hash));
        result.setLandmarks(buildLandmarks(hash));
        return result;
    }

    private FeatureBreakdown buildFeatures(ImageAnalyzer.PixelStats stats, int hash) {
        FeatureBreakdown breakdown = new FeatureBreakdown();
        int base = (int) Math.round(stats.brightness() * 100);
        breakdown.setFaceShape(clamp(base + (hash % 11) - 5));
        breakdown.setEyes(clamp(base + ((hash / 3) % 11) - 3));
        breakdown.setEyebrows(clamp(base + ((hash / 5) % 9) - 2));
        breakdown.setNose(clamp(base + ((hash / 7) % 11) - 4));
        breakdown.setLips(clamp(base + ((hash / 11) % 9) - 1));
        breakdown.setSmile(clamp(base + ((hash / 13) % 10)));
        breakdown.setForehead(clamp(base + ((hash / 17) % 8) - 2));
        breakdown.setJawline(clamp(base + ((hash / 19) % 10) - 3));
        breakdown.setHairline(clamp(base + ((hash / 23) % 7) - 1));
        return breakdown;
    }

    private Map<String, double[]> buildLandmarks(int hash) {
        Map<String, double[]> landmarks = new HashMap<>();
        double offset = (hash % 100) / 1000.0;
        landmarks.put("leftEye", new double[] { 0.35 + offset, 0.4 });
        landmarks.put("rightEye", new double[] { 0.65 + offset, 0.4 });
        landmarks.put("nose", new double[] { 0.5, 0.55 + offset });
        landmarks.put("mouthLeft", new double[] { 0.4, 0.7 });
        landmarks.put("mouthRight", new double[] { 0.6, 0.7 });
        return landmarks;
    }

    private double clamp(double value) {
        return Math.max(62, Math.min(98, value));
    }

    private int hashBuffer(byte[] buffer) {
        int hash = 0;
        for (byte value : buffer) {
            hash = (hash * 31 + (value & 0xff)) % 2147483647;
        }
        return hash;
    }
}
