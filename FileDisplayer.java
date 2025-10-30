import java.util.Arrays;

public class FileDisplayer {

    public String[] CourseNamesArray;
    
    public int countpass = 0;

    public static void main(String[] args) {
        //To run code for GG:java FileDisplayer.java GraduateGrades.csv(write this in terminal)
        //To run code for CG:java FileDIsplayer.java CurrentGrades.csv(write this in terminal)
        //args[0]
        String fileName = "GraduateGrades.csv"; // The name of the file you want to read


        // Preparing objects for phase1step1+2
        dataCollector datacollector = new dataCollector(fileName);
        dataProcessing dataprocessor = new dataProcessing();

        // Testing phase1step 1 + 2 methods
        double[] averagegrades = dataprocessor.AverageGrades(datacollector.GraduateGradesArray);
        System.out.println(averagegrades[1] + "\n");

        // Preparing objects for phase1step3
        StudentInformationReader studentinformationreader = new StudentInformationReader();
        step3 step3Object = new step3();

        // Testing phase1step 3 methods
        step3Object.STEP3(datacollector.GraduateGradesArray, studentinformationreader.StudentInfoArray);
        System.out.println();

        // Phase 2 Test
        Phase2(datacollector.GraduateGradesArray, studentinformationreader.StudentInfoArray);
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
