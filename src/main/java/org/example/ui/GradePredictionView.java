package org.example.ui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.List;

/**
 * Defines the Grade Prediction section UI.
 */
public final class GradePredictionView extends VBox {
    private final Label pageTitleLabel = new Label("Grade Prediction");
    private final Label noteLabel = new Label("Use the prediction panel to predict a specific student's grade in a subject.");
    private final ComboBox<String> predictionSubjectComboBox = new ComboBox<>();
    private final ComboBox<String> studentComboBox = new ComboBox<>();
    private final Button predictButton = new Button("Predict");
    private final Label predictedGradeValue = new Label("N/A");
    private final Label actualGradeValue = new Label("N/A");
    private final Label squaredErrorValue = new Label("N/A");

    private final ComboBox<String> evaluationSubjectComboBox = new ComboBox<>();
    private final Button evaluateButton = new Button("Evaluate");
    private final Label mseValue = new Label("N/A");
    private final Label statusLabel = new Label("Ready.");
    private final HBox panelRow = new HBox(18);

    public GradePredictionView() {
        getStyleClass().add("grade-prediction-root");
        setSpacing(16);
        setPadding(new Insets(12));
        setAlignment(Pos.CENTER);
        setFillWidth(true);
        configureLargeTitle(pageTitleLabel);
        noteLabel.getStyleClass().add("section-note");
        noteLabel.setWrapText(true);
        noteLabel.setMaxWidth(Double.MAX_VALUE);
        noteLabel.setAlignment(Pos.CENTER);
        panelRow.getStyleClass().add("prediction-panel-row");
        panelRow.setAlignment(Pos.CENTER);
        studentComboBox.setPromptText("Select student ID");
        predictionSubjectComboBox.setPromptText("Select subject");
        evaluationSubjectComboBox.setPromptText("Select subject");
        predictionSubjectComboBox.getStyleClass().add("app-input");
        studentComboBox.getStyleClass().add("app-input");
        evaluationSubjectComboBox.getStyleClass().add("app-input");
        predictButton.getStyleClass().addAll("app-button", "primary-button");
        evaluateButton.getStyleClass().addAll("app-button", "primary-button");
        predictButton.setMaxWidth(Double.MAX_VALUE);
        evaluateButton.setMaxWidth(Double.MAX_VALUE);
        predictedGradeValue.getStyleClass().add("result-value");
        actualGradeValue.getStyleClass().add("result-value");
        squaredErrorValue.getStyleClass().add("result-value");
        mseValue.getStyleClass().add("result-value");
        statusLabel.getStyleClass().add("status-label");
        panelRow.getChildren().addAll(buildPredictionPanel(), buildEvaluationPanel());
        getChildren().addAll(pageTitleLabel, noteLabel, panelRow, statusLabel);
    }

    public ComboBox<String> predictionSubjectComboBox() {
        return predictionSubjectComboBox;
    }

    public ComboBox<String> studentComboBox() {
        return studentComboBox;
    }

    public Button predictButton() {
        return predictButton;
    }

    public ComboBox<String> evaluationSubjectComboBox() {
        return evaluationSubjectComboBox;
    }

    public Button evaluateButton() {
        return evaluateButton;
    }

    public void setCourseOptions(List<String> courseNames) {
        predictionSubjectComboBox.setItems(FXCollections.observableArrayList(courseNames));
        evaluationSubjectComboBox.setItems(FXCollections.observableArrayList(courseNames));
        if (!courseNames.isEmpty()) {
            predictionSubjectComboBox.getSelectionModel().selectFirst();
            evaluationSubjectComboBox.getSelectionModel().selectFirst();
        }
    }

    public void setStudentOptions(List<String> studentIds) {
        studentComboBox.setItems(FXCollections.observableArrayList(studentIds));
        if (!studentIds.isEmpty()) {
            studentComboBox.getSelectionModel().selectFirst();
        }
    }

    public String selectedPredictionSubject() {
        return predictionSubjectComboBox.getValue();
    }

    public String selectedStudentId() {
        return studentComboBox.getValue();
    }

    public String selectedEvaluationSubject() {
        return evaluationSubjectComboBox.getValue();
    }

    public void setPredictionOutput(String predictedGrade, String actualGrade, String squaredError) {
        predictedGradeValue.setText(predictedGrade);
        actualGradeValue.setText(actualGrade);
        squaredErrorValue.setText(squaredError);
    }

    public void setEvaluationOutput(String mse) {
        mseValue.setText(mse);
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    private Node buildPredictionPanel() {
        GridPane panel = createPanel("Prediction");
        Label subjectLabel = createFieldLabel("Subject");
        Label studentLabel = createFieldLabel("Student");
        Label predictedLabel = createFieldLabel("Predicted grade");
        Label actualLabel = createFieldLabel("Actual grade");
        Label squaredLabel = createFieldLabel("Squared error");
        panel.addRow(1, subjectLabel, predictionSubjectComboBox);
        panel.addRow(2, studentLabel, studentComboBox);
        panel.add(predictButton, 0, 3, 2, 1);
        panel.addRow(4, predictedLabel, predictedGradeValue);
        panel.addRow(5, actualLabel, actualGradeValue);
        panel.addRow(6, squaredLabel, squaredErrorValue);
        return panel;
    }

    private Node buildEvaluationPanel() {
        GridPane panel = createPanel("Evaluation");
        Label subjectLabel = createFieldLabel("Subject");
        Label mseLabel = createFieldLabel("MSE");
        panel.addRow(1, subjectLabel, evaluationSubjectComboBox);
        panel.add(evaluateButton, 0, 2, 2, 1);
        panel.addRow(3, mseLabel, mseValue);
        return panel;
    }

    private GridPane createPanel(String title) {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("app-card");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(14));
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");
        grid.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHgrow(grid, Priority.ALWAYS);
        grid.setMaxWidth(380);
        grid.setMinWidth(320);
        return grid;
    }

    private Label createFieldLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().addAll("field-label", "section-label");
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
