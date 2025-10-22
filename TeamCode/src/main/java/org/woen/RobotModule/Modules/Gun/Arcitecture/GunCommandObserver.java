package org.woen.RobotModule.Modules.Gun.Arcitecture;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Architecture.Observers.Observer;
import org.woen.RobotModule.Modules.Gun.GUN_COMMAND;

public class GunCommandObserver extends Observer<GUN_COMMAND,RegisterNewGunCommandListener> {
    @Override
    public void onEvent(RegisterNewGunCommandListener registration) {
        listeners.add(registration.getData());
    }

    public GunCommandObserver() {
        EventBus.getListenersRegistration().subscribe(RegisterNewGunCommandListener.class,this::onEvent);
    }
}
