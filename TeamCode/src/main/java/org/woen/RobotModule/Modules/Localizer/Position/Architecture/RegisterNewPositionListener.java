package org.woen.RobotModule.Modules.Localizer.Position.Architecture;

import org.woen.Architecture.EventBus.IEvent;
import org.woen.Architecture.Observers.IListener;
import org.woen.Util.Vectors.Pose;

public class RegisterNewPositionListener implements IEvent<IListener<Pose>> {

    private final IListener<Pose> data;

    public RegisterNewPositionListener(IListener<Pose> data) {
        this.data = data;
    }

    @Override
    public IListener<Pose> getData() {
        return data;
    }
}
