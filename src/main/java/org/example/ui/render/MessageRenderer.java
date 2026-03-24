package org.example.ui.render;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.example.analytics.PlotData;

/**
 * Renders non-chart dashboard messages.
 */
public final class MessageRenderer implements PlotRenderer<PlotData.MessagePlotData> {
    @Override
    public Node render(PlotData.MessagePlotData plotData) {
        Label label = new Label(plotData.message());
        label.getStyleClass().add("section-note");
        label.setWrapText(true);
        StackPane pane = new StackPane(label);
        pane.getStyleClass().addAll("app-card", "chart-card");
        pane.setPadding(new Insets(32));
        return pane;
    }
}
