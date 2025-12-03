package org.example;

import org.example.tools.Splitter;
import org.example.tools.dataChooser;

public class phase2Handler{
    public String[][] CurrentGrades;
    public String[][] StudentInfo;
    public int courseColumn;
    public String dataFeature;

    public String[] chosenData;

    public phase2Handler(String[][] CurrentGrades, String[][] StudentInfo, int courseColumn){
        this.CurrentGrades = CurrentGrades;
        this.StudentInfo = StudentInfo;
        this.courseColumn = courseColumn;
    }

    public void setChosenData(){
        dataChooser datachooser;
        if (dataFeature!=null) {
            datachooser = new dataChooser(CurrentGrades, StudentInfo, courseColumn, dataFeature);
        } else {
            datachooser = new dataChooser(CurrentGrades, StudentInfo, courseColumn);
        }
        this.chosenData = datachooser.getStudentGrades();
    }

    public void setChosenDataWithDataFeature(String feature){
        this.dataFeature = feature;
        setChosenData();
    }

    public int[][] getSplitData(String feature){
        int[][] splitData = new int[2][];
        Splitter filterer = new Splitter(feature);
        splitData[0] = filterer.getStudentIndexWithFeature(StudentInfo);
        splitData[1] = filterer.getStudentIndexWithNotFeature(StudentInfo);
        return splitData;
    }

    public int[][] getGradeSplitData(double numericFeature) {
        int[][] splitData = new int[2][];
        Splitter filterer = new Splitter(numericFeature, courseColumn);
        splitData[0] = filterer.getNumericFeature(CurrentGrades);
        splitData[1] = filterer.getNotNumericFeature(CurrentGrades);
        return splitData;
    }

    public int[][] getAverageGradeSplitData(double numericFeature) {
        int[][] splitData = new int[2][];
        Splitter filterer = new Splitter(numericFeature, courseColumn);
        splitData[0] = filterer.getGradesAboveAverage(CurrentGrades, numericFeature);
        splitData[1] = filterer.getGradesBelowAverage(CurrentGrades, numericFeature);
        return splitData;
    }

    public int[][] getVarianceGradeSplitData(double numericFeature) {
        int[][] splitData = new int[2][];
        Splitter filterer = new Splitter(numericFeature, courseColumn);
        splitData[0] = filterer.getGradesAboveVariance(CurrentGrades, numericFeature);
        splitData[1] = filterer.getGradesBelowVariance(CurrentGrades, numericFeature);
        return splitData;
    }

    public int[][] getStandardDeviationGradeSplitData(double numericFeature) {
        int[][] splitData = new int[2][];
        Splitter filterer = new Splitter(numericFeature, courseColumn);
        splitData[0] = filterer.getGradesAboveStandardDeviation(CurrentGrades, numericFeature);
        splitData[1] = filterer.getGradesBelowStandardDeviation(CurrentGrades, numericFeature);
        return splitData;
    }

    public int[][] getRangeGradeSplitData(double numericFeature) {
        int[][] splitData = new int[2][];
        Splitter filterer = new Splitter(numericFeature, courseColumn);
        splitData[0] = filterer.getGradesAboveRange(CurrentGrades, numericFeature);
        splitData[1] = filterer.getGradesBelowRange(CurrentGrades, numericFeature);
        return splitData;
    }

    public int[][] getNGSplitData() {
        int[][] splitData = new int[2][];
        Splitter filterer = new Splitter("NG");
        splitData[0] = filterer.getNgGrades(chosenData);
        splitData[1] = filterer.getNotNGGrades(chosenData);
        return splitData;
    }
}