package org.example;

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
        Splitter splitter = new Splitter(feature);
        splitData[0] = splitter.getStudentIndexWithFeature(StudentInfo);
        splitData[1] = splitter.getStudentIndexWithNotFeature(StudentInfo);
        return splitData;
    }

    public int[][] getGradeSplitData(double numericFeature) {
        int[][] splitData = new int[2][];
        Splitter splitter = new Splitter(numericFeature, courseColumn);
        splitData[0] = splitter.getNumericFeature(CurrentGrades);
        splitData[1] = splitter.getNotNumericFeature(CurrentGrades);
        return splitData;
    }
}