package org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward;

import org.woen.Architecture.EventBus.Bus.EventBus;
import org.woen.Architecture.Observers.Observer;

public class FeedforwardReferenceObserver extends Observer<FeedforwardReference,RegisterFeedforwardReferenceListener> {

    public FeedforwardReferenceObserver() {
        EventBus.getListenersRegistration().subscribe(RegisterFeedforwardReferenceListener.class,this::onEvent);
    }

    @Override
    public void onEvent(RegisterFeedforwardReferenceListener registration) {
        listeners.add(registration.getData());
    }
}
