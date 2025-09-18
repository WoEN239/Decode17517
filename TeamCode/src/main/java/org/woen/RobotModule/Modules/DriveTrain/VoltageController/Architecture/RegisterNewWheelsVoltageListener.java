package org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture;

import org.woen.Architecture.EventBus.IEvent;
import org.woen.Architecture.Observers.IListener;

public class RegisterNewWheelsVoltageListener implements IEvent<IListener<WheelValueMap>> {
    private final IListener<WheelValueMap> data;

    public RegisterNewWheelsVoltageListener(IListener<WheelValueMap> data) {
        this.data = data;
    }

    @Override
    public IListener<WheelValueMap> getData() {
        return data;
    }
}
