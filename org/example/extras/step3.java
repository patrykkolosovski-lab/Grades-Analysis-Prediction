package org.example.extras;

import org.example.extras.carriers.avgvarCarrier;
import org.example.tools.Splitter;

public class step3 {
    public int countHas = 0;
    public int countHasnt = 0;
    public void STEP3(String[][] CG,String[][] SI){
        //Part 1
        //Avg for feature1
        String ActualFeature="Stable";

        Splitter filterer = new Splitter(ActualFeature);

        //Avg for those who have the feature
        int[] SItemp = filterer.getStudentIndexWithFeature(SI);
        step3Helper step3helper = new step3Helper(CG);

        //Step 3:calculating the average of those that have the feature
        avgvarCarrier result = step3helper.averagePerStudent(SItemp);
        double avg = result.getAvg();

        //Step 4:calculating the variance of those that have the feature
        double var = step3helper.variancePerStudent(SItemp, result.getTotalgrades(), result.getAvg());

        System.out.println("The AVERAGE of the students that DO share the feature "+ActualFeature+" is "+avg);
        System.out.println("The VARIANCE of the students that DO share the feature "+ActualFeature+" is "+var);

        //Avg for those who do not have the feature
        int[] SItemp2 = filterer.getStudentIndexWithNotFeature(SI);

        //Step 3:calculating the average of those that have the feature
        avgvarCarrier result2 = step3helper.averagePerStudent(SItemp2);
        double avg2 = result2.getAvg();

        //Step 4:calculating the variance of those that have the feature
        double var2 = step3helper.variancePerStudent(SItemp2, result2.getTotalgrades(), result2.getAvg());

        System.out.println();
        System.out.println("The AVERAGE of the students that DO NOT share the feature "+ActualFeature+" is "+avg2);
        System.out.println("The VARIANCE of the students that DO NOT share the feature "+ActualFeature+" is "+var2);
        System.out.println();
        System.out.println("The difference in AVERAGE for those who have the feature "+ActualFeature+" and those who dont have the feature is "+(Math.abs(avg2-avg)));
        System.out.println("The difference in VARIANCE for those who have the feature "+ActualFeature+" and those who dont have the feature is "+(Math.abs(var2-var)));

    }


    public void STEP3PART2(String[][] CG){
//Part 2
        //Avg for feature1
        int FeatureColumn=1;
        double ActualNumericFeature=6.0;
        Splitter splitter = new Splitter(ActualNumericFeature, FeatureColumn);

        int[] SItemp = splitter.getNumericFeature(CG);
        step3Helper step3helper = new step3Helper(CG);

        //Step 3:calculating the average of those that have the feature
        avgvarCarrier result = step3helper.averagePerStudent(SItemp);
        double avg = result.getAvg();

        //Step 4:calculating the variance of those that have the feature
        double var = step3helper.variancePerStudent(SItemp, result.getTotalgrades(), result.getAvg());

        System.out.println("The AVERAGE of the students that DO share the numeric feature > "+ActualNumericFeature+" is "+avg);
        System.out.println("The VARIANCE of the students that DO share the numeric feature > "+ActualNumericFeature+" is "+var);

        //Avg for those who do not have the feature
        int[] SItemp2 = splitter.getNotNumericFeature(CG);

        //Step 3:calculating the average of those that have the feature
        avgvarCarrier result2 = step3helper.averagePerStudent(SItemp2);
        double avg2 = result2.getAvg();

        //Step 4:calculating the variance of those that have the feature
        double var2 = step3helper.variancePerStudent(SItemp2, result2.getTotalgrades(), result2.getAvg());

        System.out.println();
        System.out.println("The AVERAGE of the students that DO NOT share the numeric feature >"+ActualNumericFeature+" is "+avg2);
        System.out.println("The VARIANCE of the students that DO NOT share the numeric feature >"+ActualNumericFeature+" is "+var2);
        System.out.println();
        System.out.println("The difference in AVERAGE for those who have the numeric feature >"+ActualNumericFeature+" and those who dont have the feature is "+Math.abs(avg2-avg));
        System.out.println("The difference in VARIANCE for those who have the numeric feature >"+ActualNumericFeature+" and those who dont have the feature is "+Math.abs(var2-var));
    }


