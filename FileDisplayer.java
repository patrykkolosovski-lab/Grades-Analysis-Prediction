import java.util.Arrays;

public class FileDisplayer {

    public String[] CourseNamesArray;
    
    public int countpass = 0;

    public static void main(String[] args) {
        //To run code for GG:java FileDisplayer.java GraduateGrades.csv(write this in terminal)
        //To run code for CG:java FileDIsplayer.java CurrentGrades.csv(write this in terminal)
        //args[0]
        String fileName = "GraduateGrades.csv"; // The name of the file you want to read

        System.out.println("DWAAWD");

        // Preparing objects for phase1step1+2
        dataCollector datacollector = new dataCollector(fileName);
        //dataProcessing dataprocessor = new dataProcessing();
        System.out.println("WDAA");

        // Testing phase1step 1 + 2 methods
        //double[] averagegrades = dataprocessor.AverageGrades(datacollector.GraduateGradesArray);
        //System.out.println(averagegrades[1] + "\n");

        // Preparing objects for phase1step3
        StudentInformationReader studentinformationreader = new StudentInformationReader();
        //step3 step3Object = new step3();

        // Testing phase1step 3 methods
        //step3Object.STEP3(datacollector.GraduateGradesArray, studentinformationreader.StudentInfoArray);
        System.out.println();

        // Phase 2 Test
        //Phase2(datacollector.GraduateGradesArray, studentinformationreader.StudentInfoArray);
        System.out.println();

        // Preparing objects for phase1step4
        dataChooser datachooser = new dataChooser(null, studentinformationreader.StudentInfoArray, 0);

        // Phase1Step4 Test
        STEP4(studentinformationreader.StudentInfoArray, datachooser.features, datacollector.GraduateGradesArray, 0, 0);
    }

    //Phase 2
    public static void Phase2(String[][] CurrentGrades,String[][] StudentInfoArray){

        //Course+feature from StudentInfo(if necessary)
        String Feature="Silver"; //set value to null to not check StudentInfo for a feature
        int Course=0;

        dataChooser test1=new dataChooser(CurrentGrades,StudentInfoArray,Course,Feature);
        dataChooser test2=new dataChooser(CurrentGrades,StudentInfoArray,Course);

        String[] PRINTTEST1=test1.getStudentGrades();
        String[] PRINTTEST2=test2.getStudentGrades();
        System.out.println(Arrays.toString(PRINTTEST1));
        System.out.println(Arrays.toString(PRINTTEST2));

    }

    public static int STEP4_featureIndexToCol(int j) {
        // the same function as the if statements in STEP3PART4
        if (j < 5) return 0;
        else if (j < 7) return 1;
        else if (j < 10) return 2;
        else if (j < 11) return 3;
        else return 4;
    }


    public static void STEP4(String[][] SI, String[] features, double[][] results, int coursecolumn, int studentID) {

        int k = 10; // the number of top results we want to find
        int[] topIdx = new int[k]; // hold the indeces for top k best features
        boolean[] used = new boolean[results.length]; // keep track of which j were already taken
        int found = 0;

        for (int i = 0; i < k; i++) {
            int bestFeature = -1;
            double bestVarianceReduction = -1;

            // scan all features and pick the max unused variance reduction (descending sorting mechanism)
            for (int j = 0; j < results.length; j++) {
                System.out.println("HMM");
                if (used[j]) continue;              // skip already chosen
                double vr = results[j][0];          // variance reduction for feature j
                if (vr > bestVarianceReduction && vr > 0) {        // only take positive reductions
                    System.out.println(j);
                    bestVarianceReduction = vr;
                    bestFeature = j;
                }
            }

            if (bestFeature == -1) break; // no more positive splits available
            topIdx[found++] = bestFeature;
            used[bestFeature] = true; // prevents picking the same feature index multiple times
        }

        // take the weighted average of the results of the stumps for a final result
        double weightedSum = 0.0;
        double weightTotal = 0.0;

        for (int l = 0; l < found; l++) {
            int j = topIdx[l];
            double w = results[j][0];  // I picked the weight of each result to be its variance reduction, since the higher the VR the better the split
                                       // Other alternative weight may be explored later

            // using the helper method find the column we need to search though
            int featureCol = STEP4_featureIndexToCol(j);

            // get the student value
            String studentVal = SI[studentID][featureCol];

            // check whether the student has the feature value
            boolean hasFeature = studentVal.trim().equalsIgnoreCase(features[j]);

            // get the prediction of each individual stump
            // if the student has the feature get the avg for the group with feature, otherwise avg for the group without feature
            double pred;
            if(hasFeature){
                System.out.println(l);
                pred = results[j][2];
            } else{
                System.out.println("else");
                pred = results [j][3];
            }

            // compute weighted average if weight positive and prediction is valid
            if (!Double.isNaN(pred) && w > 0) {
                weightedSum += w * pred;
                weightTotal += w;
            }
        }

        double combinedPred = (weightedSum / weightTotal);

        System.out.printf("[Step 4] Combined prediction for student %d (course column %d) = %f\n", studentID, coursecolumn, combinedPred);
    }

