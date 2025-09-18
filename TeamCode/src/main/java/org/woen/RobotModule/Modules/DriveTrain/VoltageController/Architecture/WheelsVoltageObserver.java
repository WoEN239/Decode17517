package org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Architecture.Observers.Observer;

public class WheelsVoltageObserver extends Observer<WheelValueMap,RegisterNewWheelsVoltageListener> {

    public WheelsVoltageObserver() {
        EventBus.getListenersRegistration().subscribe(RegisterNewWheelsVoltageListener.class,this::onEvent);
    }

    @Override
    public void onEvent(RegisterNewWheelsVoltageListener registration) {
        listeners.add(registration.getData());
    }
}
