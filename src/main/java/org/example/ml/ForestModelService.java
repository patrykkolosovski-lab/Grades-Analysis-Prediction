package org.example.ml;

import org.example.data.AppData;
import org.example.data.GradeDataset;
import org.example.data.GradeRow;
import org.example.data.StudentProfile;
import org.example.ml.eval.EvaluationService;
import org.example.ml.tree.DecisionTreeTrainer;
import org.example.ml.tree.RandomForestTrainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides forest-backed prediction and evaluation over the Current dataset.
 */
public final class ForestModelService {
    private final AppData appData;
    private final ForestModelConfig config;

    public ForestModelService(AppData appData, ForestModelConfig config) {
        this.appData = appData;
        this.config = config;
    }

    /**
     * Predicts a grade for a Current student and subject.
     */
    public PredictionResult predict(String studentId, String courseName) {
        GradeDataset dataset = appData.currentDataset();
        StudentProfile profile = appData.currentProfiles().get(studentId);
        if (profile == null) {
            throw new IllegalArgumentException("Unknown student ID in Current dataset: " + studentId);
        }

        int courseIndex = dataset.courseIndex(courseName);
        if (courseIndex < 0) {
            throw new IllegalArgumentException("Unknown course in Current dataset: " + courseName);
        }

        List<MlSample> samples = MlDatasetAdapter.samplesForCourse(dataset, appData.currentProfiles(), courseName);
        if (samples.isEmpty()) {
            throw new IllegalStateException("No training samples available for course: " + courseName);
        }

        DecisionTreeTrainer trainer = new DecisionTreeTrainer(config.maxDepth(), config.minSamplesLeaf());
        RandomForestTrainer forestTrainer = new RandomForestTrainer(config.treeCount(), trainer);
        RandomForestTrainer.RandomForestModel forest = forestTrainer.train(samples, config.seed());

        Double actualGrade = findActualGrade(dataset, studentId, courseIndex);
        double predictedGrade = forest.predict(profile);
        Double squaredError = actualGrade == null ? null : square(predictedGrade - actualGrade);

        return new PredictionResult(studentId, courseName, predictedGrade, actualGrade, squaredError, config);
    }

    /**
     * Evaluates the forest MSE for a Current subject using a deterministic holdout split.
     */
    public EvaluationResult evaluate(String courseName) {
        GradeDataset dataset = appData.currentDataset();
        if (dataset.courseIndex(courseName) < 0) {
            throw new IllegalArgumentException("Unknown course in Current dataset: " + courseName);
        }

        List<MlSample> samples = MlDatasetAdapter.samplesForCourse(dataset, appData.currentProfiles(), courseName);
        if (samples.size() < 2) {
            throw new IllegalStateException("Not enough data to evaluate course: " + courseName);
        }

        double mse = new EvaluationService(config).evaluate(samples);
        return new EvaluationResult(courseName, mse, samples.size(), config);
    }

    /**
     * Returns all available Current student IDs for the prediction UI.
     */
    public List<String> studentIds() {
        return new ArrayList<>(appData.currentProfiles().keySet());
    }

    /**
     * Returns all Current course names for the prediction and evaluation UI.
     */
    public List<String> courseNames() {
        return appData.currentDataset().courseNames();
    }

    private Double findActualGrade(GradeDataset dataset, String studentId, int courseIndex) {
        for (GradeRow row : dataset.rows()) {
            if (row.studentId().equals(studentId)) {
                return row.grades().get(courseIndex);
            }
        }
        return null;
    }

    private double square(double value) {
        return value * value;
    }

    /**
     * Holds the prediction output shown in the GUI.
     */
    public record PredictionResult(
            String studentId,
            String courseName,
            double predictedGrade,
            Double actualGrade,
            Double squaredError,
            ForestModelConfig config
    ) {
    }

    /**
     * Holds the evaluation output shown in the GUI.
     */
    public record EvaluationResult(
            String courseName,
            double mse,
            int sampleCount,
            ForestModelConfig config
    ) {
    }
}
