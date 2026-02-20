package org.woen.RobotModule.Modules.Camera.Events;

import org.woen.Architecture.EventBus.IEvent;

public class PipeLineSwitchEvent implements IEvent<Integer> {
    private final int data;

    public PipeLineSwitchEvent(int data) {
        this.data = data;
    }

    @Override
    public Integer getData() {
        return data;
    }
}
