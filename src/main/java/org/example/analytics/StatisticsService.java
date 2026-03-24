package org.example.analytics;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides grade aggregation helpers used by the dashboard and ML smoke tests.
 */
public final class StatisticsService {
    private StatisticsService() {
    }

    public static int[] histogram(List<Double> values) {
        int[] counts = new int[10];
        for (Double value : values) {
            if (value == null) {
                continue;
            }
            int index = Math.max(0, Math.min(9, (int) Math.floor(value) - 1));
            counts[index]++;
        }
        return counts;
    }

    public static double mean(List<Double> values) {
        if (values.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Double value : values) {
            sum += value;
        }
        return sum / values.size();
    }

    public static List<Double> withoutNulls(List<Double> values) {
        List<Double> filtered = new ArrayList<>();
        for (Double value : values) {
            if (value != null) {
                filtered.add(value);
            }
        }
        return filtered;
    }
}
