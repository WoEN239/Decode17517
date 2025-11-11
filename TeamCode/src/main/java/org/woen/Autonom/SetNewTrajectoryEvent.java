package org.woen.Autonom;

import org.woen.Architecture.EventBus.IEvent;

public class SetNewTrajectoryEvent implements IEvent<WayPoint[]> {
    private final WayPoint[] data;

    public SetNewTrajectoryEvent(WayPoint... data) {
        this.data = data;
    }

    @Override
    public WayPoint[] getData() {
        return data;
    }
}
