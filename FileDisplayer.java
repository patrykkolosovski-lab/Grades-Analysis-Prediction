import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileDisplayer {

    public static String[] CourseNamesArray;

    public static int countHas = 0;
    public static int countHasnt = 0;
    public static int countNG = 0;
    public static int countpass = 0;

    public static void main(String[] args) {

        try {
            //To run code for GG:java FileDisplayer.java GraduateGrades(write this in terminal)
            //To run code for CG:java FileDIsplayer.java CurrentGrades(write this in terminal)
            //args[0]
            String fileName = "GraduateGrades.csv"; // The name of the file you want to read
            File file=new File(fileName); // Creating a File object so we can create code that reads the file
			
        	Scanner columnScanner = new Scanner(file); // This scanner is made to count the number of columns in the csv file
			Scanner rowScanner = new Scanner(file); // This scanner is made to count the number of columns in the csv file
			Scanner elementScanner = new Scanner(file); // This canner is made to record the elements from the csv file into the 2d array

			int rows = -1; // This is the variable that stores the number of rows in the data, note that it starts as -1 because it removes the line that states the column names
			int columns = -1; // this is the variable that stores the number of columns in the data
			int linesDone = 0; // This is the variable that keeps track of how many lines have been recorded into the array

            String line = columnScanner.nextLine(); // This is a variable that stores the current line which the column scanner is reading through
            Scanner lineScanner = new Scanner(line); // This is a scanner that separates the different parts of the line into objects separated by commas
            lineScanner.useDelimiter(","); // This is what separates based on commas

			// The following is a while loop that counts the number of columns based on the number of objects (separated by commas) until it finds an integer
            while (lineScanner.hasNext() && !lineScanner.hasNextInt()) { // This is a loop condition that makes sure that there is an object after the current one and that it is not an integer
				lineScanner.next(); // This makes the scanner jump to the next object, so we're not stuck in the current object
				columns++; // This adds one to the counter that keeps track of columns, since we have found an object that isnt an int
            }
            lineScanner.close(); // I dont know exactly what this does but I see it all the time so I put it there to stay safe
			columnScanner.close(); // Same here

			// The following is a while loop that counts the number of rows based on just going line by line in the file and counting how many jumps are made
			while (rowScanner.hasNextLine() && rows < 50) { // Note that it stops after 50, this is because we have thousands of rows and right now we're just writing and testing the code
				rows++;
			}

			String[][] GraduateGradesArray = new String[rows][columns]; // This is the 2D array that we create, it is an array of doubles because the code given to us mostly uses doubles
			CourseNamesArray = new String[columns]; // This is the array that stores the course names according to their courseID
            
			// The following while loop fills the array with the grades for each course for each student
            while (elementScanner.hasNextLine() && linesDone < 50) {
            	line = elementScanner.nextLine();
            	lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");
				int j = 0;
				int z = 0;
				lineScanner.next();
            	while (lineScanner.hasNext()) {
                    if (lineScanner.hasNext("NG")) {
                        GraduateGradesArray[linesDone-1][j] = "NG";
                        lineScanner.next();
                        j++;
                    } else if (lineScanner.hasNextDouble()) {
						GraduateGradesArray[linesDone-1][j] = lineScanner.next();
						j++;
            		} else {
            			CourseNamesArray[z] = lineScanner.next();
						z++;
            		}
            	}
				linesDone++;
            	lineScanner.close();
            }
            elementScanner.close();

			// All the code above this comment is made to prepare and fill the 2d array, from this point on you can use the 2d array for your tasks
			// Below are instructions for using the arrays
			// The 2d array is exactly like a table, where you get the information for a specific element in the array by putting the numbers for rows and columns like so
			// [rows][columns]
			// In our data, the rows are the student IDs while the columns are the course IDs
			// The student ID can be used directly but the courseID is not referred to by name but by position
			// The 0th course is "Cryogenic Physics", the 1st course is "Evolutionary Dynamics", the 2nd course is "Dark Energy", and so on
			// Below you can see how the Course IDs can be used to get the Course name using the CourseNamesArray
			//-System.out.println(CourseNamesArray[0] + ", " + CourseNamesArray[1] + ", " + CourseNamesArray[2]);
			// Below is the format for getting information from the 2d array
			// [studentID][courseID]
			// For example [0][0] is the studentID 0 and the course 0, so it returns the number 8 which is the grade for "Cryogenic Physics" for the studentID 0
			//-System.out.println(CourseNamesArray[0] + " for student 0: " + GraduateGradesArray[0][0]);
			// Another example is [5][13] which would be 10 which is the grade for "Warp Field Theory" for studentID 5
			// When searching through the data, you will probably use a loop with an if statement inside, when your code does finds the CourseID youre looking for,
			// you can get the name of the course with the CourseNamesArray by inputting that CourseID into the array
			// Write your main method code in between this comment and the next

			System.out.println();
			System.out.println();



			
			

			System.out.println("main method started\n");
            StudentInformationReader(GraduateGradesArray);
           
			
            // The next
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


	public static double[] AverageGrades(String[][] GGA) {
		double[] AverageGrades = new double[GGA[0].length];
		for (int i = 0; i < GGA[0].length; i++) {
            int counter = 0;
			double average = 0;
			for (int j = 0; j < GGA.length; j++) {
                if (GGA[j][i] != null) {
                    if (!GGA[j][i].equals("NG")) {
                        average = average + Double.parseDouble(GGA[j][i]);
                        counter++;
                    } else {
                        countNG++;
                    }
                }
			}
			average = average/counter;
			AverageGrades[i] = average;
            System.out.println(countNG);
            countNG = 0;
		}
		// for (int i = 0; i < AverageGrades.length; i++) {
		//	System.out.println(i + " " + AverageGrades[i]);
		// }
        
		return AverageGrades;
	}
//Find the lowest Grade
	public static int[] Findhighest(String[][] GGA) {
		double[] AverageGrades = AverageGrades(GGA);
		double x = 0;
		int z = 1;
		int y[] = new int[z];
		int w = 0;
		for(int i = 0; i < AverageGrades.length;i++){
			if(AverageGrades[i]>x){
				x=AverageGrades[i];
				y[0] = i;
				w = i;
			}
		}
		for(int i = 0; i < AverageGrades.length;i++){
			if(AverageGrades[i] == x && w != i){
				z++;
				int[] k = new int[z];
				for (int j = 0; j < y.length; j++) {
					k[j] = y[j];
				}
				y = k;

				y[y.length-1] = i;
			}
		}
		return y;
	}
//Find the highest Grade general
    public static int[] FindhighestGeneral(double[] array) {
		double[] AverageGrades = array;
		double x = 0;
		int z = 1;
		int y[] = new int[z];
		int w = 0;
		for(int i = 0; i < AverageGrades.length;i++){
			if(AverageGrades[i]>x){
				x=AverageGrades[i];
				y[0] = i;
				w = i;
			}
		}
		for(int i = 0; i < AverageGrades.length;i++){
			if(AverageGrades[i] == x && w != i){
				z++;
				int[] k = new int[z];
				for (int j = 0; j < y.length; j++) {
					k[j] = y[j];
				}
				y = k;

				y[y.length-1] = i;
			}
		}
		return y;
	}
//Find the lowest Grade
	public static int[] Findlowest(String[][] GGA) {
		double[] AverageGrades = AverageGrades(GGA);
		double x = 1000;
		int z = 1;
		int y[] = new int[z];
		int w = 0;
		for(int i = 0; i < AverageGrades.length;i++){
			if(AverageGrades[i]<x){
				x=AverageGrades[i];
				y[0] = i;
				w = i;
			}
		}
		for(int i = 0; i < AverageGrades.length;i++){
			if(AverageGrades[i] == x && w != i){
				z++;
				int[] k = new int[z];
				for (int j = 0; j < y.length; j++) {
					k[j] = y[j];
				}
				y = k;

				y[y.length-1] = i;
			}
		}
		return y;
	}


//Checking for the minimum grade for every course that each studentId has:
public static double[] min(String[][] GGA) {
        double[] MinimumGrades = new double[GGA[0].length];
        for (int i = 0; i < GGA[0].length; i++) {
            double min = 10000;
            for (int j = 0; j < GGA[0].length; j++) {
                if (!GGA[j][i].equals("NG")) {
                    if(Double.parseDouble(GGA[j][i])<min){
                        min=Double.parseDouble(GGA[j][i]);
                    }
                }
            }
            if (min == 10000) {
                min = 0;
            }
            MinimumGrades[i] = min;
        }

        return MinimumGrades;
    }
//Checking for the minimum grade for every course that each studentId has:
public static double[] max(String[][] GGA) {
        double[] MaximumGrades = new double[GGA[0].length];
        for (int i = 0; i < GGA[0].length; i++) {
            double max = 0;
            for (int j = 0; j < GGA[0].length; j++) {
                if (!GGA[j][i].equals("NG")) {
                    if(Double.parseDouble(GGA[j][i])>max){
                        max=Double.parseDouble(GGA[j][i]);
                    }
                }
            }

            MaximumGrades[i] = max;
        }

        return MaximumGrades;
    }

	public static double[] variance(String[][] GGA, double[] AverageGrades) {
        double[] variancearray = new double[GGA[0].length];
        for (int i = 0; i < AverageGrades.length; i++) {
            int counter = 0;
            double variance = 0;
            for (int j = 0; j < AverageGrades.length; j++) {
                if (!GGA[j][i].equals("NG")) {
                    variance = variance + Math.pow(AverageGrades[i] - Double.parseDouble(GGA[j][i]), 2);
                    counter++;
                }
            }
            variance = variance/counter;
            variancearray [i] = variance;
        }
        return variancearray;
	}

//Calculating the range by MAX-MIN
public static double[] range(double[] MaximumGrades,double[] MinimumGrades){
    int LENGTH= MaximumGrades.length;
    double range[];
    range=new double[LENGTH];

    for(int i=0; i<LENGTH ;i++){
        range[i]=MaximumGrades[i]-MinimumGrades[i];
    }
    return range;
}

//Calculating the amplitude by (MAX-MIN)/2
public static double[] amplitude(double[] MaximumGrades,double[] MinimumGrades){
    int LENGTH= MaximumGrades.length;
    double amp[];
    amp=new double[LENGTH];

    for(int i=0; i<LENGTH ;i++){
        amp[i]=(MaximumGrades[i]-MinimumGrades[i])/2;
    }
    return amp;
}

// Calculate avg grade per student
    public static double[] AverageStudentGrade(String[][] GGA) {
        double[] AverageStudentGrades = new double[GGA.length];
        for (int i = 0; i < GGA.length; i++) {
            int counter = 0;
            double average = 0;
            for (int j = 0; j < GGA[0].length; j++) {
                if (GGA[i][j] != null) {
                    if (!GGA[i][j].equals("NG")) {
                        average = average + Double.parseDouble(GGA[i][j]);
                        counter++;
                    } else {
                        countNG++;
                    }
                }
                
            }
            average = average / counter;
            AverageStudentGrades[i] = average;
            // System.out.println(countNG);
            if (average >= 6.0) {
                countpass++;
                System.out.println(countpass);
            }
            countNG = 0;
            
        }
        return AverageStudentGrades;
    }

//Find who graduated cum laude (8.5 or higher)
    public static void cumLaude(double[] AverageStudentGrades) {
        List<Integer> CumLaudeID = new ArrayList<>();
        for (int i = 0; i < AverageStudentGrades.length; i++) {
            if (AverageStudentGrades[i] >= 8.5) {
                CumLaudeID.add(i);
            }
        }
        System.out.println("the amount of students who graduated cum laude: " + CumLaudeID.size());
        System.out.println("students who graduated cum laude: " + CumLaudeID);
    }

//Show how many students fall in each grade range per subject
    public static void gradeDistributionBySubject(String[][] GGA, String[] CourseNamesArray) {
        int brackets = 5;
        int subjects = GGA[0].length;
        String[] ranges = {
                "5.0 – 5.9",
                "6.0 – 6.9",
                "7.0 – 7.9",
                "8.0 – 8.9",
                "9.0 – 10.0"
        };
        int[][] gradeDistribution = new int[brackets][subjects];

        //Count how many grades fall into each range
        for (int i = 0; i < GGA[0].length; i++) {
            for (int j = 0; j < GGA.length; j++) {
                if (GGA[j][i] != null) {
                    if (!GGA[j][i].equals("NG")) {
                        double value = Double.parseDouble(GGA[j][i]);
                        if (value >= 5.0 && value < 6.0) {
                            gradeDistribution[0][i]++;
                        } else if (value >= 6.0 && value < 7.0) {
                            gradeDistribution[1][i]++;
                        } else if (value >= 7.0 && value < 8.0) {
                            gradeDistribution[2][i]++;
                        } else if (value >= 8.0 && value < 9.0) {
                            gradeDistribution[3][i]++;
                        } else if (value >= 9.0 && value <= 10.0) {
                            gradeDistribution[4][i]++;
                        }
                    }
                }
            }
        }

        //Print results for each subject
        for (int subject = 0; subject < gradeDistribution[0].length; subject++) {
            System.out.println("subject: " + CourseNamesArray[subject]);
            for (int k = 0; k < gradeDistribution.length; k++) {
                System.out.println("  " + ranges[k] + " : " + gradeDistribution[k][subject]);
            }
            System.out.println();
        }
    }

    //Check which course separates top performers from the rest
    public static void coursesThatSeparateTop(String[][] GGA, String[] CourseNamesArray) {
        double[] averages = AverageStudentGrade(GGA);

        List<Integer> TopIDs = new ArrayList<>();
        List<Integer> RestIDs = new ArrayList<>();
        for (int i = 0; i < averages.length; i++) {
            if (averages[i] >= 8.5) {
                TopIDs.add(i);
            } else {
                RestIDs.add(i);
            }
        }

        System.out.println("top students (>= 8.5 avg): " + TopIDs);
        System.out.println("rest of students: " + RestIDs);

        String bestCourse = "";
        double bestDiff = -1e9;

//Compare averages for each subject between top and rest
        for (int s = 0; s < GGA[0].length; s++) {
    double topSum = 0.0;
    int topCount = 0;
    for (int t = 0; t < TopIDs.size(); t++) {
        int id = TopIDs.get(t);
        String val = GGA[id][s];
        if (val != null && !val.equalsIgnoreCase("NG")) {
            topSum += Double.parseDouble(val);
            topCount++;
        }
    }
    double topMean = topCount == 0 ? Double.NaN : topSum / topCount;

    double restSum = 0.0;
    int restCount = 0;
    for (int r = 0; r < RestIDs.size(); r++) {
        int id2 = RestIDs.get(r);
        String val2 = GGA[id2][s];
        if (val2 != null && !val2.equalsIgnoreCase("NG")) {
            restSum += Double.parseDouble(val2);
            restCount++;
        }
    }
    double restMean = restCount == 0 ? Double.NaN : restSum / restCount;

    double diff = topMean - restMean;

    System.out.println("  " + CourseNamesArray[s] + " -> diff (top - rest): " +
                       (Double.isNaN(diff) ? "NaN" : String.format("%.3f", diff)) +
                       " (top: " + (Double.isNaN(topMean) ? "NaN" : String.format("%.2f", topMean)) +
                       ", rest: " + (Double.isNaN(restMean) ? "NaN" : String.format("%.2f", restMean)) + ")");

    if (!Double.isNaN(diff) && diff > bestDiff) {
        bestDiff = diff;
        bestCourse = CourseNamesArray[s];
    }
}


        System.out.println("course that separates top from rest the most: " +
                           bestCourse + " (diff = " + String.format("%.3f", bestDiff) + ")");
    }
//StudentInformation.csv file
public static void StudentInformationReader(String[][] CG){
    try {
            String CurrentGrades[][]=CG;
        

            String fileName = "StudentInfo.csv"; // The name of the file you want to read
            
            File file=new File(fileName); 
			

        	Scanner columnScanner = new Scanner(file); 
			Scanner rowScanner = new Scanner(file); 
			Scanner elementScanner = new Scanner(file); 

			int rows = -1; 
			int columns = -1; 
			int linesDone = 0; 

            String line = columnScanner.nextLine(); 
            Scanner lineScanner = new Scanner(line); 
            lineScanner.useDelimiter(","); 

			if (elementScanner.hasNextLine()) {//skipping the header
            elementScanner.nextLine();
}
            while (lineScanner.hasNext() && !lineScanner.hasNextInt()) { 
				lineScanner.next(); 
				columns++; 
            }
            lineScanner.close(); 
			columnScanner.close();

			
			while (rowScanner.hasNextLine() && rows < 50) { 
				rows++;
			}

			String[][] StudentInfoArray = new String[rows][columns]; 
			
            
			// The following while loop fills the array with the grades for each course for each student
            while (elementScanner.hasNextLine() && linesDone < 50) {
            	line = elementScanner.nextLine();
            	lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");
				int j = 0;
				
				lineScanner.next(); //skip first column ID if needed
            	
                while (lineScanner.hasNext()) {
					StudentInfoArray[linesDone][j] = lineScanner.next();
                    j++;
            	}
				linesDone++;
            	lineScanner.close();
            }
            elementScanner.close();
            rowScanner.close();

//STEP3(CurrentGrades, StudentInfoArray); 
Phase2(CurrentGrades, StudentInfoArray);

        
} catch (Exception e){
    e.printStackTrace();}

}

public static void STEP3(String[][] CG,String[][] SI){
//Part 1
    //Avg for feature1
    int FeatureColumn=0;
    String ActualFeature="Stable";
    
    //Step 1:finding out the length of the array
    int count=0;
    for(int i=0;i<SI.length;i++){
        if(SI[i][FeatureColumn].equalsIgnoreCase(ActualFeature)){
            count++;
        }
    }
    //Step 2:filling the array with the indexes of the students that share the same properties
    int index=0;
    int SItemp[]=new int[count];
    for(int i=0;i<SI.length;i++){
        if(SI[i][FeatureColumn].equalsIgnoreCase(ActualFeature)){
            SItemp[index]=i;
            index++;
        }
    }

    //Step 3:calculating the average of those that have the feature
    double totalsum=0;
    int totalgrades=0;
    for(int m=0;m<SItemp.length;m++){
        int studentindex=SItemp[m];
        for(int j=0;j<CG[0].length;j++){
            if(CG[studentindex][j]!=null&&!CG[studentindex][j].equals("NG")){
            
                totalsum+=Double.parseDouble(CG[studentindex][j]);
                totalgrades++;}
            
        }
    }

    double avg=(totalgrades>0)? (totalsum/totalgrades):0; //Calculating the AVG per student with the specific feature(if-then)
    //Step 4:calculating the variance of those that have the feature
    double SquaredDiff=0;
    for(int m=0;m<SItemp.length;m++){
        int studentindex=SItemp[m];
        for(int j=0;j<CG[0].length;j++){
            if(CG[studentindex][j]!=null&&!CG[studentindex][j].equals("NG")){
            
                double grade=Double.parseDouble(CG[studentindex][j]);
                SquaredDiff=Math.pow(grade-avg, 2);}
            
        }
    }
    double var=(totalgrades>0)? (SquaredDiff/totalgrades):0; //Calculating the VARIANCE per student with the specific feature(if-then)
    
    System.out.println("The AVERAGE of the students that DO share the feature "+ActualFeature+" is "+avg);
    System.out.println("The VARIANCE of the students that DO share the feature "+ActualFeature+" is "+var);
    

    //Avg for those who do not have the feature
    int FeatureColumn2=FeatureColumn;
    String ActualFeature2=ActualFeature;
    
    //Step 1:finding out the length of the array
    int count2=0;
    for(int i=0;i<SI.length;i++){
        if(!SI[i][FeatureColumn2].equalsIgnoreCase(ActualFeature2)){
            count2++;
        }
    }
    //Step 2:filling the array with the indexes of the students that share the same properties
    int index2=0;
    int SItemp2[]=new int[count2];
    for(int i=0;i<SI.length;i++){
        if(!SI[i][FeatureColumn2].equalsIgnoreCase(ActualFeature2)){//negation
            SItemp2[index2]=i;
            index2++;
        }
    }

    //Step 3:calculating the average of those that have the feature
    double totalsum2=0;
    int totalgrades2=0;
    for(int m=0;m<SItemp2.length;m++){
        int studentindex2=SItemp2[m];
        for(int j=0;j<CG[0].length;j++){
            if(CG[studentindex2][j]!=null&&!CG[studentindex2][j].equals("NG")){
            
                totalsum2+=Double.parseDouble(CG[studentindex2][j]);
                totalgrades2++;}
            
        }
    }
    
    double avg2=(totalgrades2>0)? (totalsum2/totalgrades2):0; //Calculating the AVG per student without the specific feature(if-then)
    
    //Step 4:calculating the variance of those that have the feature
    double SquaredDiff2=0;
    for(int m=0;m<SItemp2.length;m++){
        int studentindex2=SItemp2[m];
        for(int j=0;j<CG[0].length;j++){
            if(CG[studentindex2][j]!=null&&!CG[studentindex2][j].equals("NG")){
            
                double grade2=Double.parseDouble(CG[studentindex2][j]);
                SquaredDiff2=Math.pow(grade2-avg2, 2);}
            
        }
    }

   
    double var2=(totalgrades2>0)? (SquaredDiff2/totalgrades2):0; //Calculating the VARIANCE per student without the specific feature(if-then)
    
    System.out.println("");
    System.out.println("The AVERAGE of the students that DO NOT share the feature "+ActualFeature2+" is "+avg2);
    System.out.println("The VARIANCE of the students that DO NOT share the feature "+ActualFeature2+" is "+var2);
    System.out.println("");
    System.out.println("The difference in AVERAGE for those who have the feature "+ActualFeature+" and those who dont have the feature is "+(Math.abs(avg2-avg)));
    System.out.println("The difference in VARIANCE for those who have the feature "+ActualFeature+" and those who dont have the feature is "+(Math.abs(var2-var)));

}

    
public static void STEP3PART2(String[][] CG){
//Part 2
    //Avg for feature1
    int FeatureColumn=1;
    double ActualNumericFeature=6.0;
    
    //Step 1:finding out the length of the array
    int count=0;
    for(int i=0;i<CG.length;i++){
        if(CG[i][FeatureColumn]==null || CG[i][FeatureColumn].equals("NG")){continue;}
        Double grade=Double.parseDouble(CG[i][FeatureColumn]);
        if(grade>ActualNumericFeature){
            count++;
        }
    }
    
    //Step 2:filling the array with the indexes of the students that share the same properties
    int index=0;
    int SItemp[]=new int[count];
    for(int i=0;i<CG.length;i++){
        if(CG[i][FeatureColumn]==null || CG[i][FeatureColumn].equalsIgnoreCase("NG")){continue;}
        Double grade=Double.parseDouble(CG[i][FeatureColumn]);
        if(grade>ActualNumericFeature){
            SItemp[index]=i;
            index++;
        }
    }

    //Step 3:calculating the average of those that have the feature
    double totalsum=0;
    int totalgrades=0;
    for(int m=0;m<SItemp.length;m++){
        int studentindex=SItemp[m];
        for(int j=0;j<CG[0].length;j++){
            if(CG[studentindex][j]!=null&&!CG[studentindex][j].equals("NG")){
            
                totalsum+=Double.parseDouble(CG[studentindex][j]);
                totalgrades++;}
            
        }
    }


    double avg=(totalgrades>0)? (totalsum/totalgrades):0; //Calculating the AVG per student with the specific feature(if-then)
    
    //Step 4:calculating the variance of those that have the feature
    double SquaredDiff=0;
    for(int m=0;m<SItemp.length;m++){
        int studentindex=SItemp[m];
        for(int j=0;j<CG[0].length;j++){
            if(CG[studentindex][j]!=null&&!CG[studentindex][j].equals("NG")){
            
                double grade=Double.parseDouble(CG[studentindex][j]);
                SquaredDiff=Math.pow(grade-avg, 2);}
        }
    }
    double var=(totalgrades>0)? (SquaredDiff/totalgrades):0; //Calculating the VARIANCE per student with the specific feature(if-then)
    System.out.println("The AVERAGE of the students that DO share the numeric feature > "+ActualNumericFeature+" is "+avg);
    System.out.println("The VARIANCE of the students that DO share the numeric feature > "+ActualNumericFeature+" is "+var);

    //Avg for those who do not have the feature
    int FeatureColumn2=FeatureColumn;
    double ActualNumericFeature2=ActualNumericFeature;
    
    //Step 1:finding out the length of the array
    int count2=0;
    for(int i=0;i<CG.length;i++){
        if(CG[i][FeatureColumn2]==null || CG[i][FeatureColumn2].equalsIgnoreCase("NG")){continue;}
        Double grade=Double.parseDouble(CG[i][FeatureColumn2]);
        if(grade>ActualNumericFeature2){
            count2++;
        }
    }
    //Step 2:filling the array with the indexes of the students that share the same properties
    int index2=0;
    int SItemp2[]=new int[count2];
    for(int i=0;i<CG.length;i++){
        if(CG[i][FeatureColumn2]==null || CG[i][FeatureColumn2].equalsIgnoreCase("NG")){continue;}
        Double grade=Double.parseDouble(CG[i][FeatureColumn2]);
        if(grade<=ActualNumericFeature2){
            SItemp2[index2]=i;
            index2++;
        }
    }

    //Step 3:calculating the average of those that have the feature
    double totalsum2=0;
    int totalgrades2=0;
    for(int m=0;m<SItemp2.length;m++){
        int studentindex2=SItemp2[m];
        for(int j=0;j<CG[0].length;j++){
            if(CG[studentindex2][j]!=null&&!CG[studentindex2][j].equals("NG")){
            
                totalsum2+=Double.parseDouble(CG[studentindex2][j]);
                totalgrades2++;}
            
        }
    }
    
    double avg2=(totalgrades2>0)? (totalsum2/totalgrades2):0; //Calculating the AVG per student without the specific feature(if-then)
    
    //Step 4:calculating the variance of those that have the feature
    double SquaredDiff2=0;
    for(int m=0;m<SItemp2.length;m++){
        int studentindex2=SItemp2[m];
        for(int j=0;j<CG[0].length;j++){
            if(CG[studentindex2][j]!=null&&!CG[studentindex2][j].equals("NG")){
            
                double grade2=Double.parseDouble(CG[studentindex2][j]);
                SquaredDiff2=Math.pow(grade2-avg2, 2);}
            
        }
    }

   
    double var2=(totalgrades2>0)? (SquaredDiff2/totalgrades2):0; //Calculating the VARIANCE per student without the specific feature(if-then)
    
    System.out.println("");
    System.out.println("The AVERAGE of the students that DO NOT share the numeric feature >"+ActualNumericFeature2+" is "+avg2);
    System.out.println("The VARIANCE of the students that DO NOT share the numeric feature >"+ActualNumericFeature2+" is "+var2);
    System.out.println("");
    System.out.println("The difference in AVERAGE for those who have the numeric feature >"+ActualNumericFeature+" and those who dont have the feature is "+Math.abs(avg2-avg));
    System.out.println("The difference in VARIANCE for those who have the numeric feature >"+ActualNumericFeature+" and those who dont have the feature is "+Math.abs(var2-var));


}


public static void STEP3PART3 (String[][] CG,String[][] SI, int coursecolumn){
//given a course this code will find the best property to reduce variance
//!!!! in our results the variance reduction will be store according to the indexes of this ARRAY//!!!!!
String features[] = new String[] {"Stable", "Fractured", "Chaotic", "Coherent", "Resonant", "none", "Harmonized", "1 ns/mc", "2 ns/mc", "3 ns/mc", "Silver", "Crimson", "White-Blue", "Violet", "Turquiose"};
// this is where you input your course and the index of the course 
double results[][] = new double[features.length][4]; 
// variance reduction, weighted variance, avg with, avg without
double course_avg = 0;
double grade_count = 0; 
// calculate overall avg



//This for loop calculates the variance after splitting on features 
   for (int j=0; j<features.length; j++){
    int FeatureColumn=0;
      if (j < 5) {
    FeatureColumn = 0; // Quantum Coherence Threshold
} else if (j < 7) {
    FeatureColumn = 1; // Symbiotic Network Compatibility
} else if (j < 10) {
    FeatureColumn = 2; // Astro-Temporal Drift Resistance
} else if (j < 11) {
    FeatureColumn = 3; // Psionic Interference Tolerance (currently ignored)
} else {
    FeatureColumn = 4; // Bio-Luminal Transmission
}

   
        String ActualFeature=features[j];
    
    
    int count=0;
    int count_without = 0; 

    
    
     for(int i = 0; i < SI.length; i++) {
            if(i >= CG.length || CG[i][coursecolumn] == null || SI[i][FeatureColumn] == null || CG[i][coursecolumn].equals("NG")) { continue; }

            if(SI[i][FeatureColumn].trim().equalsIgnoreCase(ActualFeature)) {
                count++;
            } else {
                count_without++;
            }
        }

         //We create arrays with indexes: 1 with prop, 2 without 
        int withFeature[] = new int[count];
        int withoutFeature[] = new int[count_without];    
        
    //Step 2:filling the array with the indexes of the students that share the same properties and we store the rest in a separate array
    int index=0;
    int index_WO=0;
    for (int i = 0; i < SI.length; i++) {
    if (i >= CG.length || CG[i][coursecolumn] == null  || SI[i][FeatureColumn] == null || CG[i][coursecolumn].equals("NG")) {
        continue;
    }
        if(SI[i][FeatureColumn].trim().equalsIgnoreCase(ActualFeature)){
            withFeature[index]=i;
            index++;
        } else {
            withoutFeature[index_WO]=i;
            index_WO++; 
        }
    }
    

double course_avg_S = 0;
double grade_count_S = 0; 

//avg with feature 
for (int i =0 ; i < withFeature.length; i ++) {
    int studentid = withFeature[i];
    if(!CG[studentid][coursecolumn].equals("NG")){ 
    
            course_avg_S = course_avg_S + Double.parseDouble(CG[studentid][coursecolumn]);
            grade_count_S++;
        }
 
    }
    if (grade_count_S == 0) {
        continue;
    }
   course_avg_S = course_avg_S/grade_count_S;

//avg without feature
double course_avg_O = 0;
int grade_count_O =0; 

for (int i =0 ; i < withoutFeature.length; i ++) {
    int studentid = withoutFeature[i];
    if(!CG[studentid][coursecolumn].equals("NG")){
            course_avg_O = course_avg_O + Double.parseDouble(CG[studentid][coursecolumn]);
            grade_count_O++;
        }
    }
course_avg_O = course_avg_O/grade_count_O;
//var with feature
double varall_S =0;
for (int i =0 ; i < withFeature.length; i++) {
    int studentid = withFeature[i];
    if(!CG[studentid][coursecolumn].equals("NG"))
        {
            varall_S = varall_S + Math.pow((course_avg_S - Double.parseDouble(CG[studentid][coursecolumn])),2); 
            
        }  
 }
 varall_S= varall_S / grade_count_S;
// var without
 double varall_O =0;

 for (int i =0 ; i < withoutFeature.length; i ++) {    
    int studentid = withoutFeature[i];
    if(!CG[studentid][coursecolumn].equals("NG")){     
       varall_O = varall_O + Math.pow((course_avg_O - Double.parseDouble(CG[studentid][coursecolumn])),2);    
        }  
         
 }
varall_O= varall_O / grade_count_O;

// Inside variancereduction, remove the global course_avg/varall block,
// and after you build withFeature[] and withoutFeature[] for a given feature:

// Build the eligible index list = union of with/without groups
int[] eligible = new int[withFeature.length + withoutFeature.length];
int ei = 0;
for (int t = 0; t < withFeature.length; t++) eligible[ei++] = withFeature[t];
for (int t = 0; t < withoutFeature.length; t++) eligible[ei++] = withoutFeature[t];

// Guard: both groups must have at least one grade to avoid divide-by-zero
if (grade_count_S == 0 || grade_count_O == 0) {
    continue; // skip this feature; no valid split
}

// Compute overall (parent) mean/variance on the eligible set
double parentSum = 0.0;
int parentN = 0;
for (int idx2 = 0; idx2 < eligible.length; idx2++) {
    int s = eligible[idx2];
    if (CG[s][coursecolumn] != null && !CG[s][coursecolumn].equals("NG")) {
        parentSum += Double.parseDouble(CG[s][coursecolumn]);
        parentN++;
    }
}
double parentMean = (parentN > 0) ? (parentSum / parentN) : 0.0;
double varall = 0.0;
for (int idx2 = 0; idx2 < eligible.length; idx2++) {
    int s = eligible[idx2];
    if (CG[s][coursecolumn] != null && !CG[s][coursecolumn].equals("NG")) {
        double v = Double.parseDouble(CG[s][coursecolumn]);
        varall += Math.pow(parentMean - v, 2);
    }
}
varall = (parentN > 0) ? (varall / parentN) : 0.0;

// calculate var reduction
double allgrades = grade_count_S + grade_count_O;
double weighted_var =0;
weighted_var = ((grade_count_S * varall_S) + (grade_count_O * varall_O))/allgrades;
double variance_reduction = varall - weighted_var;  
results[j][0]= variance_reduction; 
results[j][1] = weighted_var;
results[j][2]= course_avg_S; // avg with
results[j][3] = course_avg_O ; // avg without



}
                                    // RESULTS //
int best_index =-1;
double max_reduction = -1;
for (int i =0; i < features.length; i++){
 if (results[i][0]>max_reduction){
    max_reduction = results[i][0];
    best_index=i;}
}
System.out.println("The best feature to split on "+ CourseNamesArray[coursecolumn] + " is " + features[best_index]);
System.out.println("The variance reduction is " + results[best_index][0]);
System.out.println("If " + features[best_index] + " then grade = " + results[best_index][2] + " else grade = " + results[best_index][3]);

for (int i = 0; i < SI.length; i++) {
    STEP3PART4(i, results, best_index, SI, features);
}
System.out.println("Number of students with feature = " + countHas);
System.out.println("Number of students without feature = " + countHasnt);
countHas = 0;
countHasnt = 0;

}

public static void STEP3PART4(int studentID, double[][] results, int best_index, String[][] SI, String[] features) {
    boolean hasFeature = false;
    int FeatureColumn = 0;
    if (best_index < 5) {
    FeatureColumn = 0; // Quantum Coherence Threshold
} else if (best_index < 7) {
    FeatureColumn = 1; // Symbiotic Network Compatibility
} else if (best_index < 10) {
    FeatureColumn = 2; // Astro-Temporal Drift Resistance
} else if (best_index < 11) {
    FeatureColumn = 3; // Psionic Interference Tolerance (currently ignored)
} else {
    FeatureColumn = 4; // Bio-Luminal Transmission
}

    if (SI[studentID][FeatureColumn].equals(features[best_index])) {
        hasFeature = true;
    }

    

    if (hasFeature) {
        // System.out.println(studentID + "'s predicted grade for the given course is " + results[best_index][2]);
        countHas++;
    } else {
        // System.out.println(studentID + "'s predicted grade for the given course is " + results[best_index][3]);
        countHasnt++;
    }
}





public static void Phase2(String[][] CurrentGrades,String[][] StudentInfoArray){

String Feature="Stable";
int Course=0;
dataChooser test1=new dataChooser(CurrentGrades,StudentInfoArray,Course);
dataChooser test2=new dataChooser(CurrentGrades,StudentInfoArray,Course,Feature);

System.out.println(test1.course);
System.out.println(test2.course);

}

}
