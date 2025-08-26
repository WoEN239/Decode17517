package org.woen.RobotModule.Impls.Localizer.Position.Architecture;

import org.woen.Architecture.EventBus.Bus.IEvent;
import org.woen.Architecture.Observers.IListener;
import org.woen.Util.Vectors.AbstractVector2d;
import org.woen.Util.Vectors.DoubleCoordinate;
import org.woen.Util.Vectors.Vector2d;

public class RegisterNewPositionListener
        implements IEvent<IListener<AbstractVector2d<DoubleCoordinate, Vector2d>>> {

    private final IListener<AbstractVector2d<DoubleCoordinate, Vector2d>> data;

    public RegisterNewPositionListener(IListener<AbstractVector2d<DoubleCoordinate, Vector2d>> data) {
        this.data = data;
    }

    @Override
    public IListener<AbstractVector2d<DoubleCoordinate, Vector2d>> getData() {
        return data;
    }
}
