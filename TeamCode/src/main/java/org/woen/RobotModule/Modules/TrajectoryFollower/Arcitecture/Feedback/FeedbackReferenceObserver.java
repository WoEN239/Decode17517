package org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback;

import org.woen.Architecture.EventBus.Bus.EventBus;
import org.woen.Architecture.Observers.Observer;
import org.woen.Util.Vectors.Pose;

public class FeedbackReferenceObserver extends Observer<FeedbackReference,RegisterFeedbackReferenceListener> {

    public FeedbackReferenceObserver() {
        EventBus.getListenersRegistration().subscribe(RegisterFeedbackReferenceListener.class,this::onEvent);
    }

    @Override
    public void onEvent(RegisterFeedbackReferenceListener registration) {
        listeners.add(registration.getData());
    }

}
