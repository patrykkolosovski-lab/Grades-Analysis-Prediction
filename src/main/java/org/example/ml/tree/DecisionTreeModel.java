package org.example.ml.tree;

import org.example.data.StudentProfile;

/**
 * Wraps a tree root for prediction and inspection.
 */
public record DecisionTreeModel(PredictionNode root) {
    public double predict(StudentProfile profile) {
        return root.predict(profile);
    }
}
