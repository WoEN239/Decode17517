package org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward;

import org.woen.Util.Vectors.Pose;

public class FeedforwardReference {
    public FeedforwardReference(Pose now, Pose next) {
        this.now = now;
        this.next = next;
    }

    public final Pose now;
    public final Pose next;
}