    public void STEP3PART3 (String[][] CG,String[][] SI, int coursecolumn, String[] CourseNamesArray){
//given a course this code will find the best property to reduce variance
//!!!! in our results the variance reduction will be store according to the indexes of this ARRAY//!!!!!
        String[] features = new String[] {"Stable", "Fractured", "Chaotic", "Coherent", "Resonant", "none", "Harmonized", "1 ns/mc", "2 ns/mc", "3 ns/mc", "Silver", "Crimson", "White-Blue", "Violet", "Turquiose"};
// this is where you input your course and the index of the course
        double[][] results = new double[features.length][4];
// variance reduction, weighted variance, avg with, avg without
        double course_avg = 0;
        double grade_count = 0;
// calculate overall avg



//This for loop calculates the variance after splitting on features
        for (int j=0; j<features.length; j++){
            int FeatureColumn;
            if (j < 5) {
                FeatureColumn = 0; // Quantum Coherence Threshold
            } else if (j < 7) {
                FeatureColumn = 1; // Symbiotic Network Compatibility
            } else if (j < 10) {
                FeatureColumn = 2; // Astro-Temporal Drift Resistance
            } else if (j < 11) {
                FeatureColumn = 3; // Psionic Interference Tolerance (currently ignored)
            } else {
                FeatureColumn = 4; // Bio-Luminal Transmission
            }


            String ActualFeature=features[j];


            int count=0;
            int count_without = 0;



            for(int i = 0; i < SI.length; i++) {
                if(i >= CG.length || CG[i][coursecolumn] == null || SI[i][FeatureColumn] == null || CG[i][coursecolumn].equals("NG")) { continue; }

                if(SI[i][FeatureColumn].trim().equalsIgnoreCase(ActualFeature)) {
                    count++;
                } else {
                    count_without++;
                }
            }

            //We create arrays with indexes: 1 with prop, 2 without
            int[] withFeature = new int[count];
            int[] withoutFeature = new int[count_without];

            //Step 2:filling the array with the indexes of the students that share the same properties and we store the rest in a separate array
            int index=0;
            int index_WO=0;
            for (int i = 0; i < SI.length; i++) {
                if (i >= CG.length || CG[i][coursecolumn] == null  || SI[i][FeatureColumn] == null || CG[i][coursecolumn].equals("NG")) {
                    continue;
                }
                if(SI[i][FeatureColumn].trim().equalsIgnoreCase(ActualFeature)){
                    withFeature[index]=i;
                    index++;
                } else {
                    withoutFeature[index_WO]=i;
                    index_WO++;
                }
            }


            double course_avg_S = 0;
            double grade_count_S = 0;

//avg with feature
            for (int i =0 ; i < withFeature.length; i ++) {
                int studentid = withFeature[i];
                if(!CG[studentid][coursecolumn].equals("NG")){

                    course_avg_S = course_avg_S + Double.parseDouble(CG[studentid][coursecolumn]);
                    grade_count_S++;
                }

            }
            if (grade_count_S == 0) {
                continue;
            }
            course_avg_S = course_avg_S/grade_count_S;

//avg without feature
            double course_avg_O = 0;
            int grade_count_O =0;

            for (int i =0 ; i < withoutFeature.length; i ++) {
                int studentid = withoutFeature[i];
                if(!CG[studentid][coursecolumn].equals("NG")){
                    course_avg_O = course_avg_O + Double.parseDouble(CG[studentid][coursecolumn]);
                    grade_count_O++;
                }
            }
            course_avg_O = course_avg_O/grade_count_O;
//var with feature
            double varall_S =0;
            for (int i =0 ; i < withFeature.length; i++) {
                int studentid = withFeature[i];
                if(!CG[studentid][coursecolumn].equals("NG"))
                {
                    varall_S = varall_S + Math.pow((course_avg_S - Double.parseDouble(CG[studentid][coursecolumn])),2);

                }
            }
            varall_S= varall_S / grade_count_S;
// var without
            double varall_O =0;

            for (int i =0 ; i < withoutFeature.length; i ++) {
                int studentid = withoutFeature[i];
                if(!CG[studentid][coursecolumn].equals("NG")){
                    varall_O = varall_O + Math.pow((course_avg_O - Double.parseDouble(CG[studentid][coursecolumn])),2);
                }

            }
            varall_O= varall_O / grade_count_O;

// Inside variancereduction, remove the global course_avg/varall block,
// and after you build withFeature[] and withoutFeature[] for a given feature:

// Build the eligible index list = union of with/without groups
            int[] eligible = new int[withFeature.length + withoutFeature.length];
            int counter = 0;
            for (int t = 0; t < withFeature.length; t++) eligible[counter++] = withFeature[t];
            for (int t = 0; t < withoutFeature.length; t++) eligible[counter++] = withoutFeature[t];


// Compute overall mean and variance
            double parentSum = 0.0;
            int parentN = 0;
            for (int idx2 = 0; idx2 < eligible.length; idx2++) {
                int s = eligible[idx2];
                if (CG[s][coursecolumn] != null && !CG[s][coursecolumn].equals("NG")) {
                    parentSum += Double.parseDouble(CG[s][coursecolumn]);
                    parentN++;
                }
            }
            double parentMean = (parentN > 0) ? (parentSum / parentN) : 0.0;
            double varall = 0.0;
            for (int idx2 = 0; idx2 < eligible.length; idx2++) {
                int s = eligible[idx2];
                if (CG[s][coursecolumn] != null && !CG[s][coursecolumn].equals("NG")) {
                    double v = Double.parseDouble(CG[s][coursecolumn]);
                    varall += Math.pow(parentMean - v, 2);
                }
            }
            varall = (parentN > 0) ? (varall / parentN) : 0.0;

// calculate var reduction
            double allgrades = grade_count_S + grade_count_O;
            double weighted_var =0;
            weighted_var = ((grade_count_S * varall_S) + (grade_count_O * varall_O))/allgrades;
            double variance_reduction = varall - weighted_var;
            results[j][0]= variance_reduction;
            results[j][1] = weighted_var;
            results[j][2]= course_avg_S; // avg with
            results[j][3] = course_avg_O ; // avg without



        }
        // RESULTS //
        int best_index =-1;
        double max_reduction = -1;
        for (int i =0; i < features.length; i++){
            if (results[i][0]>max_reduction){
                max_reduction = results[i][0];
                best_index=i;}
        }
        System.out.println("The best feature to split on "+ CourseNamesArray[coursecolumn] + " is " + features[best_index]);
        System.out.println("The variance reduction is " + results[best_index][0]);
        System.out.println("If " + features[best_index] + " then grade = " + results[best_index][2] + " else grade = " + results[best_index][3]);

        for (int i = 0; i < SI.length; i++) {
            STEP3PART4(i, results, best_index, SI, features);
        }
        System.out.println("Number of students with feature = " + countHas);
        System.out.println("Number of students without feature = " + countHasnt);
        countHas = 0;
        countHasnt = 0;
    }

    public void STEP3PART4(int studentID, double[][] results, int best_index, String[][] SI, String[] features) {
        boolean hasFeature = false;
        int FeatureColumn = 0;
        if (best_index < 5) {
            FeatureColumn = 0; // Quantum Coherence Threshold
        } else if (best_index < 7) {
            FeatureColumn = 1; // Symbiotic Network Compatibility
        } else if (best_index < 10) {
            FeatureColumn = 2; // Astro-Temporal Drift Resistance
        } else if (best_index < 11) {
            FeatureColumn = 3; // Psionic Interference Tolerance (currently ignored)
        } else {
            FeatureColumn = 4; // Bio-Luminal Transmission
        }

        if (SI[studentID][FeatureColumn].equalsIgnoreCase(features[best_index])) {
            hasFeature = true;
        }



        if (hasFeature) {
            System.out.println(studentID + "'s predicted grade for the given course is " + results[best_index][2]);
            // countHas++;
        } else {
            System.out.println(studentID + "'s predicted grade for the given course is " + results[best_index][3]);
            // countHasnt++;
        }
    }
}
