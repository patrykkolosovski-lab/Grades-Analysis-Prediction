package org.example.GUI;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class VisualizationPane extends BorderPane {
    private final Label placeholderLabel;
    private final Label titleLabel;

    public VisualizationPane(String title) {
        setPadding(new Insets(10));

        titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        setTop(titleLabel);
        BorderPane.setMargin(titleLabel, new Insets(0, 0, 8, 0));

        placeholderLabel = new Label("No visualisation yet.");
        placeholderLabel.setWrapText(true);
        StackPane center = new StackPane(placeholderLabel);
        center.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: #fcfcfc;");
        setCenter(center);
    }

    public void setPlaceholderText(String text) {
        placeholderLabel.setText(text);
        setCenter(new StackPane(placeholderLabel));
    }

    public void setContent(Node node) {
        if (node == null) {
            setCenter(new StackPane(placeholderLabel));
        } else {
            setCenter(node);
        }
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }
}