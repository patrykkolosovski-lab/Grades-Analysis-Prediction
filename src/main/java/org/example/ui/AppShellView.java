package org.example.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Defines the top-level application shell and section navigation.
 */
public final class AppShellView extends BorderPane {
    private final Button backButton = new Button("Back");
    private final Button dataAnalysisButton = new Button("Data Analysis");
    private final Button gradePredictionButton = new Button("Grade Prediction");
    private final StackPane contentPane = new StackPane();
    private final VBox homePane = new VBox(20);
    private final Label homeTitleLabel = new Label("Grade Dashboard");
    private final Label subtitleLabel = new Label("Welcome!");
    private final StackPane headerPane = new StackPane();

    public AppShellView() {
        getStyleClass().add("app-shell");
        setPadding(new Insets(16));
        buildHeader();
        setTop(headerPane);
        setCenter(contentPane);
        contentPane.getStyleClass().add("app-content");
        dataAnalysisButton.setPrefSize(260, 100);
        gradePredictionButton.setPrefSize(260, 100);
        backButton.getStyleClass().addAll("app-button", "secondary-button");
        configureLargeTitle(homeTitleLabel);
        dataAnalysisButton.getStyleClass().addAll("app-button", "hero-button", "primary-button");
        gradePredictionButton.getStyleClass().addAll("app-button", "hero-button", "primary-button");
        homePane.getStyleClass().add("home-pane");
        homePane.setAlignment(Pos.CENTER);
        homePane.setFillWidth(true);
        homePane.setMaxWidth(Double.MAX_VALUE);
        subtitleLabel.getStyleClass().add("app-subtitle");
        homePane.getChildren().addAll(homeTitleLabel, subtitleLabel, dataAnalysisButton, gradePredictionButton);
    }

    public Button backButton() {
        return backButton;
    }

    public Button dataAnalysisButton() {
        return dataAnalysisButton;
    }

    public Button gradePredictionButton() {
        return gradePredictionButton;
    }

    public void showHome() {
        headerPane.setVisible(false);
        headerPane.setManaged(false);
        backButton.setVisible(false);
        backButton.setManaged(false);
        contentPane.getChildren().setAll(homePane);
    }

    public void showSection(String title, Node content) {
        headerPane.setVisible(true);
        headerPane.setManaged(true);
        backButton.setVisible(true);
        backButton.setManaged(true);
        contentPane.getChildren().setAll(content);
    }

    private void buildHeader() {
        headerPane.getStyleClass().add("app-header");
        headerPane.setPadding(new Insets(0, 0, 16, 0));
        headerPane.setMaxWidth(Double.MAX_VALUE);
        backButton.setVisible(false);
        backButton.setManaged(false);
        HBox backButtonRow = new HBox(backButton);
        backButtonRow.setAlignment(Pos.CENTER_LEFT);
        backButtonRow.setMaxWidth(Double.MAX_VALUE);
        StackPane.setAlignment(backButtonRow, Pos.CENTER_LEFT);
        headerPane.getChildren().add(backButtonRow);
    }

    private void configureLargeTitle(Label label) {
        label.getStyleClass().add("page-title");
        label.setFont(Font.font("Helvetica Neue", FontWeight.BLACK, 300));
        label.setTextFill(Color.BLACK);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setWrapText(true);
    }

}
