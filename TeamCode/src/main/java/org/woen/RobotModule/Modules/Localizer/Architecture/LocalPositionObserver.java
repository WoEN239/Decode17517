package org.woen.RobotModule.Modules.Localizer.Architecture;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Architecture.Observers.Observer;
import org.woen.Util.Vectors.Pose;

public class LocalPositionObserver extends Observer<Pose,RegisterNewLocalPositionListener> {
    @Override
    public void onEvent(RegisterNewLocalPositionListener registration) {
        listeners.add(registration.getData());
    }

    public LocalPositionObserver() {
        EventBus.getListenersRegistration().subscribe(RegisterNewLocalPositionListener.class,this::onEvent);
    }

}
