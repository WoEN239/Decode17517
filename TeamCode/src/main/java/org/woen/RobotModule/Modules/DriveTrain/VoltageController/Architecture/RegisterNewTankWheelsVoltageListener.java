package org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture;

import org.woen.Architecture.EventBus.IEvent;
import org.woen.Architecture.Observers.IListener;

public class RegisterNewTankWheelsVoltageListener implements IEvent<IListener<TankWheelValueMap>> {
    private final IListener<TankWheelValueMap> data;

    public RegisterNewTankWheelsVoltageListener(IListener<TankWheelValueMap> data) {
        this.data = data;
    }

    @Override
    public IListener<TankWheelValueMap> getData() {
        return data;
    }
}
