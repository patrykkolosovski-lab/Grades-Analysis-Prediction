package org.example.extras;

import org.example.extras.carriers.avgvarCarrier;

public class step3Helper {
    String[][] CG;
    public step3Helper(String[][] CG) {
        this.CG = CG;
    }
    public avgvarCarrier averagePerStudent(int[] SItemp) {
        int totalgrades = 0;
        double totalsum=0;
        for(int m=0;m<SItemp.length;m++){
            int studentindex=SItemp[m];
            for(int j=0;j<CG[0].length;j++){
                if(CG[studentindex][j]!=null&&!CG[studentindex][j].equals("NG")){

                    totalsum+=Double.parseDouble(CG[studentindex][j]);
                    totalgrades++;}

            }
        }

        double avg=(totalgrades>0)? (totalsum/totalgrades):0; //Calculating the AVG per student with the specific feature(if-then)

        avgvarCarrier avgvarcarrier = new avgvarCarrier(avg, totalgrades);

        return avgvarcarrier;
    }

    public double variancePerStudent(int[] SItemp, int totalgrades, double avg) {
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
        return var;
    }
}
