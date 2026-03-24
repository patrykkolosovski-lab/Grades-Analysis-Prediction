package org.example.ui.render;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.analytics.PlotData;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Renders a scatter plot with marginal histograms positioned directly against
 * the plot area instead of as separate bar charts.
 */
public final class JointRenderer implements PlotRenderer<PlotData.JointPlotData> {
    @Override
    public Node render(PlotData.JointPlotData plotData) {
        return new JointPlotPane(plotData);
    }

    private ScatterChart<Number, Number> createScatter(PlotData.JointPlotData plotData) {
        ScatterChart<Number, Number> chart = new ScatterChart<>(axis(plotData.xCourse()), axis(plotData.yCourse()));
        chart.setTitle(null);
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
        return chart;
    }

    private NumberAxis axis(String label) {
        NumberAxis axis = new NumberAxis(0, 10.5, 1);
        axis.setLabel(label);
        return axis;
    }

    private static final class JointPlotPane extends Region {
        private static final double TOP_HISTOGRAM_HEIGHT = 84;
        private static final double RIGHT_HISTOGRAM_WIDTH = 84;
        private static final double GAP = 8;
        private static final Color BAR_FILL = Color.web("#092c63");

        private final PlotData.JointPlotData plotData;
        private final JointRenderer renderer = new JointRenderer();
        private final ScatterChart<Number, Number> scatterChart;
        private final Pane topHistogramPane = new Pane();
        private final Pane rightHistogramPane = new Pane();

        private JointPlotPane(PlotData.JointPlotData plotData) {
            this.plotData = plotData;
            this.scatterChart = renderer.createScatter(plotData);
            getStyleClass().addAll("app-card", "chart-card");
            topHistogramPane.setManaged(false);
            rightHistogramPane.setManaged(false);
            getChildren().addAll(scatterChart, topHistogramPane, rightHistogramPane);
            setMinSize(520, 420);
            setPrefSize(820, 620);
        }

        @Override
        protected void layoutChildren() {
            double width = getWidth();
            double height = getHeight();

            double chartWidth = Math.max(320, width - RIGHT_HISTOGRAM_WIDTH - GAP);
            double chartHeight = Math.max(260, height - TOP_HISTOGRAM_HEIGHT - GAP);

            scatterChart.resizeRelocate(0, TOP_HISTOGRAM_HEIGHT + GAP, chartWidth, chartHeight);
            scatterChart.applyCss();
            scatterChart.layout();

            Node plotArea = scatterChart.lookup(".chart-plot-background");
            if (plotArea == null) {
                double fallbackWidth = chartWidth - 110;
                double fallbackHeight = chartHeight - 90;
                topHistogramPane.resizeRelocate(70, 0, Math.max(120, fallbackWidth), TOP_HISTOGRAM_HEIGHT);
                rightHistogramPane.resizeRelocate(chartWidth + GAP, TOP_HISTOGRAM_HEIGHT + GAP + 20, RIGHT_HISTOGRAM_WIDTH, Math.max(120, fallbackHeight));
            } else {
                Bounds bounds = plotArea.getBoundsInParent();
                double topX = scatterChart.getLayoutX() + bounds.getMinX();
                double topY = scatterChart.getLayoutY() + bounds.getMinY() - TOP_HISTOGRAM_HEIGHT - 2;
                topHistogramPane.resizeRelocate(topX, Math.max(0, topY), bounds.getWidth(), TOP_HISTOGRAM_HEIGHT);

                double rightX = scatterChart.getLayoutX() + bounds.getMaxX() + 2;
                double rightY = scatterChart.getLayoutY() + bounds.getMinY();
                rightHistogramPane.resizeRelocate(rightX, rightY, RIGHT_HISTOGRAM_WIDTH, bounds.getHeight());
            }

            drawTopHistogram();
            drawRightHistogram();
        }

        @Override
        protected double computePrefWidth(double height) {
            return 820;
        }

        @Override
        protected double computePrefHeight(double width) {
            return 620;
        }

        private void drawTopHistogram() {
            topHistogramPane.getChildren().clear();
            int[] counts = plotData.xHistogram();
            int maxCount = maxCount(counts);
            double width = topHistogramPane.getWidth();
            double height = topHistogramPane.getHeight();
            if (width <= 0 || height <= 0) {
                return;
            }

            double barWidth = width / counts.length;
            for (int i = 0; i < counts.length; i++) {
                double scaledHeight = maxCount == 0 ? 0 : (counts[i] / (double) maxCount) * height;
                Rectangle bar = new Rectangle(
                        i * barWidth,
                        height - scaledHeight,
                        Math.max(1, barWidth - 2),
                        scaledHeight
                );
                styleBar(bar);
                topHistogramPane.getChildren().add(bar);
            }
        }

        private void drawRightHistogram() {
            rightHistogramPane.getChildren().clear();
            int[] counts = plotData.yHistogram();
            int maxCount = maxCount(counts);
            double width = rightHistogramPane.getWidth();
            double height = rightHistogramPane.getHeight();
            if (width <= 0 || height <= 0) {
                return;
            }

            double barHeight = height / counts.length;
            for (int i = 0; i < counts.length; i++) {
                double scaledWidth = maxCount == 0 ? 0 : (counts[i] / (double) maxCount) * width;
                Rectangle bar = new Rectangle(
                        0,
                        (counts.length - 1 - i) * barHeight,
                        scaledWidth,
                        Math.max(1, barHeight - 2)
                );
                styleBar(bar);
                rightHistogramPane.getChildren().add(bar);
            }
        }

        private void styleBar(Rectangle bar) {
            bar.setArcWidth(2);
            bar.setArcHeight(2);
            bar.setFill(BAR_FILL);
        }

        private int maxCount(int[] counts) {
            int max = 0;
            for (int count : counts) {
                max = Math.max(max, count);
            }
            return max;
        }
    }
}
