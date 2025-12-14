package org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.TargetSegment;

import org.woen.Architecture.EventBus.IEvent;
import org.woen.Autonom.WayPoint;

public class SetNewTargetTrajectorySegmentEvent implements IEvent<WayPoint> {
    private final WayPoint data;

    public WayPoint getData() {
        return data;
    }

    public SetNewTargetTrajectorySegmentEvent(WayPoint data) {
        this.data = data;
    }
}