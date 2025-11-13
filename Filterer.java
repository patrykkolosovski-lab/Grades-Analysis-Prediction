public class Filterer{

    public String feature;
    public int FeatureColumn;

    public double numericFeature;
    public int courseColumn;


    public Filterer(String feature) {
        this.feature = feature;
        dataChooser datachooser = new dataChooser(feature);
        this.FeatureColumn = datachooser.getFeatureColumn();
    }

    public Filterer(double numericFeature, int courseColumn) {
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
}