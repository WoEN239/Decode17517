package org.woen.RobotModule.Modules.Camera;

import org.woen.Architecture.EventBus.IEvent;

public class NewDetectionBallsEvent implements IEvent<MOTIF> {
    private final MOTIF data;

    public NewDetectionBallsEvent(MOTIF data) {
        this.data = data;
    }

    public MOTIF getData() {
        return data;
    }
}