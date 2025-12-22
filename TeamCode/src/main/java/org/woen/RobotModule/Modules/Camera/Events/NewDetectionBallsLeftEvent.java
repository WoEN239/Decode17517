package org.woen.RobotModule.Modules.Camera.Events;

import org.woen.Architecture.EventBus.IEvent;
import org.woen.RobotModule.Modules.Camera.Enums.BALL_COLOR;

public class NewDetectionBallsLeftEvent implements IEvent<BALL_COLOR> {
    private final BALL_COLOR data;

    public NewDetectionBallsLeftEvent(BALL_COLOR data) {
        this.data = data;
    }

    public BALL_COLOR getData() {
        return data;
    }
}