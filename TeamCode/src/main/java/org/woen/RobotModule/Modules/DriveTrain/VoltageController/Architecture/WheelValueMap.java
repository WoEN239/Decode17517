package org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture;

import androidx.annotation.NonNull;

public class WheelValueMap {
    public final Double lf;
    public final Double rf;
    public final Double rb;
    public final Double lb;

    public WheelValueMap(Double lf, Double rf, Double rb, Double lb) {
        this.lf = lf;
        this.rf = rf;
        this.rb = rb;
        this.lb = lb;
    }

    public WheelValueMap plus(WheelValueMap b){
        return new WheelValueMap(lf+b.lf, rf +b.rf,rb+b.rb,lb+b.lb);
    }

    public WheelValueMap multiply(double k){
        return new WheelValueMap(lf*k,rf*k,rb*k,lb*k);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("lf %.3f rf %.3f rb %.3f lb %.3f", lf,rf,rb,lb);
    }
}
