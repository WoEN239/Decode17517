package org.woen.RobotModule.Modules.Camera;

import org.woen.Architecture.EventBus.IEvent;

public class NewMotifEvent implements IEvent<MOTIF> {
    private final MOTIF data;

    public MOTIF getData() {
        return data;
    }

    public NewMotifEvent(MOTIF data) {
        this.data = data;
    }
}