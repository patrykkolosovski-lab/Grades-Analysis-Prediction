import java.io.File;
import java.util.Scanner;

public class dataCollector {
    private String filename;
    public String[][] GraduateGradesArray;
    public String[] CourseNamesArray;
    public dataCollector(String filename) {
        this.filename = filename;
        createArrays(filename);
    }
    private void createArrays(String filename) {
        try {
            File file=new File(filename); // Creating a File object so we can create code that reads the file
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

			GraduateGradesArray = new String[rows][columns]; // This is the 2D array that we create, it is an array of doubles because the code given to us mostly uses doubles
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

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
