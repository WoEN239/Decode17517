package org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture;

public class WheelValueMap {
    public final double lf;
    public final double rf;
    public final double rb;
    public final double lb;

    public WheelValueMap(double lf, double rf, double rb, double lb) {
        this.lf = lf;
        this.rf = rf;
        this.rb = rb;
        this.lb = lb;
    }

    public WheelValueMap plus(WheelValueMap b){
        return new WheelValueMap(lf+b.lf, rf +b.rf,rb+b.rb,lb+b.lb);
    }
}
