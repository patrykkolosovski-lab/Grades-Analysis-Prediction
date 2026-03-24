package org.example.ui.render;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import org.example.analytics.PlotData;

import java.util.Map;

/**
 * Renders one histogram chart per selected course.
 */
public final class HistogramRenderer implements PlotRenderer<PlotData.HistogramPlotData> {
    @Override
    public Node render(PlotData.HistogramPlotData plotData) {
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        int index = 0;
        for (Map.Entry<String, int[]> entry : plotData.countsByCourse().entrySet()) {
            grid.add(ChartContainerFactory.wrap(createChart(entry.getKey(), entry.getValue())), index % 2, index / 2);
            index++;
        }
        return grid;
    }

    private BarChart<String, Number> createChart(String title, int[] counts) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
        xAxis.setLabel("Grade");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Students");
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(title);
        chart.setLegendVisible(false);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < counts.length; i++) {
            series.getData().add(new XYChart.Data<>(String.valueOf(i + 1), counts[i]));
        }
        chart.getData().add(series);
        return chart;
    }
}
