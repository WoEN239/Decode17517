package org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward;

import org.woen.Architecture.EventBus.IEvent;
import org.woen.Architecture.Observers.IListener;

public class RegisterFeedforwardReferenceListener implements IEvent<IListener<FeedforwardReference>> {

    private final IListener<FeedforwardReference> data;

    public RegisterFeedforwardReferenceListener(IListener<FeedforwardReference> data) {
        this.data = data;
    }

    @Override
    public IListener<FeedforwardReference> getData() {
        return data;
    }
}
