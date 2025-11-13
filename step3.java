public class step3 {
    public int countHas = 0;
    public int countHasnt = 0;
    public void STEP3(String[][] CG,String[][] SI){
    //Part 1
    //Avg for feature1
    int FeatureColumn=0;
    String ActualFeature="Stable";
    
    Filterer filterer = new Filterer(ActualFeature);
    int[] SItemp = filterer.getStudentIndexWithFeature(SI);

    //Step 3:calculating the average of those that have the feature
    double totalsum=0;
    int totalgrades=0;
    for(int m=0;m<SItemp.length;m++){
        int studentindex=SItemp[m];
        for(int j=0;j<CG[0].length;j++){
            if(CG[studentindex][j]!=null&&!CG[studentindex][j].equals("NG")){
            
                totalsum+=Double.parseDouble(CG[studentindex][j]);
                totalgrades++;}
            
        }
    }

    double avg=(totalgrades>0)? (totalsum/totalgrades):0; //Calculating the AVG per student with the specific feature(if-then)
    //Step 4:calculating the variance of those that have the feature
    double SquaredDiff=0;
    for(int m=0;m<SItemp.length;m++){
        int studentindex=SItemp[m];
        for(int j=0;j<CG[0].length;j++){
            if(CG[studentindex][j]!=null&&!CG[studentindex][j].equals("NG")){
            
                double grade=Double.parseDouble(CG[studentindex][j]);
                SquaredDiff=Math.pow(grade-avg, 2);}
            
        }
    }
    double var=(totalgrades>0)? (SquaredDiff/totalgrades):0; //Calculating the VARIANCE per student with the specific feature(if-then)
    
    System.out.println("The AVERAGE of the students that DO share the feature "+ActualFeature+" is "+avg);
    System.out.println("The VARIANCE of the students that DO share the feature "+ActualFeature+" is "+var);
    

    //Avg for those who do not have the feature
    int FeatureColumn2=FeatureColumn;
    String ActualFeature2=ActualFeature;
    
    //Step 1:finding out the length of the array
    int count2=0;
    for(int i=0;i<SI.length;i++){
        if(!SI[i][FeatureColumn2].equalsIgnoreCase(ActualFeature2)){
            count2++;
        }
    }
    //Step 2:filling the array with the indexes of the students that share the same properties
    int index2=0;
    int SItemp2[]=new int[count2];
    for(int i=0;i<SI.length;i++){
        if(!SI[i][FeatureColumn2].equalsIgnoreCase(ActualFeature2)){//negation
            SItemp2[index2]=i;
            index2++;
        }
    }

    //Step 3:calculating the average of those that have the feature
    double totalsum2=0;
    int totalgrades2=0;
    for(int m=0;m<SItemp2.length;m++){
        int studentindex2=SItemp2[m];
        for(int j=0;j<CG[0].length;j++){
            if(CG[studentindex2][j]!=null&&!CG[studentindex2][j].equals("NG")){
            
                totalsum2+=Double.parseDouble(CG[studentindex2][j]);
                totalgrades2++;}
            
        }
    }
    
    double avg2=(totalgrades2>0)? (totalsum2/totalgrades2):0; //Calculating the AVG per student without the specific feature(if-then)
    
    //Step 4:calculating the variance of those that have the feature
    double SquaredDiff2=0;
    for(int m=0;m<SItemp2.length;m++){
        int studentindex2=SItemp2[m];
        for(int j=0;j<CG[0].length;j++){
            if(CG[studentindex2][j]!=null&&!CG[studentindex2][j].equals("NG")){
            
                double grade2=Double.parseDouble(CG[studentindex2][j]);
                SquaredDiff2=Math.pow(grade2-avg2, 2);}
            
        }
    }

   
    double var2=(totalgrades2>0)? (SquaredDiff2/totalgrades2):0; //Calculating the VARIANCE per student without the specific feature(if-then)
    
    System.out.println("");
    System.out.println("The AVERAGE of the students that DO NOT share the feature "+ActualFeature2+" is "+avg2);
    System.out.println("The VARIANCE of the students that DO NOT share the feature "+ActualFeature2+" is "+var2);
    System.out.println("");
    System.out.println("The difference in AVERAGE for those who have the feature "+ActualFeature+" and those who dont have the feature is "+(Math.abs(avg2-avg)));
    System.out.println("The difference in VARIANCE for those who have the feature "+ActualFeature+" and those who dont have the feature is "+(Math.abs(var2-var)));

}

    
public void STEP3PART2(String[][] CG){
//Part 2
    //Avg for feature1
    int FeatureColumn=1;
    double ActualNumericFeature=6.0;
    
    //Step 1:finding out the length of the array
    int count=0;
    for(int i=0;i<CG.length;i++){
        if(CG[i][FeatureColumn]==null || CG[i][FeatureColumn].equals("NG")){continue;}
        Double grade=Double.parseDouble(CG[i][FeatureColumn]);
        if(grade>ActualNumericFeature){
            count++;
        }
    }
    
    //Step 2:filling the array with the indexes of the students that share the same properties
    int index=0;
    int SItemp[]=new int[count];
    for(int i=0;i<CG.length;i++){
        if(CG[i][FeatureColumn]==null || CG[i][FeatureColumn].equalsIgnoreCase("NG")){continue;}
        Double grade=Double.parseDouble(CG[i][FeatureColumn]);
        if(grade>ActualNumericFeature){
            SItemp[index]=i;
            index++;
        }
    }

    //Step 3:calculating the average of those that have the feature
    double totalsum=0;
    int totalgrades=0;
    for(int m=0;m<SItemp.length;m++){
        int studentindex=SItemp[m];
        for(int j=0;j<CG[0].length;j++){
            if(CG[studentindex][j]!=null&&!CG[studentindex][j].equals("NG")){
            
                totalsum+=Double.parseDouble(CG[studentindex][j]);
                totalgrades++;}
            
        }
    }

    double avg=(totalgrades>0)? (totalsum/totalgrades):0; //Calculating the AVG per student with the specific feature(if-then)
    
    //Step 4:calculating the variance of those that have the feature
    double SquaredDiff=0;
    for(int m=0;m<SItemp.length;m++){
        int studentindex=SItemp[m];
        for(int j=0;j<CG[0].length;j++){
            if(CG[studentindex][j]!=null&&!CG[studentindex][j].equals("NG")){
            
                double grade=Double.parseDouble(CG[studentindex][j]);
                SquaredDiff=Math.pow(grade-avg, 2);}
        }
    }
    double var=(totalgrades>0)? (SquaredDiff/totalgrades):0; //Calculating the VARIANCE per student with the specific feature(if-then)
    System.out.println("The AVERAGE of the students that DO share the numeric feature > "+ActualNumericFeature+" is "+avg);
    System.out.println("The VARIANCE of the students that DO share the numeric feature > "+ActualNumericFeature+" is "+var);

    //Avg for those who do not have the feature
    int FeatureColumn2=FeatureColumn;
    double ActualNumericFeature2=ActualNumericFeature;
    
    //Step 1:finding out the length of the array
    int count2=0;
    for(int i=0;i<CG.length;i++){
        if(CG[i][FeatureColumn2]==null || CG[i][FeatureColumn2].equalsIgnoreCase("NG")){continue;}
        Double grade=Double.parseDouble(CG[i][FeatureColumn2]);
        if(grade>ActualNumericFeature2){
            count2++;
        }
    }
    //Step 2:filling the array with the indexes of the students that share the same properties
    int index2=0;
    int SItemp2[]=new int[count2];
    for(int i=0;i<CG.length;i++){
        if(CG[i][FeatureColumn2]==null || CG[i][FeatureColumn2].equalsIgnoreCase("NG")){continue;}
        Double grade=Double.parseDouble(CG[i][FeatureColumn2]);
        if(grade<=ActualNumericFeature2){
            SItemp2[index2]=i;
            index2++;
        }
    }

    //Step 3:calculating the average of those that have the feature
    double totalsum2=0;
    int totalgrades2=0;
    for(int m=0;m<SItemp2.length;m++){
        int studentindex2=SItemp2[m];
        for(int j=0;j<CG[0].length;j++){
            if(CG[studentindex2][j]!=null&&!CG[studentindex2][j].equals("NG")){
            
                totalsum2+=Double.parseDouble(CG[studentindex2][j]);
                totalgrades2++;}
            
        }
    }
    
    double avg2=(totalgrades2>0)? (totalsum2/totalgrades2):0; //Calculating the AVG per student without the specific feature(if-then)
    
    //Step 4:calculating the variance of those that have the feature
    double SquaredDiff2=0;
    for(int m=0;m<SItemp2.length;m++){
        int studentindex2=SItemp2[m];
        for(int j=0;j<CG[0].length;j++){
            if(CG[studentindex2][j]!=null&&!CG[studentindex2][j].equals("NG")){
            
                double grade2=Double.parseDouble(CG[studentindex2][j]);
                SquaredDiff2=Math.pow(grade2-avg2, 2);}
            
        }
    }

   
    double var2=(totalgrades2>0)? (SquaredDiff2/totalgrades2):0; //Calculating the VARIANCE per student without the specific feature(if-then)
    
    System.out.println("");
    System.out.println("The AVERAGE of the students that DO NOT share the numeric feature >"+ActualNumericFeature2+" is "+avg2);
    System.out.println("The VARIANCE of the students that DO NOT share the numeric feature >"+ActualNumericFeature2+" is "+var2);
    System.out.println("");
    System.out.println("The difference in AVERAGE for those who have the numeric feature >"+ActualNumericFeature+" and those who dont have the feature is "+Math.abs(avg2-avg));
    System.out.println("The difference in VARIANCE for those who have the numeric feature >"+ActualNumericFeature+" and those who dont have the feature is "+Math.abs(var2-var));


}


public void STEP3PART3 (String[][] CG,String[][] SI, int coursecolumn, String[] CourseNamesArray){
//given a course this code will find the best property to reduce variance
//!!!! in our results the variance reduction will be store according to the indexes of this ARRAY//!!!!!
String features[] = new String[] {"Stable", "Fractured", "Chaotic", "Coherent", "Resonant", "none", "Harmonized", "1 ns/mc", "2 ns/mc", "3 ns/mc", "Silver", "Crimson", "White-Blue", "Violet", "Turquiose"};
// this is where you input your course and the index of the course 
double results[][] = new double[features.length][4]; 
// variance reduction, weighted variance, avg with, avg without
double course_avg = 0;
double grade_count = 0; 
// calculate overall avg



//This for loop calculates the variance after splitting on features 
   for (int j=0; j<features.length; j++){
    int FeatureColumn=0;
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
        int withFeature[] = new int[count];
        int withoutFeature[] = new int[count_without];    
        
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
int ei = 0;
for (int t = 0; t < withFeature.length; t++) eligible[ei++] = withFeature[t];
for (int t = 0; t < withoutFeature.length; t++) eligible[ei++] = withoutFeature[t];

// Guard: both groups must have at least one grade to avoid divide-by-zero
if (grade_count_S == 0 || grade_count_O == 0) {
    continue; // skip this feature; no valid split
}

// Compute overall (parent) mean/variance on the eligible set
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

STEP4(SI, features, results, coursecolumn, countHas);

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
        // System.out.println(studentID + "'s predicted grade for the given course is " + results[best_index][2]);
        countHas++;
    } else {
        // System.out.println(studentID + "'s predicted grade for the given course is " + results[best_index][3]);
        countHasnt++;
    }
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
