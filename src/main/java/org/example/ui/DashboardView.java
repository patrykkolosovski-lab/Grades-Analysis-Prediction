package org.example.ui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import org.example.analytics.ComparisonOperator;
import org.example.analytics.FeatureType;
import org.example.analytics.PlotType;
import org.example.data.DatasetType;

import java.util.List;

/**
 * Defines the dashboard layout and exposes the interactive controls.
 */
public final class DashboardView extends BorderPane {
    private final Label pageTitleLabel = new Label("Data Analysis");
    private final ComboBox<DatasetType> datasetComboBox = new ComboBox<>();
    private final ComboBox<PlotType> plotTypeComboBox = new ComboBox<>();
    private final Spinner<Integer> maxCoursesSpinner = new Spinner<>(1, 4, 2);
    private final ListView<String> courseListView = new ListView<>();
    private final ComboBox<FeatureType> featureTypeComboBox = new ComboBox<>();
    private final ComboBox<String> categoricalValueComboBox = new ComboBox<>();
    private final ComboBox<ComparisonOperator> comparisonOperatorComboBox = new ComboBox<>();
    private final TextField thresholdField = new TextField();
    private final Label filterHelpLabel = new Label();
    private final Label statusLabel = new Label("Ready.");
    private final GridPane resultsGrid = new GridPane();
    private final Button renderButton = new Button("Render");
    private final Button clearButton = new Button("Clear selection");

    public DashboardView() {
        getStyleClass().add("data-analysis-root");
        setPadding(new Insets(12));
        setTop(buildTitle());
        setLeft(buildSidebar());
        setCenter(buildResults());
        setBottom(buildStatusBar());
        configureControls();
    }

    public ComboBox<DatasetType> datasetComboBox() {
        return datasetComboBox;
    }

    public ComboBox<PlotType> plotTypeComboBox() {
        return plotTypeComboBox;
    }

    public Spinner<Integer> maxCoursesSpinner() {
        return maxCoursesSpinner;
    }

    public ListView<String> courseListView() {
        return courseListView;
    }

    public ComboBox<FeatureType> featureTypeComboBox() {
        return featureTypeComboBox;
    }

    public ComboBox<String> categoricalValueComboBox() {
        return categoricalValueComboBox;
    }

    public ComboBox<ComparisonOperator> comparisonOperatorComboBox() {
        return comparisonOperatorComboBox;
    }

    public TextField thresholdField() {
        return thresholdField;
    }

    public Label filterHelpLabel() {
        return filterHelpLabel;
    }

    public GridPane resultsGrid() {
        return resultsGrid;
    }

    public Button renderButton() {
        return renderButton;
    }

    public Button clearButton() {
        return clearButton;
    }

    public void setCourses(List<String> courses) {
        courseListView.setItems(FXCollections.observableArrayList(courses));
        courseListView.getSelectionModel().clearSelection();
    }

    public List<String> selectedCourses() {
        return courseListView.getSelectionModel().getSelectedItems();
    }

    public void setStatus(String text) {
        statusLabel.setText(text);
    }

    public void setFilterControlsVisible(boolean categorical, boolean numeric) {
        categoricalValueComboBox.setManaged(categorical);
        categoricalValueComboBox.setVisible(categorical);
        comparisonOperatorComboBox.setManaged(numeric);
        comparisonOperatorComboBox.setVisible(numeric);
        thresholdField.setManaged(numeric);
        thresholdField.setVisible(numeric);
    }

    public void showResult(Node node) {
        resultsGrid.getChildren().clear();
        resultsGrid.add(node, 0, 0);
        GridPane.setHgrow(node, Priority.ALWAYS);
        GridPane.setVgrow(node, Priority.ALWAYS);
    }

    private Node buildSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.getStyleClass().add("analysis-sidebar");
        sidebar.setPadding(new Insets(0, 12, 0, 0));
        sidebar.setPrefWidth(340);

