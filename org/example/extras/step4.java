package org.example.extras;

public class step4 {

    public static int STEP4_featureIndexToCol(int j) {
        // the same function as the if statements in STEP3PART4
        if (j < 5) return 0;
        else if (j < 7) return 1;
        else if (j < 10) return 2;
        else if (j < 11) return 3;
        else return 4;
    }


    public static double STEP4(String[][] SI, String[] features, double[][] results, int coursecolumn, int studentID, boolean returnResults) {

        int k = 10; // number of top results
        int[] topIdx = new int[k]; // array for indexes of top results
        boolean[] used = new boolean[results.length];
        int found = 0;

        for (int i = 0; i < k; i++) {
            int bestFeature = -1;
            double bestVarianceReduction = -1;

            // scan all features and pick the max unused variance reduction
            for (int j = 0; j < results.length; j++) {
                if (used[j]) continue;
                double vr = results[j][0];
                if (vr > bestVarianceReduction && vr > 0) {
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

            // STEP4_featureIndextToCol(j) finds the column we need to search through
            int featureCol = STEP4_featureIndexToCol(j);

            // get the student's feature
            String studentVal = SI[studentID][featureCol];

            // check whether the student has the feature
            boolean hasFeature = studentVal.trim().equalsIgnoreCase(features[j]);

            // get the prediction of each individual stump
            // if the student has the feature get the avg for the group with feature, otherwise avg for the group without feature
            double pred;
            if(hasFeature){
                pred = results[j][2];
            } else{
                pred = results [j][3];
            }

            // compute weighted average if weight positive and prediction is valid
            if (!Double.isNaN(pred) && w > 0) {
                weightedSum += w * pred;
                weightTotal += w;
            }
        }

        double combinedPred = (weightedSum / weightTotal);

        if (returnResults) {
            return combinedPred;
        } else {
            System.out.printf("[Step 4] Combined prediction for student %d (course column %d) = %f\n", studentID, coursecolumn, combinedPred);
            return 0;
        }
    }

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

    // compares the results from step3 and step4
    public static void compareSTEP3STEP4(String[][] SI,
                                         String[] features,
                                         double[][] results,
                                         int coursecolumn,
                                         int studentID,
                                         int best_index,
                                         int k,
                                         double actualGrade){
        double resultSTEP3 = step3prediction(SI, features, results, studentID, best_index);
        double resultSTEP4 = STEP4(SI, features, results, studentID, k, true);

        // residuals of estimated values from the actual values
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
