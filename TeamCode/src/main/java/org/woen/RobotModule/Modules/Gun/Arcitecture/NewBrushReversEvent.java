package org.woen.RobotModule.Modules.Gun.Arcitecture;

import org.woen.Architecture.EventBus.IEvent;

public class NewBrushReversEvent implements IEvent<Boolean> {
    private final boolean data;

    public NewBrushReversEvent(boolean data) {
        this.data = data;
    }

    @Override
    public Boolean getData() {
        return data;
    }

}
