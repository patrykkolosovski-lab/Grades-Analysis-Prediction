package org.example.ml;

/**
 * Runs a forest prediction for a student and course using the default forest settings.
 */
public final class ForestPredictionMain {
    private static final String DEFAULT_STUDENT_ID = "312804";
    private static final String DEFAULT_COURSE_NAME = "Cryogenic Physics";

    private ForestPredictionMain() {
    }

    public static void main(String[] args) throws Exception {
        String studentId = args.length > 0 ? args[0] : DEFAULT_STUDENT_ID;
        String courseName = args.length > 1 ? args[1] : DEFAULT_COURSE_NAME;

        ForestPredictionService.ForestPrediction prediction =
                ForestPredictionService.predictCurrentStudentCourse(studentId, courseName);

        System.out.println("Student ID: " + prediction.studentId());
        System.out.println("Course: " + prediction.courseName());
        System.out.println("Predicted grade: " + prediction.predictedGrade());
        System.out.println("Actual grade: " + prediction.actualGrade());
        System.out.println("Tree count: " + prediction.treeCount());
        System.out.println("Seed: " + prediction.seed());
    }
}
