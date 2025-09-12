package org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback;

import org.woen.Util.Vectors.Pose;

public class FeedbackReference {
    public final Pose pos;
    public final Pose vel;

    public FeedbackReference(Pose pos, Pose vel) {
        this.pos = pos;
        this.vel = vel;
    }

}
