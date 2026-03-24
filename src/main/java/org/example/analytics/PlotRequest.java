package org.example.analytics;

import org.example.data.DatasetType;

import java.util.List;

/**
 * Captures the dashboard selection state used to produce chart data.
 */
public record PlotRequest(
        DatasetType datasetType,
        PlotType plotType,
        List<String> selectedCourses,
        FeatureFilter featureFilter
) {
}
