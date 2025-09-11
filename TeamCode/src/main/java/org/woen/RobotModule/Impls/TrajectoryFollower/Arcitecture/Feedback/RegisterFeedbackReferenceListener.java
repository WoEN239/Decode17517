package org.woen.RobotModule.Impls.TrajectoryFollower.Arcitecture.Feedback;

import org.woen.Architecture.EventBus.Bus.IEvent;
import org.woen.Architecture.Observers.IListener;
import org.woen.Util.Vectors.Pose;

public class RegisterFeedbackReferenceListener implements IEvent<IListener<Pose>> {
    private final IListener<Pose> data;

    public RegisterFeedbackReferenceListener(IListener<Pose> data) {
        this.data = data;
    }

    @Override
    public IListener<Pose> getData() {
        return data;
    }
}
