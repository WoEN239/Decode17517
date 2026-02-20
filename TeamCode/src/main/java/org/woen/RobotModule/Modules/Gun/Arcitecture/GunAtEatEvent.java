package org.woen.RobotModule.Modules.Gun.Arcitecture;

import org.woen.Architecture.EventBus.IEvent;

public class GunAtEatEvent implements IEvent<Boolean> {
    private final Boolean data;

    public Boolean getData() {
        return data;

    }

    public GunAtEatEvent(Boolean data) {
        this.data = data;

    }
}