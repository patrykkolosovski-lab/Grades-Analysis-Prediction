import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileDisplayerOriginal {

    public static void main(String[] args) {

        try {
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

			double[][] GraduateGradesArray = new double[rows][columns]; // This is the 2D array that we create, it is an array of doubles because the code given to us mostly uses doubles
			String[] CourseNamesArray = new String[columns]; // This is the array that stores the course names according to their courseID
            
			// The following while loop fills the array with the grades for each course for each student
            while (elementScanner.hasNextLine() && linesDone < 50) {
            	line = elementScanner.nextLine();
            	lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");
				int j = 0;
				int z = 0;
				lineScanner.next();
            	while (lineScanner.hasNext()) {
					if (lineScanner.hasNextDouble()) {
						GraduateGradesArray[linesDone-1][j] = lineScanner.nextDouble();
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
			System.out.println(CourseNamesArray[0] + ", " + CourseNamesArray[1] + ", " + CourseNamesArray[2]);
			// Below is the format for getting information from the 2d array
			// [studentID][courseID]
			// For example [0][0] is the studentID 0 and the course 0, so it returns the number 8 which is the grade for "Cryogenic Physics" for the studentID 0
			System.out.println(CourseNamesArray[0] + " for student 0: " + GraduateGradesArray[0][0]);
			// Another example is [5][13] which would be 10 which is the grade for "Warp Field Theory" for studentID 5
			System.out.println(CourseNamesArray[13] + " for student 5: " + GraduateGradesArray[5][13]);
			// When searching through the data, you will probably use a loop with an if statement inside, when your code does finds the CourseID youre looking for,
			// you can get the name of the course with the CourseNamesArray by inputting that CourseID into the array
			// Write your main method code in between this comment and the next

			System.out.println();
			System.out.println();

			// This commented code is to test out the code for finding the hardest and easiest courses
			// for (int i = 0; i < Findlowest(GraduateGradesArray).length; i++) {
			// 	System.out.println(Findlowest(GraduateGradesArray)[i]);
			// }



			for (int i = 0; i < AverageStudentGrade(GraduateGradesArray).length; i++) {
				System.out.println(AverageStudentGrade(GraduateGradesArray)[i]);
			}
			

			System.out.println("main method started\n");

        // example data
        // double[][] GGA = {
        //     {9.0, 7.5, 8.5},
        //     {6.2, 6.8, 7.0},
        //     {8.6, 9.1, 8.9},
        //     {5.9, 6.1, 6.3}
        // };
        // String[] CourseNamesArray = {"Intro DSAI", "Discrete Math", "Programming"};
        // double[] avg = AverageStudentGrade(GGA);
        // cumLaude(avg);
        // gradeDistributionBySubject(GGA, CourseNamesArray);
        // coursesThatSeparateTop(GGA, CourseNamesArray);

			


			

			// The next comment, ignore the catch stuff below

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


	public static double[] AverageGrades(double[][] GGA) {
		double[] AverageGrades = new double[GGA[0].length];
		for (int i = 0; i < GGA[0].length; i++) {
			double average = 0;
			for (int j = 0; j < GGA.length; j++) {
				average = average + GGA[j][i];
			}
			average = average/GGA.length;
			AverageGrades[i] = average;
		}
		// for (int i = 0; i < AverageGrades.length; i++) {
		//	System.out.println(i + " " + AverageGrades[i]);
		// }
		return AverageGrades;
	}

	public static int[] Findhighest(double[][] GGA) {
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

	public static int[] Findlowest(double[][] GGA) {
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
public static double[] min(double[][] GGA) {
        double[] MinimumGrades = new double[GGA[0].length];
        for (int i = 0; i < GGA[0].length; i++) {
            double min = 10000;
            for (int j = 0; j < GGA[0].length; j++) {
                if(GGA[j][i]<min){
                    min=GGA[j][i];
                }
            }

            MinimumGrades[i] = min;
        }

        return MinimumGrades;
    }
//Checking for the minimum grade for every course that each studentId has:
public static double[] max(double[][] GGA) {
        double[] MaximumGrades = new double[GGA[0].length];
        for (int i = 0; i < GGA[0].length; i++) {
            double max = 0;
            for (int j = 0; j < GGA[0].length; j++) {
                if(GGA[j][i]>max){
                    max=GGA[j][i];
                }
            }

            MaximumGrades[i] = max;
        }

        return MaximumGrades;
    }

	public static double[] variance(double[][] GGA, double[] AverageGrades) {
        double[] variancearray = new double[GGA[0].length];
        for (int i = 0; i < AverageGrades.length; i++) {
            double variance = 0;
            for (int j = 0; j < AverageGrades.length; j++) {
                variance = variance + Math.pow(AverageGrades[i] - GGA[j][i], 2);
            }
            variance = variance/GGA.length;
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

// calculate avg grade per student
    public static double[] AverageStudentGrade(double[][] GGA) {
        double[] AverageStudentGrades = new double[GGA.length];
        for (int i = 0; i < GGA.length; i++) {
            double average = 0;
            for (int j = 0; j < GGA[0].length; j++) {
                average = average + GGA[i][j];
            }
            average = average / GGA[0].length;
            AverageStudentGrades[i] = average;
        }
        return AverageStudentGrades;
    }

    // find who graduated cum laude (8.5 or higher)
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

    // show how many students fall in each grade range per subject
    public static void gradeDistributionBySubject(double[][] GGA, String[] CourseNamesArray) {
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

        // count how many grades fall into each range
        for (int i = 0; i < GGA[0].length; i++) {
            for (int j = 0; j < GGA.length; j++) {
                if (GGA[j][i] >= 5.0 && GGA[j][i] < 6.0) {
                    gradeDistribution[0][i]++;
                } else if (GGA[j][i] >= 6.0 && GGA[j][i] < 7.0) {
                    gradeDistribution[1][i]++;
                } else if (GGA[j][i] >= 7.0 && GGA[j][i] < 8.0) {
                    gradeDistribution[2][i]++;
                } else if (GGA[j][i] >= 8.0 && GGA[j][i] < 9.0) {
                    gradeDistribution[3][i]++;
                } else if (GGA[j][i] >= 9.0 && GGA[j][i] <= 10.0) {
                    gradeDistribution[4][i]++;
                }
            }
        }

        // print results for each subject
        for (int subject = 0; subject < gradeDistribution[0].length; subject++) {
            System.out.println("subject: " + CourseNamesArray[subject]);
            for (int k = 0; k < gradeDistribution.length; k++) {
                System.out.println("  " + ranges[k] + " : " + gradeDistribution[k][subject]);
            }
            System.out.println();
        }
    }

    // check which course separates top performers from the rest
    public static void coursesThatSeparateTop(double[][] GGA, String[] CourseNamesArray) {
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

        // compare averages for each subject between top and rest
        for (int s = 0; s < GGA[0].length; s++) {
            double topSum = 0.0;
            for (int t = 0; t < TopIDs.size(); t++) {
                int id = TopIDs.get(t);
                topSum = topSum + GGA[id][s];
            }
            double topMean = TopIDs.size() == 0 ? Double.NaN : topSum / TopIDs.size();

            double restSum = 0.0;
            for (int r = 0; r < RestIDs.size(); r++) {
                int id2 = RestIDs.get(r);
                restSum = restSum + GGA[id2][s];
            }
            double restMean = RestIDs.size() == 0 ? Double.NaN : restSum / RestIDs.size();

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
	// Your new method should start here
}

