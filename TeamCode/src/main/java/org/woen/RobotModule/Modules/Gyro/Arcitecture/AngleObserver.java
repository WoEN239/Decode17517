package org.woen.RobotModule.Modules.Gyro.Arcitecture;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Architecture.Observers.Observer;

public class AngleObserver extends Observer<Double, RegisterNewAngleListener> {
    @Override
    public void onEvent(RegisterNewAngleListener registration) {
        listeners.add(registration.getData());
    }

    public AngleObserver() {
        EventBus.getListenersRegistration().subscribe(RegisterNewAngleListener.class,this::onEvent);
    }

}
