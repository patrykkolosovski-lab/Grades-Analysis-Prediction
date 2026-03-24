package org.example.ml.tree;

import org.example.data.StudentProfile;

/**
 * Defines a tree node capable of predicting a grade from a student profile.
 */
public sealed interface PredictionNode permits LeafNode, DecisionNode {
    double predict(StudentProfile profile);
}
