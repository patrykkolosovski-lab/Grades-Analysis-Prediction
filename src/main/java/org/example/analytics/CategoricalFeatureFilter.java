package org.example.analytics;

import org.example.data.StudentProfile;

/**
 * Filters student profiles by an exact categorical feature value.
 */
public record CategoricalFeatureFilter(FeatureType featureType, String expectedValue) implements FeatureFilter {
    @Override
    public boolean matches(StudentProfile profile) {
        if (profile == null) {
            return false;
        }
        return switch (featureType) {
            case QUANTUM_COHERENCE_THRESHOLD -> profile.quantumCoherenceThreshold().equalsIgnoreCase(expectedValue);
            case SYMBIOTIC_NETWORK_COMPATIBILITY -> profile.symbioticNetworkCompatibility().equalsIgnoreCase(expectedValue);
            case ASTRO_TEMPORAL_DRIFT_RESISTANCE -> profile.astroTemporalDriftResistance().equalsIgnoreCase(expectedValue);
            case BIO_LUMINAL_TRANSMISSION -> profile.bioLuminalTransmission().equalsIgnoreCase(expectedValue);
            default -> false;
        };
    }

    @Override
    public String description() {
        return featureType.label() + " = " + expectedValue;
    }
}
