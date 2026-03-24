package org.example.ml.tree;

import org.example.data.StudentProfile;

/**
 * Represents a terminal prediction node.
 */
public record LeafNode(double prediction, int sampleCount) implements PredictionNode {
    @Override
    public double predict(StudentProfile profile) {
        return prediction;
    }
}
