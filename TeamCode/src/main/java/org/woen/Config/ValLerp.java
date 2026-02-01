package org.woen.Config;

public class ValLerp {
    public double hiDist;
    public double lowDist;
    public double hiVal;
    public double lowVal;

    public ValLerp(double lowDist, double hiDist, double lowVal, double hiVal) {
        this.hiDist = hiDist;
        this.lowDist = lowDist;
        this.hiVal = hiVal;
        this.lowVal = lowVal;
    }

    public double get(double dist){
        if(dist<lowDist){ dist = lowDist; }
        if(dist>hiDist){  dist = hiDist; }

        return lowVal + (dist - lowDist) * (hiVal-lowVal);
    }
}
