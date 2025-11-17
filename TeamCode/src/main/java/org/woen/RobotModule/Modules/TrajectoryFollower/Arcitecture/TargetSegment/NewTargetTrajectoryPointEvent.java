package org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.TargetSegment;

import org.woen.Architecture.EventBus.IEvent;
import org.woen.Autonom.WayPoint;
import org.woen.Trajectory.TrajectoryPoint;

public class NewTargetTrajectoryPointEvent implements IEvent<WayPoint> {
    private final WayPoint data;

    public WayPoint getData() {
        return data;
    }

    public NewTargetTrajectoryPointEvent(WayPoint data) {
        this.data = data;
    }
}