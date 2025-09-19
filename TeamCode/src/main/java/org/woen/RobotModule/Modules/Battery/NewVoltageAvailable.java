package org.woen.RobotModule.Modules.Battery;

import org.woen.Architecture.EventBus.IEvent;

public class NewVoltageAvailable implements IEvent<Double> {
    private final double data;

    public NewVoltageAvailable(double data) {
        this.data = data;
    }

    @Override
    public Double getData() {
        return data;
    }
}
