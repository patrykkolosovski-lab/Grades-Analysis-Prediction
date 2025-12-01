package org.example;
import java.util.Arrays;

public class FileDisplayer {

    public String[] CourseNamesArray;

    public int countpass = 0;

    public static void main(String[] args) {
        //To run code for GG:java FileDisplayer.java GraduateGrades.csv(write this in terminal)
        //To run code for CG:java FileDIsplayer.java CurrentGrades.csv(write this in terminal)
        //args[0]
        String fileName = "CurrentGrades.csv"; // The name of the file you want to read

        // Preparing objects for phase1step1+2
        dataCollector datacollector = new dataCollector(fileName);
        dataProcessing dataprocessor = new dataProcessing();

        // Testing phase1step1 + 2 methods
        double[] averagegrades = dataprocessor.AverageGrades(datacollector.GraduateGradesArray);
        System.out.println(averagegrades[1] + "\n");

        // Preparing objects for phase1step3
        StudentInformationReader studentinformationreader = new StudentInformationReader("StudentInfo.csv");
        step3 step3Object = new step3();

        // Testing phase1step3 methods
        step3Object.STEP3(datacollector.GraduateGradesArray, studentinformationreader.StudentInfoArray);
        System.out.println();

        // Testing phase1step4 methods
        step3Object.STEP3PART3(datacollector.GraduateGradesArray, studentinformationreader.StudentInfoArray, 0, datacollector.CourseNamesArray);

        // Phase 2 Test
        Phase2(datacollector.GraduateGradesArray, studentinformationreader.StudentInfoArray);
        System.out.println();

        // test Filterer
        Splitter filterer = new Splitter("Stable");
        int[] indexes = filterer.getStudentIndexWithFeature(studentinformationreader.StudentInfoArray);

//        for (int i = 0; i < indexes.length; i++) {
//                System.out.println("Index: " + indexes[i]);
//        }

        // test expanded options
//        for (int i = 0; i < filterer.getGradesAboveAverage(datacollector.GraduateGradesArray, 8).length; i++) {
//            System.out.println(filterer.getGradesAboveAverage(datacollector.GraduateGradesArray, 8)[i]);
//        };

        // Split based on grades test
//        phase2Handler phase2handler = new phase2Handler(datacollector.GraduateGradesArray, studentinformationreader.StudentInfoArray, 0);
//        phase2handler.setChosenData();
//        int[][] test6 = phase2handler.getSplitData("Stable");
//        int[][] test7 = phase2handler.getSplitData("Resonant");
//        int[][] test8 = phase2handler.getSplitData("None");


//        System.out.println("Stable \n\n");
//
//        for (int i = 0; i < test6.length; i++) {
//            System.out.print("{ ");
//            for (int j = 0; j < test6[0].length; j++) {
//                System.out.print(datacollector.GraduateGradesArray[test6[i][j]][0]);
//                System.out.print(", ");
//            }
//            System.out.print("} \n");
//        }
//
//        System.out.println("\n\n\n");
//        System.out.println("Resonant \n\n");
//
//
//        for (int i = 0; i < test7.length; i++) {
//            System.out.print("{ ");
//            for (int j = 0; j < test7[0].length; j++) {
//                System.out.print(datacollector.GraduateGradesArray[test7[i][j]][0]);
//                System.out.print(", ");
//            }
//            System.out.print("} \n");
//        }
//
//        System.out.println();
//
//        System.out.println(datacollector.GraduateGradesArray.length);
//        System.out.println(datacollector.GraduateGradesArray[0].length);
//        System.out.println(test8.length);
//        System.out.println(test8[0].length);

//        for (int i = 0; i < test8.length; i++) {
//            System.out.print("{ ");
//            for (int j = 0; j < test8[0].length; j++) {
//                System.out.print(datacollector.GraduateGradesArray[test8[i][j]][0]);
//                System.out.print(", ");
//            }
//            System.out.print("} \n");
//        }
    }

    //Phase 2
    public static void Phase2(String[][] CurrentGrades,String[][] StudentInfoArray){

        //Course+feature from StudentInfo(if necessary)
        String Feature="Silver"; //set value to null to not check StudentInfo for a feature
        int Course=0;

        dataChooser test1=new dataChooser(CurrentGrades,StudentInfoArray,Course,Feature);
        dataChooser test2=new dataChooser(CurrentGrades,StudentInfoArray,Course);

        String[] PRINTTEST1=test1.getStudentGrades();
        String[] PRINTTEST2=test2.getStudentGrades();
        System.out.println(Arrays.toString(PRINTTEST1));
        System.out.println(Arrays.toString(PRINTTEST2));

    }

}