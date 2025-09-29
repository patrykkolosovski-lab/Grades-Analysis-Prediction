import java.io.File;
import java.util.Scanner;

public class FileDisplayer {

    public static void main(String[] args) {
	

        try {
        	// Adapt this when you want to read and display a different file.
            String fileName = "GraduateGrades.csv";
            File file=new File(fileName);
            
			
            // This code uses two Scanners, one which scans the file line per line
            Scanner fileScanner = new Scanner(file);

			int lines = 0;
            int linesDone = 0;
			int linesDone2 = 0;
			int columnNumber = 0;
			boolean shouldBreak = false;

			
			while (fileScanner.nextLine() != null) {
				lines++;
			}

			while (fileScanner.hasNextLine() && linesDone2 < 25) {
				if (shouldBreak == false) {
					
            		String line = fileScanner.nextLine();
            		linesDone2++;
            		// and one that scans the line entry per entry using the commas as delimiters
            		Scanner lineScanner = new Scanner(line);
                	lineScanner.useDelimiter(",");
				
            		while (lineScanner.hasNext()) {
						columnNumber++;
						if (lineScanner.hasNextInt() || lineScanner.hasNextBoolean()) {
							shouldBreak = true;
						}
            		}
            
            		lineScanner.close();	
				}
            }

			double[][] doublearray = new double[lines][columnNumber];

            
            while (fileScanner.hasNextLine() && linesDone < 25) {

            	String line = fileScanner.nextLine();
            	linesDone++;
            	// and one that scans the line entry per entry using the commas as delimiters
            	Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");
				
            	while (lineScanner.hasNext()) {
            		// Separate commands can be used depending on the types of the entries
            		// (i) and (s) are added to the printout to show how each entry is recognized
            		if (lineScanner.hasNextInt()) {
            			int i = lineScanner.nextInt();
						double x = ((double)i);
            			
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
            
            fileScanner.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
