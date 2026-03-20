package org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback;

public class TankFeedbackReference {
    public final boolean isEnable;
    public final double angle;

    public TankFeedbackReference(boolean isEnable, double angle) {
        this.isEnable = isEnable;
        this.angle = angle;
    }
    @Override
    public String toString(){
        return  "h" + angle + " " + isEnable;
    }
}
