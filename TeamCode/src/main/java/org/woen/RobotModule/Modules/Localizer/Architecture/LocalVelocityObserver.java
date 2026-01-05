package org.woen.RobotModule.Modules.Localizer.Architecture;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Architecture.Observers.Observer;
import org.woen.Util.Vectors.Pose;

public class LocalVelocityObserver extends Observer<Pose, RegisterNewLocalVelocityListener> {

    @Override
    public void onEvent(RegisterNewLocalVelocityListener registration) {
        listeners.add(registration.getData());
    }

    public LocalVelocityObserver() {
        EventBus.getListenersRegistration()
                .subscribe(RegisterNewLocalVelocityListener.class,this::onEvent);
    }
}
