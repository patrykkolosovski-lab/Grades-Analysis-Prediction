package org.example.GUI;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class featureInfo {
    // Filter feature names (first scroll)
    private static final String FEATURE_PSIONIC = "Psionic Interference Tolerance";
    private static final String FEATURE_QCT = "Quantum Coherence Threshold";
    private static final String FEATURE_SYMBIOTIC = "Symbiotic Network Compatibility";
    private static final String FEATURE_ATDR = "Astro-Temporal Drift Resistance";
    private static final String FEATURE_BLT = "Bio-Luminal Transmission";
    private static final String FEATURE_NG = "NG";

    // All feature options for the first scroll
    private static final String[] FILTER_FEATURES = {
            FEATURE_PSIONIC,
            FEATURE_QCT,
            FEATURE_SYMBIOTIC,
            FEATURE_ATDR,
            FEATURE_BLT,
            FEATURE_NG

    };

    // Possible categorical values per feature (for second scroll)
    private static final Map<String, List<String>> CATEGORICAL_VALUES = new LinkedHashMap<>();
    static {
        CATEGORICAL_VALUES.put(FEATURE_QCT, Arrays.asList(
                "Stable", "Fractured", "Chaotic", "Coherent", "Resonant"
        ));
        CATEGORICAL_VALUES.put(FEATURE_SYMBIOTIC, Arrays.asList(
                "None", "Harmonized"
        ));
        CATEGORICAL_VALUES.put(FEATURE_ATDR, Arrays.asList(
                "1 ns/mc", "2 ns/mc", "3 ns/mc"
        ));
        CATEGORICAL_VALUES.put(FEATURE_BLT, Arrays.asList(
                "Silver", "Crimson", "White-Blue", "Violet"
        ));
    }

    public static String getFeaturePsionic() {
        return FEATURE_PSIONIC;
    }
    public static String getFeatureQCT() {
        return FEATURE_QCT;
    }
    public static String getFeatureSymbiotic() {
        return FEATURE_SYMBIOTIC;
    }
    public static String getFeatureAtdr() {
        return FEATURE_ATDR;
    }
    public static String getFeatureBlt() {
        return FEATURE_BLT;
    }
    public static String getFeatureNg() {
        return FEATURE_NG;
    }
    public static String[] getFilterFeatures() {
        return FILTER_FEATURES;
    }
    public static Map<String, List<String>> getCategoricalValues() {
        return CATEGORICAL_VALUES;
    }

}
