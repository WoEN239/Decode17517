package org.woen.RobotModule.Modules.Gyro.Arcitecture;

import org.woen.Architecture.EventBus.IEvent;
import org.woen.Architecture.Observers.IListener;

public class RegisterNewAngularVelocityListener implements IEvent<IListener<Double>> {
    private final IListener<Double> data;

    public RegisterNewAngularVelocityListener(IListener<Double> data) {
        this.data = data;
    }

    @Override
    public IListener<Double> getData() {
        return data;
    }
}
