package org.example.analytics;

import java.util.List;
import java.util.Map;

/**
 * Represents chart-ready analytics output.
 */
public sealed interface PlotData permits PlotData.HistogramPlotData, PlotData.GroupedBarPlotData, PlotData.ScatterPlotData, PlotData.SwarmPlotData, PlotData.JointPlotData, PlotData.MissingGradesPlotData, PlotData.MessagePlotData {
    /**
     * Holds per-course histogram counts.
     */
    record HistogramPlotData(Map<String, int[]> countsByCourse) implements PlotData {
    }

    /**
     * Holds grouped bar-chart counts.
     */
    record GroupedBarPlotData(Map<String, int[]> countsByCourse) implements PlotData {
    }

    /**
     * Holds scatter plot coordinates.
     */
    record ScatterPlotData(String xCourse, String yCourse, List<PointValue> points) implements PlotData {
    }

    /**
     * Holds course-grade values for swarm rendering.
     */
    record SwarmPlotData(List<SwarmCourseValues> courses) implements PlotData {
    }

    /**
     * Holds scatter coordinates plus marginal histograms for a joint plot.
     */
    record JointPlotData(String xCourse, String yCourse, List<PointValue> points, int[] xHistogram, int[] yHistogram) implements PlotData {
    }

    /**
     * Holds missing-grade counts per course.
     */
    record MissingGradesPlotData(Map<String, Integer> missingByCourse) implements PlotData {
    }

    /**
     * Holds a non-chart message for the dashboard.
     */
    record MessagePlotData(String message) implements PlotData {
    }

    /**
     * Represents a single 2D point.
     */
    record PointValue(double x, double y) {
    }

    /**
     * Holds all valid grades for one course in swarm rendering.
     */
    record SwarmCourseValues(String courseName, List<Double> grades) {
    }
}
