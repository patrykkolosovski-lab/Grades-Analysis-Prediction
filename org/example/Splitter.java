package org.example;
public class Splitter{

    public String feature;
    public int FeatureColumn;

    public double numericFeature;
    public int courseColumn;


    public Splitter(String feature) {
        this.feature = feature;
        dataChooser datachooser = new dataChooser(feature);
        this.FeatureColumn = datachooser.getFeatureColumn();
    }

    public Splitter(double numericFeature, int courseColumn) {
        this.numericFeature = numericFeature;
        this.courseColumn = courseColumn;
    }

    public int[] getStudentIndexWithFeature(String[][] SI) {
        dataChooser datachooser = new  dataChooser(feature);
        int FeatureColumn = datachooser.getFeatureColumn();
        //Step 1:finding out the length of the array
        int count=0;
        for(int i=0;i<SI.length;i++){
            if(SI[i][FeatureColumn].equalsIgnoreCase(feature)){
                count++;
            }
        }
        //Step 2:filling the array with the indexes of the students that share the same properties
        int index=0;
        int SItemp[]=new int[count];
        for(int i=0;i<SI.length;i++){
            if(SI[i][FeatureColumn].equalsIgnoreCase(feature)){
                SItemp[index]=i;
                index++;
            }
        }
        return SItemp;
    }

    public int[] getStudentIndexWithNotFeature(String[][] SI){
        dataChooser dataChooser = new dataChooser(feature);
        int FeatureColumn2 = dataChooser.getFeatureColumn();
        //Step 1:finding out the length of the array
        int count2=0;
        for(int i=0;i<SI.length;i++){
            if(!SI[i][FeatureColumn2].equalsIgnoreCase(feature)){
                count2++;
            }
        }
        //Step 2:filling the array with the indexes of the students that share the same properties
        int index2=0;
        int SItemp2[]=new int[count2];
        for(int i=0;i<SI.length;i++){
            if(!SI[i][FeatureColumn2].equalsIgnoreCase(feature)){//negation
                SItemp2[index2]=i;
                index2++;
            }
        }
        return SItemp2;
    }

    public int[] getNumericFeature(String[][] CG){

        //Step 1:finding out the length of the array
        int count=0;
        for(int i=0;i<CG.length;i++){
            if(CG[i][courseColumn]==null || CG[i][courseColumn].equals("NG")){continue;}
            Double grade=Double.parseDouble(CG[i][courseColumn]);
            if(grade>numericFeature){
                count++;
            }
        }

        //Step 2:filling the array with the indexes of the students that share the same properties
        int index=0;
        int SItemp[]=new int[count];
        for(int i=0;i<CG.length;i++){
            if(CG[i][courseColumn]==null || CG[i][courseColumn].equalsIgnoreCase("NG")){continue;}
            Double grade=Double.parseDouble(CG[i][courseColumn]);
            if(grade>numericFeature){
                SItemp[index]=i;
                index++;
            }
        }
        return SItemp;
    }

    public int[] getNotNumericFeature(String[][] CG){

        //Step 1:finding out the length of the array
        int count2=0;
        for(int i=0;i<CG.length;i++){
            if(CG[i][courseColumn]==null || CG[i][courseColumn].equalsIgnoreCase("NG")){continue;}
            Double grade=Double.parseDouble(CG[i][courseColumn]);
            if(grade>numericFeature){
                count2++;
            }
        }
        //Step 2:filling the array with the indexes of the students that share the same properties
        int index2=0;
        int SItemp2[]=new int[count2];
        for(int i=0;i<CG.length;i++){
            if(CG[i][courseColumn]==null || CG[i][courseColumn].equalsIgnoreCase("NG")){continue;}
            Double grade=Double.parseDouble(CG[i][courseColumn]);
            if(grade<=numericFeature){
                SItemp2[index2]=i;
                index2++;
            }
        }
        return SItemp2;
    }


    //PART 1
//GETTERS
//Calculates the Average of a course
    public double getStudentAverage(String[] grades){
        double sum=0;
        int count=0;
        for(String G: grades){
            if(G!=null && !G.equals("NG")){
                sum+=Double.parseDouble(G);
                count++;
            }
        }
        return (count > 0) ? (sum / count) : 0;

    }

    //Calculates the Variance of a course
    public double getStudentVariance(String[] grades){
        double avg = getStudentAverage(grades);
        double sumSquaredDiffs = 0;
        int count = 0;
        for(String G: grades){
            if(G!=null && !G.equals("NG")){
                double val = Double.parseDouble(G);
                sumSquaredDiffs += Math.pow(val - avg, 2);
                count++;
            }
        }
        return (count > 0) ? (sumSquaredDiffs / count) : 0;
    }

    //Calculates the Standard Deviation of a course
    public double getStandardDeviation(String[] grades){
        return Math.sqrt(getStudentVariance(grades));
    }

    //Calculates the lowest grade of a course
    public double getStudentMin(String[] grades){
        double min=Double.MAX_VALUE;

        for(String G: grades){
            if(G!=null && !G.equals("NG")){
                double VAL=Double.parseDouble(G);
                if(VAL<min){
                    min=VAL;
                }
            }
        }
        if(min==Double.MAX_VALUE){
            return 0;
        }else{
            return min;
        }
    }
    //Calculates the highest grade of a course
    public double getStudentMax(String[] grades){
        double max=Double.MIN_VALUE;

        for(String G: grades){
            if(G!=null && !G.equals("NG")){
                double VAL=Double.parseDouble(G);
                if(VAL>max){
                    max=VAL;
                }
            }
        }
        if(max==Double.MIN_VALUE){
            return 0;
        }else{
            return max;
        }
    }

