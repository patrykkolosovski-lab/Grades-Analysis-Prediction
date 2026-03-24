package org.example.data;

/**
 * Enumerates the datasets exposed by the application.
 */
public enum DatasetType {
    CURRENT("Current grades"),
    GRADUATE("Graduate grades");

    private final String label;

    DatasetType(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
