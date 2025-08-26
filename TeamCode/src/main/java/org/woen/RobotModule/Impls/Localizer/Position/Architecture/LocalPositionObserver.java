package org.woen.RobotModule.Impls.Localizer.Position.Architecture;

import org.woen.Architecture.EventBus.Bus.EventBus;
import org.woen.Architecture.Observers.Observer;
import org.woen.Util.Vectors.AbstractVector2d;
import org.woen.Util.Vectors.DoubleCoordinate;
import org.woen.Util.Vectors.Vector2d;

public class LocalPositionObserver extends Observer<AbstractVector2d<DoubleCoordinate, Vector2d>,RegisterNewPositionListener> {
    @Override
    public void onEvent(RegisterNewPositionListener registration) {
        listeners.add(registration.getData());
    }

    public LocalPositionObserver() {
        EventBus.getListenersRegistration().subscribe(RegisterNewPositionListener.class,this::onEvent);
    }


}
