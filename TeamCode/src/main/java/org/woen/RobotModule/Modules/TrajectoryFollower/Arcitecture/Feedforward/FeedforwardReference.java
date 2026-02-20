package org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward;

import org.woen.Util.Vectors.Pose;

public class FeedforwardReference {
    public FeedforwardReference(Pose now, Pose accel) {
        this.vel = now;
        this.accel = accel;
    }

    public final Pose vel;
    public final Pose accel;
}
