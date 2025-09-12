package org.woen.RobotModule.Modules.Localizer.Position.Architecture;

import org.woen.Architecture.EventBus.Bus.EventBus;
import org.woen.Architecture.Observers.Observer;
import org.woen.Util.Vectors.Pose;

public class PositionObserver extends Observer<Pose,RegisterNewPositionListener> {

    @Override
    public void onEvent(RegisterNewPositionListener registration) {
        listeners.add(registration.getData());
    }

    public PositionObserver() {
        EventBus.getListenersRegistration().subscribe(RegisterNewPositionListener.class,this::onEvent);
    }

}
