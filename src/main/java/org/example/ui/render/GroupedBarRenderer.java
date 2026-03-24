package org.example.ui.render;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.example.analytics.PlotData;

import java.util.Map;

/**
 * Renders grouped grade distributions for multiple courses.
 */
public final class GroupedBarRenderer implements PlotRenderer<PlotData.GroupedBarPlotData> {
    @Override
    public Node render(PlotData.GroupedBarPlotData plotData) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
        xAxis.setLabel("Grade");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Students");
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Grade distributions");
        for (Map.Entry<String, int[]> entry : plotData.countsByCourse().entrySet()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(entry.getKey());
            int[] counts = entry.getValue();
            for (int i = 0; i < counts.length; i++) {
                series.getData().add(new XYChart.Data<>(String.valueOf(i + 1), counts[i]));
            }
            chart.getData().add(series);
        }
        return ChartContainerFactory.wrap(chart);
    }
}
