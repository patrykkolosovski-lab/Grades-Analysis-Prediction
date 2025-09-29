import java.io.File;
import java.util.Scanner;

public class FileDisplayer {

    public static void main(String[] args) {
	

        try {
            String fileName = "GraduateGrades.csv"; // The name of the file you want to read
            File file=new File(fileName); // Creating a File object so we can create code that reads the file
            
			
            Scanner columnScanner = new Scanner(file); // This scanner is made to count the number of columns in the csv file
			Scanner rowScanner = new Scanner(file); // This scanner is made to count the number of columns in the csv file
			Scanner elementScanner = new Scanner(file); // This canner is made to record the elements from the csv file into the 2d array


			int rows = -1; // This is the variable that stores the number of rows in the data, note that it starts as -1 because it removes the line that states the column names
			int columns = 0; // this is the variable that stores the number of columns in the data
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
			while (rowScanner.hasNextLine() && rows < 25) { // Note that it stops after 25, this is because we have thousands of rows and right now we're just writing and testing the code
				rows++;
			}

			double[][] doublearray = new double[rows][columns]; // This is the 2D array that we create, it is an array of doubles because the code given to us mostly uses doubles


			// After this is all code given to us on Canvas
            
            while (elementScanner.hasNextLine() && linesDone < 25) {

            	line = elementScanner.nextLine();
            	// and one that scans the line entry per entry using the commas as delimiters
            	lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");
				linesDone++;
				
            	while (lineScanner.hasNext()) {
            		// Separate commands can be used depending on the types of the entries
            		// (i) and (s) are added to the printout to show how each entry is recognized
            		if (lineScanner.hasNextInt()) {
            			int i = lineScanner.nextInt();
            			System.out.print("(i)" + i + " ");
            		} else if (lineScanner.hasNextDouble()) {
            			double d = lineScanner.nextDouble();
            			System.out.print("(d)" + d + " ");
            		} else {
            			String s = lineScanner.next();
            			System.out.print("(s)" + s + " ");

            		}
            	}
            
            	lineScanner.close();
            	System.out.println();
            }
            
            elementScanner.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
