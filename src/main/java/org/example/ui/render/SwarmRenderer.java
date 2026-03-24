package org.example.ui.render;

import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.util.StringConverter;
import org.example.analytics.PlotData;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Renders the legacy swarm point-display behavior by collapsing equal grades
 * per course and scaling marker size by frequency.
 */
public final class SwarmRenderer implements PlotRenderer<PlotData.SwarmPlotData> {
    @Override
    public Node render(PlotData.SwarmPlotData plotData) {
        int courseCount = plotData.courses().size();
        NumberAxis xAxis = new NumberAxis(0.5, courseCount + 0.5, 1.0);
        xAxis.setLabel("Course");
        xAxis.setMinorTickVisible(false);
        xAxis.setAutoRanging(false);
        xAxis.setTickLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Number object) {
                int index = (int) Math.round(object.doubleValue()) - 1;
                if (index < 0 || index >= plotData.courses().size()) {
                    return "";
                }
                return plotData.courses().get(index).courseName();
            }

            @Override
            public Number fromString(String string) {
                return 0;
            }
        });

        NumberAxis yAxis = new NumberAxis(0, 10.5, 1);
        yAxis.setLabel("Grade");

        ScatterChart<Number, Number> chart = new ScatterChart<>(xAxis, yAxis);
        chart.setTitle("Swarm plot");
        chart.setLegendVisible(false);

        int globalMaxFrequency = 1;
        for (PlotData.SwarmCourseValues course : plotData.courses()) {
            Map<Double, Integer> frequencyByGrade = new LinkedHashMap<>();
            for (Double grade : course.grades()) {
                frequencyByGrade.put(grade, frequencyByGrade.getOrDefault(grade, 0) + 1);
                globalMaxFrequency = Math.max(globalMaxFrequency, frequencyByGrade.get(grade));
            }
        }

        for (int courseIndex = 0; courseIndex < plotData.courses().size(); courseIndex++) {
            PlotData.SwarmCourseValues course = plotData.courses().get(courseIndex);
            Map<Double, Integer> frequencyByGrade = new LinkedHashMap<>();
            for (Double grade : course.grades()) {
                frequencyByGrade.put(grade, frequencyByGrade.getOrDefault(grade, 0) + 1);
            }

            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            double xCenter = courseIndex + 1;
            final int maxFrequency = globalMaxFrequency;
            for (Map.Entry<Double, Integer> entry : frequencyByGrade.entrySet()) {
                XYChart.Data<Number, Number> point = new XYChart.Data<>(xCenter, entry.getKey());
                int frequency = entry.getValue();
                point.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        double radius = 5 + (10.0 * frequency / maxFrequency);
                        newNode.setStyle("-fx-background-radius: " + radius + "px; -fx-padding: " + radius + "px;");
                    }
                });
                series.getData().add(point);
            }
            chart.getData().add(series);
        }

        return ChartContainerFactory.wrap(chart);
    }
}
