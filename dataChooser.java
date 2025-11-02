import java.util.Scanner;

class dataChooser{
    public String[][] gradesdata;
    public String[][] StudentInfo;
    public int CourseColumn;
    public String feature;
    public int PITChecker = 0;
    public double PITFeature = 0;

    
    //We choose an array made of the grades for the course
    public dataChooser(String[][] data, String[][] StudentInfo, int course) {
        this.StudentInfo = StudentInfo;
        this.gradesdata = data;
        this.CourseColumn = course;
    }
    //We choose an array made of the features of the students from StudentInformation
    public dataChooser(String[][] data, String[][] StudentInfo,int Course, String feature) {
        this.StudentInfo = StudentInfo;
        this.gradesdata = data;
        this.CourseColumn= Course;
        this.feature = feature;
    }

    public dataChooser(String feature) {
        this.feature = feature;
    }

    public int getFeatureColumn(){
        checkPIT();
        if(PITChecker!=0) {
            return 3;
        }
        int FeatureColumn=0;
        int j = 0;
        String features[] = new String[] {"Stable", "Fractured", "Chaotic", "Coherent", "Resonant", "None", "Harmonized", "1 ns/mc", "2 ns/mc", "3 ns/mc", "Silver", "Crimson", "White-Blue", "Violet", "Turquiose"};
        for (int i=0; i<features.length; i++){
            if (feature.equals(features[i])) {
                j = i;
            }
        }

        if (j < 5) {
                FeatureColumn = 0; // Quantum Coherence Threshold
            } else if (j < 7) {
                FeatureColumn = 1; // Symbiotic Network Compatibility
            } else if (j < 10) {
                FeatureColumn = 2; // Astro-Temporal Drift Resistance
            } else if (j < 15) {
                FeatureColumn = 4; // Bio-Luminal Transmission
            }
        return FeatureColumn;
    }

    private void checkPIT(){
        Scanner scanner = new  Scanner(feature);
        if (scanner.hasNext("greater")) {
            PITChecker = 1;
            while(!scanner.hasNextDouble()) {
                scanner.next();
            }
            PITFeature = scanner.nextDouble();
        } else if (scanner.hasNext("smaller")) {
            PITChecker = 2;
            while(!scanner.hasNextDouble()) {
                scanner.next();
            }
            PITFeature = scanner.nextDouble();
        } else {
            PITChecker = 0;
        }
    }
   
    //Here you do all of the process for filtering data based on the course or feature you put in
    public String[] getStudentGrades(){
        //Part1
        if(feature==null||feature.isEmpty()){ //Checking if a feature is present if not we just take the normal grades and create an array
            //Step 1:finding out the length of the CourseGrades array
            int count=0;
            for(int i=0;i<gradesdata.length;i++){
                if(gradesdata[i][CourseColumn]==null || gradesdata[i][CourseColumn].equals("NG")){continue;}
                count++;
            }

            //Separate Array to store the grades for the courses
            String CourseGrades[]=new String[count];

            //Step 2:filling the array with the grades of the students
            int index=0;
   
            for(int i=0;i<gradesdata.length;i++){
                if(gradesdata[i][CourseColumn]==null || gradesdata[i][CourseColumn].equalsIgnoreCase("NG")){continue;}
                CourseGrades[index]=gradesdata[i][CourseColumn];
                index++;
            }

            return CourseGrades;

        } else {
            int featureColumn = getFeatureColumn();
            //Part2
            //Step 1:finding out the length of the CourseGrades array that has a certain feature
            int count=0;
            for(int i=0;i<gradesdata.length;i++){
                if(PITChecker==0) {
                    if (featureColumn==4) {
                        if (StudentInfo[i][featureColumn].charAt(0)==feature.charAt(0)) {count++;}
                    } else {
                        if(StudentInfo[i][featureColumn].equals(feature)){count++;}
                    }
                } else if (PITChecker==1) {
                    if(Double.parseDouble(StudentInfo[i][featureColumn])>=PITFeature){count++;}
                } else if (PITChecker==2) {
                    if(Double.parseDouble(StudentInfo[i][featureColumn])<=PITFeature){count++;}
                }
            }
            //Separate Array to store the grades for the courses
            String CourseGrades[]=new String[count];

            //Step 2:filling the array with the graddes of the students
            int index=0;
            int temp[]=new int[count];
            for(int i=0;i<gradesdata.length;i++){
                if(gradesdata[i][CourseColumn]==null || gradesdata[i][CourseColumn].equalsIgnoreCase("NG")){continue;}
                if(PITChecker==0) {
                    if (featureColumn==4) {
                        if (StudentInfo[i][featureColumn].charAt(0)==feature.charAt(0)){
                            CourseGrades[index]=gradesdata[i][CourseColumn];
                            temp[index]= i;
                            index++;
                        }
                    } else {
                        if(StudentInfo[i][featureColumn].equalsIgnoreCase(feature)){
                        CourseGrades[index]=gradesdata[i][CourseColumn];
                        temp[index]= i;
                        index++;
                        }
                    }
                } else if (PITChecker==1) {
                    if(Double.parseDouble(StudentInfo[i][featureColumn])>=PITFeature){
                        CourseGrades[index]=gradesdata[i][CourseColumn];
                        temp[index]= i;
                        index++;
                    }
                } else if (PITChecker==2) {
                    if(Double.parseDouble(StudentInfo[i][featureColumn])<=PITFeature){
                        CourseGrades[index]=gradesdata[i][CourseColumn];
                        temp[index]= i;
                        index++;
                    }
                }
            }

            for(int m=0;m<temp.length;m++){
                int studentindex=temp[m];
                if(gradesdata[studentindex][CourseColumn]!=null&&!gradesdata[studentindex][CourseColumn].equals("NG")){
                    CourseGrades[m]=gradesdata[studentindex][CourseColumn];
                }
            }
            return CourseGrades;
        }
    }
}
