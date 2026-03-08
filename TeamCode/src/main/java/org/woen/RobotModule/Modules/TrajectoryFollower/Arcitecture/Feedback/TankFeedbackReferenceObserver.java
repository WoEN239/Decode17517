package org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Architecture.Observers.Observer;

public class TankFeedbackReferenceObserver extends Observer<TankFeedbackReference, RegisterNewTankFeedbackReferenceListener> {

    public TankFeedbackReferenceObserver() {
        EventBus.getListenersRegistration().subscribe(RegisterNewTankFeedbackReferenceListener.class,this::onEvent);
    }

    @Override
    public void onEvent(RegisterNewTankFeedbackReferenceListener registration) {
        listeners.add(registration.getData());
    }

}
