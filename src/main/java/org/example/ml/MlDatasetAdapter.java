package org.example.ml;

import org.example.analytics.FeatureType;
import org.example.data.GradeDataset;
import org.example.data.GradeRow;
import org.example.data.StudentProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Converts dashboard datasets into aligned ML training samples.
 */
public final class MlDatasetAdapter {
    private MlDatasetAdapter() {
    }

    /**
     * Builds aligned samples for one course using the Current dataset and student profiles.
     */
    public static List<MlSample> samplesForCourse(GradeDataset dataset, Map<String, StudentProfile> profiles, String courseName) {
        int courseIndex = dataset.courseIndex(courseName);
        if (courseIndex < 0) {
            throw new IllegalArgumentException("Unknown course: " + courseName);
        }
        List<MlSample> samples = new ArrayList<>();
        for (GradeRow row : dataset.rows()) {
            StudentProfile profile = profiles.get(row.studentId());
            Double target = row.grades().get(courseIndex);
            if (profile != null && target != null) {
                samples.add(new MlSample(profile, target));
            }
        }
        return samples;
    }

    /**
     * Resolves the numeric feature value used by the tree trainer.
     */
    public static double featureValue(StudentProfile profile, FeatureType featureType) {
        return switch (featureType) {
            case QUANTUM_COHERENCE_THRESHOLD -> encode(profile.quantumCoherenceThreshold(), FeatureType.QUANTUM_COHERENCE_THRESHOLD.categories());
            case SYMBIOTIC_NETWORK_COMPATIBILITY -> encode(profile.symbioticNetworkCompatibility(), FeatureType.SYMBIOTIC_NETWORK_COMPATIBILITY.categories());
            case ASTRO_TEMPORAL_DRIFT_RESISTANCE -> encode(profile.astroTemporalDriftResistance(), FeatureType.ASTRO_TEMPORAL_DRIFT_RESISTANCE.categories());
            case PSIONIC_INTERFERENCE_TOLERANCE -> profile.psionicInterferenceTolerance();
            case BIO_LUMINAL_TRANSMISSION -> encode(profile.bioLuminalTransmission(), FeatureType.BIO_LUMINAL_TRANSMISSION.categories());
            case NONE -> 0.0;
        };
    }

    private static double encode(String value, List<String> categories) {
        int index = categories.indexOf(value.trim());
        return index < 0 ? -1.0 : index;
    }
}
