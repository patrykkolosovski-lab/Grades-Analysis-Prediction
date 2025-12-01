package org.example;

import java.util.ArrayList;
import java.util.List;

public class dataProcessing {
    private int countNG = 0;
    
    private int countpass = 0;

    public double[] AverageGrades(String[][] GGA) {
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
                        // countNG++;
                    }
                }
			}
			average = average/counter;
			AverageGrades[i] = average;
            // System.out.println(countNG);
            // countNG = 0;
		}
		// for (int i = 0; i < AverageGrades.length; i++) {
		//	System.out.println(i + " " + AverageGrades[i]);
		// }
        
		return AverageGrades;
	}

    //Find the highest Grade
    public int[] Findhighest(String[][] GGA) {
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
    public int[] FindhighestGeneral(double[] array) {
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
    public int[] Findlowest(String[][] GGA) {
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
    public double[] min(String[][] GGA) {
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

    //Checking for the maaximum grade for every course that each studentId has:
    public double[] max(String[][] GGA) {
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

    public double[] variance(String[][] GGA) {
        double[] AverageGrades = AverageGrades(GGA);
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
    public double[] range(String[][] GGA){
        double[] MaximumGrades = max(GGA);
        double[] MinimumGrades = min(GGA);
        int LENGTH= MaximumGrades.length;
        double range[];
        range=new double[LENGTH];

        for(int i=0; i<LENGTH ;i++){
            range[i]=MaximumGrades[i]-MinimumGrades[i];
        }
        return range;
    }

    //Calculating the amplitude by (MAX-MIN)/2
    public double[] amplitude(String[][] GGA){
        double[] MaximumGrades = max(GGA);
        double[] MinimumGrades = min(GGA);
        int LENGTH= MaximumGrades.length;
        double amp[];
        amp=new double[LENGTH];

        for(int i=0; i<LENGTH ;i++){
            amp[i]=(MaximumGrades[i]-MinimumGrades[i])/2;
        }
        return amp;
    }

    // Calculate avg grade per student
    public double[] AverageStudentGrades(String[][] GGA) {
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
                        // countNG++;
                    }
                }
                
            }
            average = average / counter;
            AverageStudentGrades[i] = average;
            // System.out.println(countNG);
            if (average >= 6.0) {
                // countpass++;
                // System.out.println(countpass);
            }
            // countNG = 0;
            
        }
        return AverageStudentGrades;
    }

    //Find who graduated cum laude (8.5 or higher)
    public void cumLaude(String[][] GGA) {
        double[] AverageStudentGrades = AverageStudentGrades(GGA);
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
    public void gradeDistributionBySubject(String[][] GGA, String[] CourseNamesArray) {
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
    public void coursesThatSeparateTop(String[][] GGA, String[] CourseNamesArray) {
        double[] averages = AverageStudentGrades(GGA);

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
}