    //Calculates the range of a course
    public double getRange(String[] grades){
        return getStudentMax(grades)-getStudentMin(grades);
    }




    //PART 2
//IMPLEMENTATION
    public int[] getGradesAboveAverage(String[][] CG, double threshold){
        //Step 1:finding out the length of the array
        int count=0;
        for(int i=0;i<CG.length;i++){
            if(getStudentAverage(CG[i])>threshold){
                count++;
            }
        }

        //Step 2:filling the array with the indexes of the students that share the same properties
        int index=0;
        int SItemp[]=new int[count];
        for(int i=0;i<CG.length;i++){
            if(getStudentAverage(CG[i])>threshold){
                SItemp[index]=i;
                index++;
            }
        }
        return SItemp;
    }

    public int[] getGradesBelowAverage(String[][] CG, double threshold){
        //Step 1:finding out the length of the array
        int count=0;
        for(int i=0;i<CG.length;i++){
            if(getStudentAverage(CG[i])<=threshold){
                count++;
            }
        }

        //Step 2:filling the array with the indexes of the students that share the same properties
        int index=0;
        int SItemp[]=new int[count];
        for(int i=0;i<CG.length;i++){
            if(getStudentAverage(CG[i])<=threshold){
                SItemp[index]=i;
                index++;
            }
        }
        return SItemp;
    }

    public int[] getGradesAboveVariance(String[][] CG, double threshold){
        //Step 1:finding out the length of the array
        int count=0;
        for(int i=0;i<CG.length;i++){
            if(getStudentVariance(CG[i])>threshold){
                count++;
            }
        }

        //Step 2:filling the array with the indexes of the students that share the same properties
        int index=0;
        int SItemp[]=new int[count];
        for(int i=0;i<CG.length;i++){
            if(getStudentVariance(CG[i])>threshold){
                SItemp[index]=i;
                index++;
            }
        }
        return SItemp;
    }

    public int[] getGradesBelowVariance(String[][] CG, double threshold){
        //Step 1:finding out the length of the array
        int count=0;
        for(int i=0;i<CG.length;i++){
            if(getStudentVariance(CG[i])<=threshold){
                count++;
            }
        }

        //Step 2:filling the array with the indexes of the students that share the same properties
        int index=0;
        int SItemp[]=new int[count];
        for(int i=0;i<CG.length;i++){
            if(getStudentVariance(CG[i])<=threshold){
                SItemp[index]=i;
                index++;
            }
        }
        return SItemp;
    }

    public int[] getGradesAboveStandardDeviation(String[][] CG, double threshold){
        //Step 1:finding out the length of the array
        int count=0;
        for(int i=0;i<CG.length;i++){
            if(getStandardDeviation(CG[i])>threshold){
                count++;
            }
        }

        //Step 2:filling the array with the indexes of the students that share the same properties
        int index=0;
        int SItemp[]=new int[count];
        for(int i=0;i<CG.length;i++){
            if(getStandardDeviation(CG[i])>threshold){
                SItemp[index]=i;
                index++;
            }
        }
        return SItemp;
    }

    public int[] getGradesBelowStandardDeviation(String[][] CG, double threshold){
        //Step 1:finding out the length of the array
        int count=0;
        for(int i=0;i<CG.length;i++){
            if(getStandardDeviation(CG[i])<=threshold){
                count++;
            }
        }

        //Step 2:filling the array with the indexes of the students that share the same properties
        int index=0;
        int SItemp[]=new int[count];
        for(int i=0;i<CG.length;i++){
            if(getStandardDeviation(CG[i])<=threshold){
                SItemp[index]=i;
                index++;
            }
        }
        return SItemp;
    }


    public int[] getGradesAboveRange(String[][] CG, double threshold){
        //Step 1:finding out the length of the array
        int count=0;
        for(int i=0;i<CG.length;i++){
            if(getStudentAverage(CG[i])>threshold){
                count++;
            }
        }

        //Step 2:filling the array with the indexes of the students that share the same properties
        int index=0;
        int SItemp[]=new int[count];
        for(int i=0;i<CG.length;i++){
            if(getRange(CG[i])>threshold){
                SItemp[index]=i;
                index++;
            }
        }
        return SItemp;
    }

    public int[] getGradesBelowRange(String[][] CG, double threshold){
        //Step 1:finding out the length of the array
        int count=0;
        for(int i=0;i<CG.length;i++){
            if(getStudentAverage(CG[i])<=threshold){
                count++;
            }
        }

        //Step 2:filling the array with the indexes of the students that share the same properties
        int index=0;
        int SItemp[]=new int[count];
        for(int i=0;i<CG.length;i++){
            if(getStudentAverage(CG[i])<=threshold){
                SItemp[index]=i;
                index++;
            }
        }
        return SItemp;
    }

    //Calculates how many NG grades are in a course
    public int[] getNgGrades(String [] grades){
        //Part1
        int count=0;
        for(String G: grades){
            if(G!=null && G.equals("NG")){
                count++;
            }
        }
        //Part2
        int index=0;
        int Indexes[]=new int[count];
        for(int i=0;i<grades.length;i++){
            String G=grades[i];
            if(G!=null && G.equals("NG")){
                Indexes[index]=i;
                index++;
            }
        }
        return Indexes;
    }

    public int[] getNotNGGrades(String[] grades) {
        //Part1
        int count=0;
        for(String G: grades){
            if(G!=null && !G.equals("NG")){
                count++;
            }
        }
        //Part2
        int index=0;
        int Indexes[]=new int[count];
        for(int i=0;i<grades.length;i++){
            String G=grades[i];
            if(G!=null && !G.equals("NG")){
                Indexes[index]=i;
                index++;
            }
        }
        return Indexes;
    }
}