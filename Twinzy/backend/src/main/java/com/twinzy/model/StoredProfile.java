package com.twinzy.model;

public class StoredProfile extends Profile {

    private FeatureBreakdown storedFeatures;

    public FeatureBreakdown getStoredFeatures() {
        return storedFeatures;
    }

    public void setStoredFeatures(FeatureBreakdown storedFeatures) {
        this.storedFeatures = storedFeatures;
    }
}
