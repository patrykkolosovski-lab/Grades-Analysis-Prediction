package org.example.analytics;

/**
 * Enumerates supported numeric comparison operators.
 */
public enum ComparisonOperator {
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUAL(">="),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL("<=");

    private final String label;

    ComparisonOperator(String label) {
        this.label = label;
    }

    public boolean matches(double left, double right) {
        return switch (this) {
            case GREATER_THAN -> left > right;
            case GREATER_THAN_OR_EQUAL -> left >= right;
            case LESS_THAN -> left < right;
            case LESS_THAN_OR_EQUAL -> left <= right;
        };
    }

    @Override
    public String toString() {
        return label;
    }
}
