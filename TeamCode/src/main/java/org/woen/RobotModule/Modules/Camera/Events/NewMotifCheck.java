package org.woen.RobotModule.Modules.Camera.Events;

import org.woen.Architecture.EventBus.IEvent;

public class NewMotifCheck implements IEvent<Boolean> {
    private final Boolean data;

    public NewMotifCheck(Boolean data) {
        this.data = data;
    }

    public Boolean getData() {
        return data;
    }
}
