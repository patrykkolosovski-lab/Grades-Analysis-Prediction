package org.example.analytics;

import org.example.data.StudentProfile;

/**
 * Defines a profile-based filter used by the analytics pipeline.
 */
public sealed interface FeatureFilter permits NoFilter, CategoricalFeatureFilter, NumericThresholdFilter {
    boolean matches(StudentProfile profile);

    String description();

    default boolean isActive() {
        return !(this instanceof NoFilter);
    }
}
