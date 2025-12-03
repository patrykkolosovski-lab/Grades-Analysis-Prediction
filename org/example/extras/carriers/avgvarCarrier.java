package org.example.extras.carriers;

public class avgvarCarrier {
    private double avg;
    private int totalgrades;
    public avgvarCarrier(double avg, int totalgrades) {
        this.avg = avg;
        this.totalgrades = totalgrades;
    }
    public double getAvg() {
        return avg;
    }
    public int getTotalgrades() {
        return totalgrades;
    }
}
