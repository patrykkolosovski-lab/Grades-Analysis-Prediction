public class Filterer{

    public String feature;
    public int FeatureColumn;

    public Filterer(String feature) {
        this.feature = feature;
        dataChooser datachooser = new dataChooser(feature);
        this.FeatureColumn = datachooser.getFeatureColumn();
    }
    
    public int[] getStudentIndexWithFeature(String ActualFeature, String[][] SI) {
        dataChooser datachooser = new  dataChooser(ActualFeature);
        int FeatureColumn = datachooser.getFeatureColumn();
         //Step 1:finding out the length of the array
    int count=0;
    for(int i=0;i<SI.length;i++){
        if(SI[i][FeatureColumn].equalsIgnoreCase(ActualFeature)){
            count++;
        }
    }
    //Step 2:filling the array with the indexes of the students that share the same properties
    int index=0;
    int SItemp[]=new int[count];
    for(int i=0;i<SI.length;i++){
        if(SI[i][FeatureColumn].equalsIgnoreCase(ActualFeature)){
            SItemp[index]=i;
            index++;
        }
    }
    return SItemp;
    }


    public int[] getStudentIndexWithNotFeature(String ActualFeature2, String[][] SI){
        dataChooser dataChooser = new dataChooser(ActualFeature2);
        int FeatureColumn2 = dataChooser.getFeatureColumn();
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
    return SItemp2;
    }

    public int[] getNumericFeature(double ActualNumericFeature, String[][] SI, String[][] CG){
    
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
    return SItemp;
    }

    public int[] getNotNumericFeature(double ActualNumericFeature, String[][] SI, String[][] CG){
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
    return SItemp2;
    }
}