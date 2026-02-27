package org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture;

import static java.lang.Math.abs;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

public class TankWheelValueMap {
    public final Double l;
    public final Double r;
    public TankWheelValueMap(Double l, Double r) {
        this.l = l;
        this.r = r;
    }

    public TankWheelValueMap plus(TankWheelValueMap b){
        return new TankWheelValueMap(l+b.l,r+b.r);
    }
    public TankWheelValueMap dot(TankWheelValueMap b){
        return new TankWheelValueMap(l*b.l, r*b.r);
    }
    public TankWheelValueMap multiply(double k){
        return new TankWheelValueMap(l*k,r*k);
    }
    public TankWheelValueMap border(TankWheelValueMap b){
        double borderedLf = l;
        double borderedRf = r;

        if(abs(l)<=b.l){
            borderedLf = 0;
        }
        if(abs(r)<=b.r){
            borderedRf = 0;
        }

        return new TankWheelValueMap(borderedLf,borderedRf);
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        return String.format("lf %.3f rf %.3f",l,r);
    }
}
