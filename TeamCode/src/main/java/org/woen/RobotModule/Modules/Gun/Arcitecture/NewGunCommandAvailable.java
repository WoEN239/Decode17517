package org.woen.RobotModule.Modules.Gun.Arcitecture;

import org.woen.Architecture.EventBus.IEvent;
import org.woen.RobotModule.Modules.Gun.GUN_COMMAND;

public class NewGunCommandAvailable implements IEvent<GUN_COMMAND> {
    private final GUN_COMMAND data;

    public NewGunCommandAvailable(GUN_COMMAND data) {
        this.data = data;
    }

    @Override
    public GUN_COMMAND getData() {
        return data;
    }
}
