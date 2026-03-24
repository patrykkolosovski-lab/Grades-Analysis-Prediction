package org.example.ui.render;

import javafx.scene.Node;
import org.example.analytics.PlotData;

/**
 * Renders one analytics payload into a JavaFX node.
 */
public interface PlotRenderer<T extends PlotData> {
    Node render(T plotData);
}
