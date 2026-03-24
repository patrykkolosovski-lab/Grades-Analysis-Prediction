package org.example.analytics;

/**
 * Enumerates the supported dashboard plot types.
 */
public enum PlotType {
    HISTOGRAM("Histogram"),
    DISTRIBUTION_BAR("Distribution bar"),
    SCATTER("Scatter"),
    SWARM("Swarm"),
    JOINT("Joint"),
    MISSING_GRADES("Missing grades");

    private final String label;

    PlotType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
