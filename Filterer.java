public class Filterer{
    public static int[] getStudentIndexWithFeature(String ActualFeature, String[][] SI) {
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
}