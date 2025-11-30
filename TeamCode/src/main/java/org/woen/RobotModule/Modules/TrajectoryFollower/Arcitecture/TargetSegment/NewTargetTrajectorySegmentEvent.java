package org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.TargetSegment;

import org.woen.Architecture.EventBus.IEvent;
import org.woen.Autonom.WayPoint;

public class NewTargetTrajectorySegmentEvent implements IEvent<WayPoint> {
    private final WayPoint data;

    public WayPoint getData() {
        return data;
    }

    public NewTargetTrajectorySegmentEvent(WayPoint data) {
        this.data = data;
    }
}