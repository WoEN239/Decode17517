package org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.TargetSegment;

import org.woen.Architecture.EventBus.IEvent;
import org.woen.Trajectory.TrajectoryPoint;

public class NewTargetTrajectoryPointEvent implements IEvent<TrajectoryPoint> {
    private final TrajectoryPoint data;

    public TrajectoryPoint getData() {
        return data;
    }

    public NewTargetTrajectoryPointEvent(TrajectoryPoint data) {
        this.data = data;
    }
}