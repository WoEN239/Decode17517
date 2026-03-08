package org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback;

import org.woen.Architecture.EventBus.IEvent;
import org.woen.Architecture.Observers.IListener;

public class RegisterNewTankFeedbackReferenceListener implements IEvent<IListener<TankFeedbackReference>> {
    private final IListener<TankFeedbackReference> data;

    public RegisterNewTankFeedbackReferenceListener(IListener<TankFeedbackReference> data) {
        this.data = data;
    }

    @Override
    public IListener<TankFeedbackReference> getData() {
        return data;
    }
}