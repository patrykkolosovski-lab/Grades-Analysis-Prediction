package org.example.ui.render;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * Wraps rendered charts in a themed container shared across the GUI.
 */
public final class ChartContainerFactory {
    private ChartContainerFactory() {
    }

    public static StackPane wrap(Node content) {
        StackPane pane = new StackPane(content);
        pane.getStyleClass().addAll("app-card", "chart-card");
        pane.setPadding(new Insets(10));
        return pane;
    }
}
