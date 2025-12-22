package org.woen.RobotModule.Modules.Camera.Events;

import org.woen.Architecture.EventBus.IEvent;
import org.woen.RobotModule.Modules.Camera.Enums.MOTIF;

public class NewMotifEvent implements IEvent<MOTIF> {
    private final MOTIF data;

    public MOTIF getData() {
        return data;
    }

    public NewMotifEvent(MOTIF data) {
        this.data = data;
    }
}