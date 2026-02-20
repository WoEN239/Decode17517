package org.woen.RobotModule.Modules.Gun.Arcitecture;

import org.woen.Architecture.EventBus.IEvent;
import org.woen.RobotModule.Modules.Gun.Config.AIM_COMMAND;

public class NewAimEvent implements IEvent<AIM_COMMAND> {
    private final AIM_COMMAND data;

    public NewAimEvent(AIM_COMMAND data) {
        this.data = data;
    }

    @Override
    public AIM_COMMAND getData() {
        return data;
    }

}
