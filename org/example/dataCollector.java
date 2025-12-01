package org.example;

import java.io.File;
import java.util.Scanner;

public class dataCollector {
    public String[][] GraduateGradesArray;
    public String[] CourseNamesArray;
    public dataCollector(String filename) {
        createArrays(filename);
    }
    private void createArrays(String filename) {
        try {
            File file=new File(filename); // Creating a File object so we can create code that reads the file
        	Scanner columnScanner = new Scanner(file); // This scanner is made to count the number of columns in the csv file
			Scanner rowScanner = new Scanner(file); // This scanner is made to count the number of rows in the csv file
			Scanner elementScanner = new Scanner(file); // This scanner is made to record the elements from the csv file into the 2d array

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
            lineScanner.close();
			columnScanner.close();

			// The following is a while loop that counts the number of rows based on just going line by line in the file and counting how many jumps are made
			while (rowScanner.hasNextLine()) {
				rows++;
				rowScanner.nextLine();
			}

			GraduateGradesArray = new String[rows][columns]; // This is the 2D array that we create, it is an array of Strings to account for NGs
			CourseNamesArray = new String[columns]; // This is the array that stores the course names according to their courseID
            
			// The following while loop fills the array with the grades for each course for each student
            while (elementScanner.hasNextLine()) {
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
