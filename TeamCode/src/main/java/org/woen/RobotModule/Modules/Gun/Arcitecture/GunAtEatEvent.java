package org.woen.RobotModule.Modules.Gun.Arcitecture;

import org.woen.Architecture.EventBus.IEvent;

public class GunAtEatEvent implements IEvent<Integer> {
    private final int data;

    public Integer getData() {
        return data;

    }

    public GunAtEatEvent(int data) {
        this.data = data;

    }
}