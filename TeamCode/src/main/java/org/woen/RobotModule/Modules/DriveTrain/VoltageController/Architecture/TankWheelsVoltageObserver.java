package org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Architecture.Observers.Observer;

public class TankWheelsVoltageObserver extends Observer<TankWheelValueMap,RegisterNewTankWheelsVoltageListener> {

    public TankWheelsVoltageObserver() {
        EventBus.getListenersRegistration().subscribe(RegisterNewTankWheelsVoltageListener.class,this::onEvent);
    }

    @Override
    public void onEvent(RegisterNewTankWheelsVoltageListener registration) {
        listeners.add(registration.getData());
    }
}