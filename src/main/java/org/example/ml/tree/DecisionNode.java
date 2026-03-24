package org.example.ml.tree;

import org.example.analytics.FeatureType;
import org.example.data.StudentProfile;
import org.example.ml.MlDatasetAdapter;

/**
 * Represents a binary split on one encoded profile feature.
 */
public record DecisionNode(
        FeatureType featureType,
        double threshold,
        PredictionNode left,
        PredictionNode right,
        double fallbackPrediction
) implements PredictionNode {
    @Override
    public double predict(StudentProfile profile) {
        if (profile == null) {
            return fallbackPrediction;
        }
        double value = MlDatasetAdapter.featureValue(profile, featureType);
        return value >= threshold ? left.predict(profile) : right.predict(profile);
    }
}
