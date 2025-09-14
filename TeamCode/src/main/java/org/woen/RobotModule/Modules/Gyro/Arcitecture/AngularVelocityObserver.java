package org.woen.RobotModule.Modules.Gyro.Arcitecture;

import org.woen.Architecture.EventBus.Bus.EventBus;
import org.woen.Architecture.Observers.Observer;

public class AngularVelocityObserver extends Observer<Double,RegisterNewAngularVelocityListener> {

    @Override
    public void onEvent(RegisterNewAngularVelocityListener registration) {
        listeners.add(registration.getData());
    }

    public AngularVelocityObserver() {
        EventBus.getListenersRegistration().subscribe(RegisterNewAngularVelocityListener.class,this::onEvent);
    }

}