        VBox datasetBox = new VBox(8,
                createSectionLabel("Dataset"),
                datasetComboBox,
                createSectionLabel("Maximum courses"),
                maxCoursesSpinner,
                createSectionLabel("Courses"),
                courseListView
        );

        VBox filterBox = new VBox(8,
                createSectionLabel("Feature filter"),
                featureTypeComboBox,
                comparisonOperatorComboBox,
                thresholdField,
                categoricalValueComboBox,
                filterHelpLabel
        );

        VBox plotBox = new VBox(8,
                createSectionLabel("Plot type"),
                plotTypeComboBox,
                renderButton,
                clearButton
        );
        datasetBox.getStyleClass().addAll("app-card", "control-card");
        filterBox.getStyleClass().addAll("app-card", "control-card");
        plotBox.getStyleClass().addAll("app-card", "control-card");

        sidebar.getChildren().addAll(datasetBox, new Separator(), filterBox, new Separator(), plotBox);
        ScrollPane scrollPane = new ScrollPane(sidebar);
        scrollPane.getStyleClass().add("analysis-scroll");
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPannable(true);
        return scrollPane;
    }

    private Node buildTitle() {
        HBox titleBar = new HBox(pageTitleLabel);
        titleBar.setAlignment(Pos.CENTER);
        titleBar.setMaxWidth(Double.MAX_VALUE);
        titleBar.getStyleClass().add("analysis-title-bar");
        configureLargeTitle(pageTitleLabel);
        HBox.setHgrow(pageTitleLabel, Priority.ALWAYS);
        return titleBar;
    }

    private Node buildResults() {
        resultsGrid.getStyleClass().add("analysis-results");
        resultsGrid.setHgap(12);
        resultsGrid.setVgap(12);
        resultsGrid.setAlignment(Pos.TOP_LEFT);
        return resultsGrid;
    }

    private Node buildStatusBar() {
        HBox bar = new HBox(statusLabel);
        bar.getStyleClass().add("status-bar");
        statusLabel.getStyleClass().add("status-label");
        bar.setPadding(new Insets(8, 0, 0, 0));
        return bar;
    }

    private void configureControls() {
        datasetComboBox.getItems().setAll(DatasetType.values());
        plotTypeComboBox.getItems().setAll(PlotType.values());
        featureTypeComboBox.getItems().setAll(FeatureType.values());
        comparisonOperatorComboBox.getItems().setAll(ComparisonOperator.values());
        datasetComboBox.getStyleClass().add("app-input");
        plotTypeComboBox.getStyleClass().add("app-input");
        featureTypeComboBox.getStyleClass().add("app-input");
        categoricalValueComboBox.getStyleClass().add("app-input");
        comparisonOperatorComboBox.getStyleClass().add("app-input");
        thresholdField.getStyleClass().add("app-input");
        courseListView.getStyleClass().add("app-list");
        maxCoursesSpinner.getStyleClass().add("spinner-ghost");
        renderButton.getStyleClass().addAll("app-button", "primary-button");
        clearButton.getStyleClass().addAll("app-button", "secondary-button");
        courseListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        courseListView.setPrefHeight(280);
        datasetComboBox.getSelectionModel().select(DatasetType.CURRENT);
        plotTypeComboBox.getSelectionModel().select(PlotType.HISTOGRAM);
        featureTypeComboBox.getSelectionModel().select(FeatureType.NONE);
        comparisonOperatorComboBox.getSelectionModel().select(ComparisonOperator.GREATER_THAN_OR_EQUAL);
        thresholdField.setPromptText("Numeric threshold");
        categoricalValueComboBox.setPromptText("Value");
        filterHelpLabel.setWrapText(true);
        filterHelpLabel.getStyleClass().add("section-note");
        renderButton.setMaxWidth(Double.MAX_VALUE);
        clearButton.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(courseListView, Priority.ALWAYS);
        setFilterControlsVisible(false, false);
    }

    private Label createSectionLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("section-label");
        return label;
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
