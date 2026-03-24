package org.example.ui.render;

import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import org.example.analytics.PlotData;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Renders paired course grades as a scatter plot.
 */
public final class ScatterRenderer implements PlotRenderer<PlotData.ScatterPlotData> {
    @Override
    public Node render(PlotData.ScatterPlotData plotData) {
        ScatterChart<Number, Number> chart = new ScatterChart<>(axis(plotData.xCourse()), axis(plotData.yCourse()));
        chart.setTitle(plotData.xCourse() + " vs " + plotData.yCourse());
        chart.setLegendVisible(false);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        Map<String, Integer> frequencyMap = new LinkedHashMap<>();
        int maxFrequency = 1;
        for (PlotData.PointValue point : plotData.points()) {
            String key = point.x() + "," + point.y();
            int frequency = frequencyMap.getOrDefault(key, 0) + 1;
            frequencyMap.put(key, frequency);
            maxFrequency = Math.max(maxFrequency, frequency);
        }

        final int finalMaxFrequency = maxFrequency;
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            String[] coordinates = entry.getKey().split(",");
            double x = Double.parseDouble(coordinates[0]);
            double y = Double.parseDouble(coordinates[1]);
            int frequency = entry.getValue();
            XYChart.Data<Number, Number> point = new XYChart.Data<>(x, y);
            point.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    double radius = 5 + (10.0 * frequency / finalMaxFrequency);
                    newNode.setStyle("-fx-background-radius: " + radius + "px; -fx-padding: " + radius + "px;");
                }
            });
            series.getData().add(point);
        }
        chart.getData().add(series);
        return ChartContainerFactory.wrap(chart);
    }

    private NumberAxis axis(String label) {
        NumberAxis axis = new NumberAxis(0, 10.5, 1);
        axis.setLabel(label);
        return axis;
    }
}
