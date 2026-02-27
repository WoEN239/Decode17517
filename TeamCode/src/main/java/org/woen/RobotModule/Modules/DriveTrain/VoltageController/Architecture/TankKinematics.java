package org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture;

import org.woen.Util.Vectors.Pose;
public class TankKinematics {
    private final double B;
    private final double r;

    public TankKinematics(double b, double r) {
        B = b;
        this.r = r;
    }

    public TankWheelValueMap getWheel(double h, double x){
        return new TankWheelValueMap((x-h*B*0.5),(x+h*B*0.5));
    }

    public Pose getRobot(TankWheelValueMap w){
        return new Pose( (w.r-w.l)/B,(w.r+w.l)*0.5,0 );
    }
}
