
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
        System.out.println(averagegrades[1]);

        // Preparing objects for phase1step3
        StudentInformationReader studentinformationreader = new StudentInformationReader();
        step3 step3Object = new step3();

        // Testing phase1step 3 methods
        step3Object.STEP3(datacollector.GraduateGradesArray, studentinformationreader.StudentInfoArray);

    }

    public void Phase2Test(String[][] CurrentGrades,String[][] StudentInfoArray){

        String Feature="Stable";
        int Course=0;
        Chooser test1=new Chooser(CurrentGrades,StudentInfoArray,Course);
        Chooser test2=new Chooser(CurrentGrades,StudentInfoArray,Course,Feature);

        System.out.println(test1.course);
        System.out.println(test2.course);

    }

}
