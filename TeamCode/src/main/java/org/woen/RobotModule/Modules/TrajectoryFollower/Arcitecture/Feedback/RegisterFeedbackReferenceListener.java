package org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback;

import org.woen.Architecture.EventBus.Bus.IEvent;
import org.woen.Architecture.Observers.IListener;
import org.woen.Util.Vectors.Pose;

public class RegisterFeedbackReferenceListener implements IEvent<IListener<FeedbackReference>> {
    private final IListener<FeedbackReference> data;

    public RegisterFeedbackReferenceListener(IListener<FeedbackReference> data) {
        this.data = data;
    }

    @Override
    public IListener<FeedbackReference> getData() {
        return data;
    }
}
