package org.example.GUI;

public class filePaths {
    private static final String PATH_GRAD = "src/main/resources/GraduateGrades.csv";
    private static final String PATH_CURR = "src/main/resources/CurrentGrades.csv";
    private static final String StudentInformation = "src/main/resources/StudentInfo.csv";

    public static String getPathGrad() {
        return PATH_GRAD;
    }
    public static String getPathCurr() {
        return PATH_CURR;
    }
    public static String getStudentInformation() {
        return StudentInformation;
    }
}
