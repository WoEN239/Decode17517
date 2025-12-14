package org.woen.Autonom;

import org.woen.Architecture.EventBus.IEvent;

public class SetNewWaypointsSequenceEvent implements IEvent<WayPoint[]> {
    private final WayPoint[] data;

    public SetNewWaypointsSequenceEvent(WayPoint... data) {
        this.data = data;
    }

    @Override
    public WayPoint[] getData() {
        return data;
    }
}
