package org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Architecture.Observers.Observer;

public class FeedbackReferenceObserver extends Observer<FeedbackReference, RegisterNewFeedbackReferenceListener> {

    public FeedbackReferenceObserver() {
        EventBus.getListenersRegistration().subscribe(RegisterNewFeedbackReferenceListener.class,this::onEvent);
    }

    @Override
    public void onEvent(RegisterNewFeedbackReferenceListener registration) {
        listeners.add(registration.getData());
    }

}
