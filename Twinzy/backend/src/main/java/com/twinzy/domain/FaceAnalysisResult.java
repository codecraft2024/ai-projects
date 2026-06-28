package com.twinzy.domain;

import java.util.List;
import java.util.Map;

import com.twinzy.model.FeatureBreakdown;

public class FaceAnalysisResult {

    private List<Double> embedding;
    private FeatureBreakdown features;
    private VisualTraits traits;
    private Map<String, double[]> landmarks;

    public List<Double> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(List<Double> embedding) {
        this.embedding = embedding;
    }

    public FeatureBreakdown getFeatures() {
        return features;
    }

    public void setFeatures(FeatureBreakdown features) {
        this.features = features;
    }

    public VisualTraits getTraits() {
        return traits;
    }

    public void setTraits(VisualTraits traits) {
        this.traits = traits;
    }

    public Map<String, double[]> getLandmarks() {
        return landmarks;
    }

    public void setLandmarks(Map<String, double[]> landmarks) {
        this.landmarks = landmarks;
    }
}
