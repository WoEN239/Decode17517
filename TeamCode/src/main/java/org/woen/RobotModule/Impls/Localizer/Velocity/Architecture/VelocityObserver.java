package org.woen.RobotModule.Impls.Localizer.Velocity.Architecture;

import org.woen.Architecture.EventBus.Bus.EventBus;
import org.woen.Architecture.Observers.Observer;
import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.DoubleCoordinate;
import org.woen.Util.Vectors.Vector2d;

public class VelocityObserver extends Observer<Pose, RegisterNewVelocityListener> {

    @Override
    public void onEvent(RegisterNewVelocityListener registration) {
        listeners.add(registration.getData());
    }

    public VelocityObserver() {
        EventBus.getListenersRegistration()
                .subscribe(RegisterNewVelocityListener.class,this::onEvent);
    }
}
