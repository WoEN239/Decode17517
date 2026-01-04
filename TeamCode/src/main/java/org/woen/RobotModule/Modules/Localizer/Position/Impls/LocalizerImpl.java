package org.woen.RobotModule.Modules.Localizer.Position.Impls;

import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.DevicePool.Devices.Odometers.Inter.PinPoint;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.PositionObserver;
import org.woen.RobotModule.Modules.Localizer.Position.Interface.PositionLocalizer;
import org.woen.RobotModule.Modules.Localizer.Velocity.Architecture.VelocityObserver;
import org.woen.Util.Vectors.Pose;

public class LocalizerImpl implements PositionLocalizer {
    private PinPoint odo;

    private final PositionObserver poseObserver = new PositionObserver();

    private final VelocityObserver velocityObserver = new VelocityObserver();

    @Override
    public void update() {
        odo.update();
        Pose pose = odo.getPose();
        Pose vel = odo.getVel();

        poseObserver.notifyListeners(pose);
        velocityObserver.notifyListeners(vel);
    }

    @Override
    public void init() {
        odo = DevicePool.getInstance().pinPoint;
        odo.init();
    }
}
