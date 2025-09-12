package org.woen.RobotModule.Modules.Localizer.Velocity.Architecture;

import org.woen.Architecture.EventBus.Bus.IEvent;
import org.woen.Architecture.Observers.IListener;
import org.woen.Util.Vectors.Pose;

public class RegisterNewVelocityListener implements IEvent<IListener<Pose>> {

    private final IListener<Pose> data;

    public RegisterNewVelocityListener(IListener<Pose> data) {
        this.data = data;
    }

    @Override
    public IListener<Pose> getData() {
        return data;
    }
}
