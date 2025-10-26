package org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Architecture.Observers.Observer;

public class FeedforwardReferenceObserver extends Observer<FeedforwardReference, RegisterNewFeedforwardReferenceListener> {

    public FeedforwardReferenceObserver() {
        EventBus.getListenersRegistration().subscribe(RegisterNewFeedforwardReferenceListener.class,this::onEvent);
    }

    @Override
    public void onEvent(RegisterNewFeedforwardReferenceListener registration) {
        listeners.add(registration.getData());
    }
}
