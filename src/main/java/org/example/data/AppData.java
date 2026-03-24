package org.example.data;

import java.util.Map;

/**
 * Aggregates the datasets and profile data required by the dashboard.
 */
public record AppData(
        GradeDataset currentDataset,
        GradeDataset graduateDataset,
        Map<String, StudentProfile> currentProfiles
) {
    public GradeDataset dataset(DatasetType datasetType) {
        return switch (datasetType) {
            case CURRENT -> currentDataset;
            case GRADUATE -> graduateDataset;
        };
    }

    public boolean supportsFeatureFilters(DatasetType datasetType) {
        return datasetType == DatasetType.CURRENT;
    }
}
