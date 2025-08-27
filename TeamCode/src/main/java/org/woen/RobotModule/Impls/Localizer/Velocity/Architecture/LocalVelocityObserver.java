package org.woen.RobotModule.Impls.Localizer.Velocity.Architecture;

import org.woen.Architecture.EventBus.Bus.EventBus;
import org.woen.Architecture.Observers.Observer;
import org.woen.Util.Vectors.AbstractVector2d;
import org.woen.Util.Vectors.DoubleCoordinate;
import org.woen.Util.Vectors.Vector2d;

public class LocalVelocityObserver
        extends Observer<AbstractVector2d<DoubleCoordinate, Vector2d>, RegisterNewLocalVelocityListener> {

    @Override
    public void onEvent(RegisterNewLocalVelocityListener registration) {
        listeners.add(registration.getData());
    }

    public LocalVelocityObserver() {
        EventBus.getListenersRegistration()
                .subscribe(RegisterNewLocalVelocityListener.class,this::onEvent);
    }
}
