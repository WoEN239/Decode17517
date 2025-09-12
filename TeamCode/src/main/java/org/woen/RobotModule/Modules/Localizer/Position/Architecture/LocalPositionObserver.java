package org.woen.RobotModule.Modules.Localizer.Position.Architecture;

import org.woen.Architecture.EventBus.Bus.EventBus;
import org.woen.Architecture.Observers.Observer;
import org.woen.Util.Vectors.Pose;

public class LocalPositionObserver extends Observer<Pose,RegisterNewPositionListener> {
    @Override
    public void onEvent(RegisterNewPositionListener registration) {
        listeners.add(registration.getData());
    }

    public LocalPositionObserver() {
        EventBus.getListenersRegistration().subscribe(RegisterNewPositionListener.class,this::onEvent);
    }


}
