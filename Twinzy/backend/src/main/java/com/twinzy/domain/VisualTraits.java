package com.twinzy.domain;

public class VisualTraits {

    private double brightness;
    private double warmth;
    private double contrast;
    private String estimatedGender;
    private String skinToneBucket;

    public VisualTraits() {
    }

    public VisualTraits(double brightness, double warmth, double contrast, String estimatedGender, String skinToneBucket) {
        this.brightness = brightness;
        this.warmth = warmth;
        this.contrast = contrast;
        this.estimatedGender = estimatedGender;
        this.skinToneBucket = skinToneBucket;
    }

    public double getBrightness() {
        return brightness;
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }

    public double getWarmth() {
        return warmth;
    }

    public void setWarmth(double warmth) {
        this.warmth = warmth;
    }

    public double getContrast() {
        return contrast;
    }

    public void setContrast(double contrast) {
        this.contrast = contrast;
    }

    public String getEstimatedGender() {
        return estimatedGender;
    }

    public void setEstimatedGender(String estimatedGender) {
        this.estimatedGender = estimatedGender;
    }

    public String getSkinToneBucket() {
        return skinToneBucket;
    }

    public void setSkinToneBucket(String skinToneBucket) {
        this.skinToneBucket = skinToneBucket;
    }
}
