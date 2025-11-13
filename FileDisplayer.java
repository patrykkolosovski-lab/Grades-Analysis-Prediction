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

        // Testing phase1step 1 + 2 methods
        double[] averagegrades = dataprocessor.AverageGrades(datacollector.GraduateGradesArray);
        System.out.println(averagegrades[1] + "\n");

        // Preparing objects for phase1step3
        StudentInformationReader studentinformationreader = new StudentInformationReader();
        step3 step3Object = new step3();

        // Testing phase1step 3 methods
        step3Object.STEP3(datacollector.GraduateGradesArray, studentinformationreader.StudentInfoArray);
        System.out.println();

        // Testing phase1step 4 methods
        step3Object.STEP3PART3(datacollector.GraduateGradesArray, studentinformationreader.StudentInfoArray, 0, datacollector.CourseNamesArray);

        // Phase 2 Test
        Phase2(datacollector.GraduateGradesArray, studentinformationreader.StudentInfoArray);
        System.out.println();

        // Preparing objects for phase1step4
        dataChooser datachooser = new dataChooser(null, studentinformationreader.StudentInfoArray, 0);

        // Phase1Step4 Test

        
        // test Filterer
        Filterer filterer = new Filterer("Stable");
        //TODO: NOT WORKING
        int[] indexes = filterer.getStudentIndexWithFeature(studentinformationreader.StudentInfoArray);

        for (int i = 0; i < indexes.length; i++) {
                System.out.println("Index: " + indexes[i]);
        }
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