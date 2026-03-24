package org.example.analytics;

import org.example.data.StudentProfile;

/**
 * Filters student profiles by a numeric threshold on psionic tolerance.
 */
public record NumericThresholdFilter(
        FeatureType featureType,
        ComparisonOperator operator,
        double threshold
) implements FeatureFilter {
    @Override
    public boolean matches(StudentProfile profile) {
        if (profile == null || featureType != FeatureType.PSIONIC_INTERFERENCE_TOLERANCE) {
            return false;
        }
        return operator.matches(profile.psionicInterferenceTolerance(), threshold);
    }

    @Override
    public String description() {
        return featureType.label() + " " + operator + " " + threshold;
    }
}
