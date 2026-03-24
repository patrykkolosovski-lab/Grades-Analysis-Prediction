package org.example.ml;

import org.example.data.AppData;
import org.example.data.CsvDatasetRepository;

/**
 * Provides a one-call entrypoint for training a forest and predicting a grade
 * for a specific student and course on the Current dataset.
 */
public final class ForestPredictionService {
    private ForestPredictionService() {
    }

    /**
     * Trains a forest on the Current dataset and predicts the selected student's
     * grade for the selected course using default forest settings.
     */
    public static ForestPrediction predictCurrentStudentCourse(String studentId, String courseName) throws Exception {
        return predictCurrentStudentCourse(studentId, courseName, ForestModelConfig.DEFAULT);
    }

    /**
     * Trains a forest on the Current dataset and predicts the selected student's
     * grade for the selected course using caller-provided forest settings.
     */
    public static ForestPrediction predictCurrentStudentCourse(
            String studentId,
            String courseName,
            int treeCount,
            int maxDepth,
            int minSamplesLeaf,
            long seed
    ) throws Exception {
        return predictCurrentStudentCourse(studentId, courseName, new ForestModelConfig(treeCount, maxDepth, minSamplesLeaf, seed));
    }

    /**
     * Trains a forest on the Current dataset and predicts the selected student's
     * grade for the selected course using the provided forest configuration.
     */
    public static ForestPrediction predictCurrentStudentCourse(
            String studentId,
            String courseName,
            ForestModelConfig config
    ) throws Exception {
        AppData appData = new CsvDatasetRepository().load();
        ForestModelService.PredictionResult result = new ForestModelService(appData, config).predict(studentId, courseName);
        return new ForestPrediction(
                result.studentId(),
                result.courseName(),
                result.predictedGrade(),
                result.actualGrade(),
                config.treeCount(),
                config.seed()
        );
    }

    /**
     * Holds the prediction payload returned by the convenience API.
     */
    public record ForestPrediction(
            String studentId,
            String courseName,
            double predictedGrade,
            Double actualGrade,
            int treeCount,
            long seed
    ) {
    }
}
