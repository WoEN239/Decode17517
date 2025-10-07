package org.woen.RobotModule.Modules.Localizer.Velocity.Impls;

import org.woen.RobotModule.Modules.Localizer.Velocity.Architecture.LocalVelocityObserver;
import org.woen.RobotModule.Modules.Localizer.Velocity.Architecture.VelocityObserver;
import org.woen.RobotModule.Modules.Localizer.Velocity.Interface.VelocityLocalizer;
import org.woen.Telemetry.ConfigurableVariables.Provider;
import org.woen.Util.Vectors.Pose;

public class VelocityLocalizerMoc implements VelocityLocalizer {
    private final VelocityObserver velocityObserver = new VelocityObserver();
    private final LocalVelocityObserver localVelocityObserver = new LocalVelocityObserver();

    private final Provider<Pose> velocityProvider = new Provider<>(new Pose(0,0,0));
    private final Provider<Pose> localVelocityProvider = new Provider<>(new Pose(0,0,0));

    @Override
    public void update() {
        velocityObserver.notifyListeners(velocityProvider.get());
        localVelocityObserver.notifyListeners(localVelocityProvider.get());
    }

    @Override
    public void init() {
    }
}
