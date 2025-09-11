package org.woen.RobotModule.Impls.Localizer.Velocity.Architecture;

import org.woen.Architecture.EventBus.Bus.IEvent;
import org.woen.Architecture.Observers.IListener;
import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.DoubleCoordinate;
import org.woen.Util.Vectors.Vector2d;

public class RegisterNewLocalVelocityListener implements IEvent<IListener<Pose>> {

    private final IListener<Pose> data;

    public RegisterNewLocalVelocityListener(IListener<Pose> data) {
        this.data = data;
    }

    @Override
    public IListener getData() {
        return data;
    }
}