    // return prediction instead of printing (used to compare results in compareSTEP3STEP4)
    public static double step3prediction(String [][] SI,
                                         String[] features,
                                         double [][] results,
                                         int studentID,
                                         int best_index) {
        int col = STEP4_featureIndexToCol(best_index);
        String value = SI[studentID][col];
        double pred;
        boolean hasFeature = value.trim().equalsIgnoreCase(features[best_index]);

        if(hasFeature){
            pred = results[best_index][2];
        } else{
            pred = results [best_index][3];
        }
        return pred;

    }

    // return prediction instead of printing (used to compare results in compareSTEP3STEP4)
    // this is a copy of the STEP4 method but instead of printing returns a prediction
    public static double step4prediction(String [][] SI,
                                         String[] features,
                                         double [][] results,
                                         int studentID,
                                         int k) {
        int[] topIdx = new int[k]; // hold the indeces for top k best features
        boolean[] used = new boolean[results.length]; // keep track of which j were already taken
        int found = 0;

        for (int i = 0; i < k; i++) {
            int bestFeature = -1;
            double bestVarianceReduction = -1;

            // scan all features and pick the max unused variance reduction (descending sorting mechanism)
            for (int j = 0; j < results.length; j++) {
                if (used[j]) continue;              // skip already chosen
                double vr = results[j][0];          // variance reduction for feature j
                if (vr > bestVarianceReduction && vr > 0) {        // only take positive reductions
                    bestVarianceReduction = vr;
                    bestFeature = j;
                }
            }

            if (bestFeature == -1) break; // no more positive splits available
            topIdx[found++] = bestFeature;
            used[bestFeature] = true; // prevents picking the same feature index multiple times
        }

        // take the weighted average of the results of the stumps for a final result
        double weightedSum = 0.0;
        double weightTotal = 0.0;

        for (int l = 0; l < found; l++) {
            int j = topIdx[l];
            double w = results[j][0];  // I picked the weight of each result to be its variance reduction, since the higher the VR the better the split
            // Other alternative weight may be explored later

            // using the helper method find the column we need to search though
            int featureCol = STEP4_featureIndexToCol(j);

            // get the student value
            String studentVal = SI[studentID][featureCol];

            // check whether the student has the feature value
            boolean hasFeature = studentVal.trim().equalsIgnoreCase(features[j]);

            // get the prediction of each individual stump
            // if the student has the feature get the avg for the group with feature, otherwise avg for the group without feature
            double pred;
            if (hasFeature) {
                pred = results[j][2];
            } else {
                pred = results[j][3];
            }

            // compute weighted average if weight positive and prediction is valid
            if (!Double.isNaN(pred) && w > 0) {
                weightedSum += w * pred;
                weightTotal += w;
            }
        }
        double combinedPred = (weightedSum / weightTotal);
        return combinedPred;
    }

    // method that compares the results from step3 and step4
    public static void compareSTEP3STEP4(String[][] SI,
                                         String[] features,
                                         double[][] results,
                                         int coursecolumn,
                                         int studentID,
                                         int best_index,
                                         int k,
                                         double actualGrade){
        double resultSTEP3 = step3prediction(SI, features, results, studentID, best_index);
        double resultSTEP4 = step4prediction(SI, features, results, studentID, k);

        // calculate the residuals of estimated values from the actual values
        double residualSTEP3 = Math.abs(resultSTEP3 - actualGrade);
        double residualSTEP4 = Math.abs(resultSTEP4 - actualGrade);

        System.out.println("Student " + studentID + " (course col " + coursecolumn + ") ===");
        System.out.println("Actual grade: " + actualGrade);
        System.out.println("Step 3 prediction: " + resultSTEP3 + " Residual: " + residualSTEP3);
        System.out.println("Step 4 prediction: " + resultSTEP4 + " Residual: " + residualSTEP4);

        if(residualSTEP4<=residualSTEP3){
            System.out.println("Better prediction: Step 4");
        } else{
            System.out.println("Better prediction: Step 3");
        }
    }

}