package org.woen.RobotModule.Modules.Localizer.Velocity.Impls;

import static org.woen.Config.OdometerConstant.METER_PER_ANGLE;
import static org.woen.Config.OdometerConstant.Y_ODOMETER_RADIUS;

import org.woen.Architecture.EventBus.Bus.EventBus;
import org.woen.Config.MatchData;
import org.woen.RobotModule.Modules.Localizer.DeviceListener.Architecture.LocalizeDeviceData;
import org.woen.RobotModule.Modules.Localizer.DeviceListener.Architecture.RegisterNewLocalizeDeviceListener;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewPositionListener;
import org.woen.RobotModule.Modules.Localizer.Velocity.Architecture.LocalVelocityObserver;
import org.woen.RobotModule.Modules.Localizer.Velocity.Architecture.VelocityObserver;
import org.woen.RobotModule.Interface.IRobotModule;
import org.woen.RobotModule.Modules.Localizer.Velocity.Interface.VelocityLocalizer;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.Vector2d;

public class VelocityLocalizerImpl implements VelocityLocalizer {

    private Pose velocity = new Pose(
            0,
            new Vector2d()
    );
    private final VelocityObserver velocityObserver = new VelocityObserver();


    private Pose localVelocity = new Pose(
            MatchData.startPosition.h,
            new Vector2d()
    );
    private final LocalVelocityObserver localVelocityObserver = new LocalVelocityObserver();


    private double robotAngle = MatchData.startPosition.h;

    private void setRobotAngle(Pose pos){
        robotAngle = pos.h;
    }


    private LocalizeDeviceData deviceData = new LocalizeDeviceData();

    public void setDeviceData(LocalizeDeviceData data){
        deviceData = data;
    }


    @Override
    public void update(){

        double xLoc = deviceData.leftOdVel + deviceData.rightOdVel;
        xLoc *= 0.5;

        double h = -deviceData.leftOdVel + deviceData.rightOdVel;
        h *= 0.5;
        h *= METER_PER_ANGLE;

        double yLoc = deviceData.sideOdVel;
        yLoc += h*Y_ODOMETER_RADIUS;

        localVelocity = new Pose(
                h,
                new Vector2d(xLoc,
                             yLoc)
                );

        velocity = new Pose(
                h,
                localVelocity.vector.rotate(robotAngle)
        );

        localVelocityObserver.notifyListeners(localVelocity);
        velocityObserver.notifyListeners(velocity);

        Telemetry.getInstance().add("velocity",velocity.toString());
        Telemetry.getInstance().add("local velocity",localVelocity.toString());
    }

    @Override
    public void init() {
        EventBus.getListenersRegistration().invoke(
                new RegisterNewLocalizeDeviceListener(this::setDeviceData));
        EventBus.getListenersRegistration().invoke(
                new RegisterNewPositionListener(this::setRobotAngle));
    }
}
