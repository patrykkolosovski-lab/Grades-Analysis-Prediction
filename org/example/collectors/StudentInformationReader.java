package org.example.collectors;

import java.io.File;
import java.util.Scanner;

public class StudentInformationReader {
    public String[][] StudentInfoArray;
    public StudentInformationReader(String fileName) {
        try {
            
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

			
			while (rowScanner.hasNextLine()) { 
				rows++;
				rowScanner.nextLine();
			}

			StudentInfoArray = new String[rows][columns]; 
			
            
			// The following while loop fills the array with the grades for each course for each student
            while (elementScanner.hasNextLine()) {
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
        
    } catch (Exception e){
        e.printStackTrace();}

    }
}
