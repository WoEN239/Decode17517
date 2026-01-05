package org.woen.RobotModule.Modules.Localizer.Architecture;

import org.woen.Architecture.EventBus.IEvent;
import org.woen.Architecture.Observers.IListener;
import org.woen.Util.Vectors.Pose;

public class RegisterNewLocalPositionListener implements IEvent<IListener<Pose>> {

    private final IListener<Pose> data;

    public RegisterNewLocalPositionListener(IListener<Pose> data) {
        this.data = data;
    }

    @Override
    public IListener<Pose> getData() {
        return data;
    }
}
