package org.woen.RobotModule.Modules.Gun.Arcitecture;

import org.woen.Architecture.EventBus.IEvent;
import org.woen.Architecture.Observers.IListener;
import org.woen.RobotModule.Modules.Gun.GUN_COMMAND;

public class RegisterNewGunCommandListener implements IEvent<IListener<GUN_COMMAND>> {
    private final IListener<GUN_COMMAND> data;

    public RegisterNewGunCommandListener(IListener<GUN_COMMAND> data) {
        this.data = data;
    }

    @Override
    public IListener<GUN_COMMAND> getData() {
        return data;
    }
}
