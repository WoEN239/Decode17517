package org.woen.RobotModule.Modules.Gyro.Arcitecture;

import org.woen.Architecture.EventBus.IEvent;
import org.woen.Architecture.Observers.IListener;

public class RegisterNewAngleListener implements IEvent<IListener<Double>> {

    public RegisterNewAngleListener(IListener<Double> data) {
        this.data = data;
    }

    private final IListener<Double> data;

    @Override
    public IListener<Double> getData() {
        return data;
    }
}
