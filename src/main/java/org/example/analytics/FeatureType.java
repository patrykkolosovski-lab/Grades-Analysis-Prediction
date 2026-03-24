package org.example.analytics;

import java.util.List;

/**
 * Enumerates the supported feature filters for student profile data.
 */
public enum FeatureType {
    NONE("No filter", false, List.of()),
    QUANTUM_COHERENCE_THRESHOLD("Quantum Coherence Threshold", true, List.of("Stable", "Fractured", "Chaotic", "Coherent", "Resonant")),
    SYMBIOTIC_NETWORK_COMPATIBILITY("Symbiotic Network Compatibility", true, List.of("None", "Harmonized")),
    ASTRO_TEMPORAL_DRIFT_RESISTANCE("Astro-Temporal Drift Resistance", true, List.of("1 ns/mc", "2 ns/mc", "3 ns/mc")),
    PSIONIC_INTERFERENCE_TOLERANCE("Psionic Interference Tolerance", false, List.of()),
    BIO_LUMINAL_TRANSMISSION("Bio-Luminal Transmission", true, List.of("Silver", "Crimson", "White-Blue", "Violet", "Turquiose"));

    private final String label;
    private final boolean categorical;
    private final List<String> categories;

    FeatureType(String label, boolean categorical, List<String> categories) {
        this.label = label;
        this.categorical = categorical;
        this.categories = categories;
    }

    public String label() {
        return label;
    }

    public boolean isCategorical() {
        return categorical;
    }

    public List<String> categories() {
        return categories;
    }

    @Override
    public String toString() {
        return label;
    }
}
