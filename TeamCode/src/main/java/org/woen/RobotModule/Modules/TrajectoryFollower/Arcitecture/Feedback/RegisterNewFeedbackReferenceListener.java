package org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback;

import org.woen.Architecture.EventBus.IEvent;
import org.woen.Architecture.Observers.IListener;

public class RegisterNewFeedbackReferenceListener implements IEvent<IListener<FeedbackReference>> {
    private final IListener<FeedbackReference> data;

    public RegisterNewFeedbackReferenceListener(IListener<FeedbackReference> data) {
        this.data = data;
    }

    @Override
    public IListener<FeedbackReference> getData() {
        return data;
    }
}
