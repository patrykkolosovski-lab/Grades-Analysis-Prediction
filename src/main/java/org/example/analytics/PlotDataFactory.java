package org.example.analytics;

import org.example.data.AppData;
import org.example.data.GradeDataset;
import org.example.data.GradeRow;
import org.example.data.StudentProfile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds chart-ready data from datasets, profile filters, and course selections.
 */
public final class PlotDataFactory {
    /**
     * Produces plot data for the current dashboard request.
     */
    public PlotData create(AppData appData, PlotRequest request) {
        GradeDataset dataset = appData.dataset(request.datasetType());
        List<String> courses = normalizeSelectedCourses(request.selectedCourses(), dataset.courseNames());
        if (courses.isEmpty()) {
            return new PlotData.MessagePlotData("Select at least one course.");
        }

        if (request.featureFilter().isActive() && !appData.supportsFeatureFilters(request.datasetType())) {
            return new PlotData.MessagePlotData("Feature filters are only available for the Current dataset.");
        }

        return switch (request.plotType()) {
            case HISTOGRAM -> createHistogramData(appData, dataset, courses, request.featureFilter());
            case DISTRIBUTION_BAR -> createGroupedBarData(appData, dataset, courses, request.featureFilter());
            case SCATTER -> createScatterData(appData, dataset, courses, request.featureFilter());
            case SWARM -> createSwarmData(appData, dataset, courses, request.featureFilter());
            case JOINT -> createJointData(appData, dataset, courses, request.featureFilter());
            case MISSING_GRADES -> createMissingGradesData(dataset, courses);
        };
    }

    private PlotData createHistogramData(AppData appData, GradeDataset dataset, List<String> courses, FeatureFilter filter) {
        Map<String, int[]> countsByCourse = new LinkedHashMap<>();
        for (String course : courses) {
            countsByCourse.put(course, StatisticsService.histogram(collectFilteredGrades(appData, dataset, course, filter)));
        }
        return new PlotData.HistogramPlotData(countsByCourse);
    }

    private PlotData createGroupedBarData(AppData appData, GradeDataset dataset, List<String> courses, FeatureFilter filter) {
        Map<String, int[]> countsByCourse = new LinkedHashMap<>();
        for (String course : courses) {
            countsByCourse.put(course, StatisticsService.histogram(collectFilteredGrades(appData, dataset, course, filter)));
        }
        return new PlotData.GroupedBarPlotData(countsByCourse);
    }

    private PlotData createScatterData(AppData appData, GradeDataset dataset, List<String> courses, FeatureFilter filter) {
        if (courses.size() != 2) {
            return new PlotData.MessagePlotData("Scatter plots require exactly 2 selected courses.");
        }
        List<PlotData.PointValue> points = collectPairedPoints(appData, dataset, courses.get(0), courses.get(1), filter);
        if (points.isEmpty()) {
            return new PlotData.MessagePlotData("No paired grades were found for the selected courses.");
        }
        return new PlotData.ScatterPlotData(courses.get(0), courses.get(1), points);
    }

    private PlotData createSwarmData(AppData appData, GradeDataset dataset, List<String> courses, FeatureFilter filter) {
        List<PlotData.SwarmCourseValues> courseValues = new ArrayList<>();
        for (String course : courses) {
            List<Double> grades = collectFilteredGrades(appData, dataset, course, filter);
            if (!grades.isEmpty()) {
                courseValues.add(new PlotData.SwarmCourseValues(course, grades));
            }
        }
        if (courseValues.isEmpty()) {
            return new PlotData.MessagePlotData("No grades were found for the selected courses.");
        }
        return new PlotData.SwarmPlotData(courseValues);
    }

    private PlotData createJointData(AppData appData, GradeDataset dataset, List<String> courses, FeatureFilter filter) {
        if (courses.size() != 2) {
            return new PlotData.MessagePlotData("Joint plots require exactly 2 selected courses.");
        }
        List<PlotData.PointValue> points = collectPairedPoints(appData, dataset, courses.get(0), courses.get(1), filter);
        if (points.isEmpty()) {
            return new PlotData.MessagePlotData("No paired grades were found for the selected courses.");
        }
        List<Double> xValues = new ArrayList<>(points.size());
        List<Double> yValues = new ArrayList<>(points.size());
        for (PlotData.PointValue point : points) {
            xValues.add(point.x());
            yValues.add(point.y());
        }
        return new PlotData.JointPlotData(courses.get(0), courses.get(1), points, StatisticsService.histogram(xValues), StatisticsService.histogram(yValues));
    }

    private PlotData createMissingGradesData(GradeDataset dataset, List<String> courses) {
        Map<String, Integer> missing = new LinkedHashMap<>();
        for (String course : courses) {
            int courseIndex = dataset.courseIndex(course);
            int count = 0;
            for (GradeRow row : dataset.rows()) {
                if (row.grades().get(courseIndex) == null) {
                    count++;
                }
            }
            missing.put(course, count);
        }
        return new PlotData.MissingGradesPlotData(missing);
    }

    private List<String> normalizeSelectedCourses(List<String> requested, List<String> available) {
        if (requested == null || requested.isEmpty()) {
            return List.of();
        }
        List<String> normalized = new ArrayList<>();
        for (String course : requested) {
            if (available.contains(course) && !normalized.contains(course)) {
                normalized.add(course);
            }
            if (normalized.size() == 4) {
                break;
            }
        }
        return normalized;
    }

    private List<Double> collectFilteredGrades(AppData appData, GradeDataset dataset, String courseName, FeatureFilter filter) {
        int courseIndex = dataset.courseIndex(courseName);
        List<Double> values = new ArrayList<>();
        for (GradeRow row : dataset.rows()) {
            if (!matchesFilter(appData, dataset, row, filter)) {
                continue;
            }
            Double value = row.grades().get(courseIndex);
            if (value != null) {
                values.add(value);
            }
        }
        return values;
    }

    private List<PlotData.PointValue> collectPairedPoints(AppData appData, GradeDataset dataset, String courseX, String courseY, FeatureFilter filter) {
        int xIndex = dataset.courseIndex(courseX);
        int yIndex = dataset.courseIndex(courseY);
        List<PlotData.PointValue> points = new ArrayList<>();
        for (GradeRow row : dataset.rows()) {
            if (!matchesFilter(appData, dataset, row, filter)) {
                continue;
            }
            Double x = row.grades().get(xIndex);
            Double y = row.grades().get(yIndex);
            if (x != null && y != null) {
                points.add(new PlotData.PointValue(x, y));
            }
        }
        return points;
    }

    private boolean matchesFilter(AppData appData, GradeDataset dataset, GradeRow row, FeatureFilter filter) {
        if (!filter.isActive()) {
            return true;
        }
        if (!appData.supportsFeatureFilters(dataset.type())) {
            return false;
        }
        StudentProfile profile = appData.currentProfiles().get(row.studentId());
        return filter.matches(profile);
    }
}
