package org.woen.Util.Trajectory.Config;

public class RobotDriveConstants {
    public final double maxVel ;
    public final double maxAccel ;
    public final double mass ;
    public final double maxAngularVel ;
    public final double maxCentralForce;

    public RobotDriveConstants(double maxVel, double maxAccel, double mass, double maxAngularVel, double maxCentralForce) {
        this.maxVel = maxVel;
        this.maxAccel = maxAccel;
        this.mass = mass;
        this.maxAngularVel = maxAngularVel;
        this.maxCentralForce = maxCentralForce;
    }

    public static RobotDriveConstants getDefault(){
        return new RobotDriveConstants(100,120,6,1.5,50);
    }
    //TODO - config
}
    