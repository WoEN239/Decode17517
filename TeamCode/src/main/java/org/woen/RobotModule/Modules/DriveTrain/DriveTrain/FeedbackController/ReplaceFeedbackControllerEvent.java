package org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController;

import org.woen.Architecture.EventBus.IEvent;

public class ReplaceFeedbackControllerEvent implements IEvent<TankFeedbackController> {
    private final TankFeedbackController data;

    public ReplaceFeedbackControllerEvent(TankFeedbackController data) {
        this.data = data;
    }

    @Override
    public TankFeedbackController getData() {
        return data;
    }
}
