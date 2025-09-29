import java.io.File;
import java.util.Scanner;

public class FileDisplayer {

    public static void main(String[] args) {
	

        try {
        	// Adapt this when you want to read and display a different file.
            String fileName = "GraduateGrades.csv";
            File file=new File(fileName);
            
			
            // This code uses two Scanners, one which scans the file line per line
            Scanner fileScanner1 = new Scanner(file);
			Scanner fileScanner2 = new Scanner(file);
			Scanner fileScanner3 = new Scanner(file);


			int lines = -1;
			int linesDone2 = 0;
			int columnNumber = 0;
		

			while (fileScanner1.hasNextLine()) {
            		String line = fileScanner1.nextLine();
            		// and one that scans the line entry per entry using the commas as delimiters
            		Scanner lineScanner = new Scanner(line);
                	lineScanner.useDelimiter(",");
				
            		while (lineScanner.hasNext() && !lineScanner.hasNextInt()) {
						lineScanner.next();
						columnNumber++;
            		}
            		lineScanner.close();	
            }

			fileScanner1.close();

			while (fileScanner2.hasNextLine() && lines < 25) {
				lines++;
			}

			double[][] doublearray = new double[lines][columnNumber];

            
            while (fileScanner3.hasNextLine() && linesDone2 < 25) {

            	String line = fileScanner3.nextLine();
            	// and one that scans the line entry per entry using the commas as delimiters
            	Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");
				linesDone2++;
				
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
            
            fileScanner3.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
