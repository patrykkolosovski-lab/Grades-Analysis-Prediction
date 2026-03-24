package org.example.ui.render;

import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.example.analytics.PlotData;

import java.util.Map;

/**
 * Renders missing-grade counts per course.
 */
public final class MissingGradesRenderer implements PlotRenderer<PlotData.MissingGradesPlotData> {
    @Override
    public Node render(PlotData.MissingGradesPlotData plotData) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Course");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Missing grades");
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Missing grades");
        chart.setLegendVisible(false);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : plotData.missingByCourse().entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        chart.getData().add(series);
        return ChartContainerFactory.wrap(chart);
    }
}
