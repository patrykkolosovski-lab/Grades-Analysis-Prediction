package org.example.ui;

import javafx.scene.Parent;
import org.example.data.AppData;
import org.example.ml.ForestModelConfig;
import org.example.ml.ForestModelService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Coordinates the Grade Prediction page and its forest-backed actions.
 */
public final class GradePredictionController {
    private final GradePredictionView view;
    private final ForestModelService forestModelService;

    public GradePredictionController(AppData appData) {
        this.view = new GradePredictionView();
        this.forestModelService = new ForestModelService(appData, ForestModelConfig.DEFAULT);
        bind();
        loadOptions();
    }

    public Parent root() {
        return view;
    }

    private void bind() {
        view.predictButton().setOnAction(event -> predict());
        view.evaluateButton().setOnAction(event -> evaluate());
    }

    private void loadOptions() {
        view.setCourseOptions(forestModelService.courseNames());
        List<String> studentIds = new ArrayList<>(forestModelService.studentIds());
        studentIds.sort(Comparator.naturalOrder());
        view.setStudentOptions(studentIds);
    }

    private void predict() {
        String subject = view.selectedPredictionSubject();
        String studentId = view.selectedStudentId();
        if (subject == null || subject.isBlank()) {
            view.setStatus("Select a subject before predicting.");
            return;
        }
        if (studentId == null || studentId.isBlank()) {
            view.setStatus("Enter a student ID before predicting.");
            return;
        }

        try {
            ForestModelService.PredictionResult result = forestModelService.predict(studentId, subject);
            view.setPredictionOutput(
                    formatNumber(result.predictedGrade()),
                    formatNullable(result.actualGrade()),
                    formatNullable(result.squaredError())
            );
            view.setStatus("Prediction computed using forest defaults.");
        } catch (RuntimeException exception) {
            view.setPredictionOutput("N/A", "N/A", "N/A");
            view.setStatus(exception.getMessage());
        }
    }

    private void evaluate() {
        String subject = view.selectedEvaluationSubject();
        if (subject == null || subject.isBlank()) {
            view.setStatus("Select a subject before evaluating.");
            return;
        }

        try {
            ForestModelService.EvaluationResult result = forestModelService.evaluate(subject);
            view.setEvaluationOutput(formatNumber(result.mse()));
            view.setStatus("Forest MSE computed on a deterministic 80/20 holdout split.");
        } catch (RuntimeException exception) {
            view.setEvaluationOutput("N/A");
            view.setStatus(exception.getMessage());
        }
    }

    private String formatNumber(double value) {
        return String.format("%.4f", value);
    }

    private String formatNullable(Double value) {
        return value == null ? "N/A" : formatNumber(value);
    }
}
