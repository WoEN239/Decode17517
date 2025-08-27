package org.woen.RobotModule.Impls.Localizer.Velocity;

import static org.woen.Config.OdometerConstant.METER_PER_ANGLE;
import static org.woen.Config.OdometerConstant.Y_ODOMETER_RADIUS;

import org.woen.Architecture.EventBus.Bus.EventBus;
import org.woen.Config.MatchData;
import org.woen.RobotModule.Impls.Localizer.DeviceListener.Architecture.LocalizeDeviceData;
import org.woen.RobotModule.Impls.Localizer.DeviceListener.Architecture.RegisterNewLocalizeDeviceListener;
import org.woen.RobotModule.Impls.Localizer.Position.Architecture.RegisterNewPositionListener;
import org.woen.RobotModule.Impls.Localizer.Velocity.Architecture.LocalVelocityObserver;
import org.woen.RobotModule.Impls.Localizer.Velocity.Architecture.VelocityObserver;
import org.woen.RobotModule.Interface.IRobotModule;
import org.woen.Util.Vectors.AbstractVector2d;
import org.woen.Util.Vectors.DoubleCoordinate;
import org.woen.Util.Vectors.Vector2d;

public class VelocityLocalizer implements IRobotModule {

    private AbstractVector2d<DoubleCoordinate, Vector2d> velocity = new AbstractVector2d<>(
            new DoubleCoordinate(0),
            new Vector2d()
    );
    private final VelocityObserver velocityObserver = new VelocityObserver();


    private AbstractVector2d<DoubleCoordinate, Vector2d> localVelocity = new AbstractVector2d<>(
            new DoubleCoordinate(MatchData.startPosition.getX().getData()),
            new Vector2d()
    );
    private final LocalVelocityObserver localVelocityObserver = new LocalVelocityObserver();


    private double robotAngle = MatchData.startPosition.getX().getData();

    private void setRobotAngle(AbstractVector2d<DoubleCoordinate,Vector2d> pos){
        robotAngle = pos.getX().getData();
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

        localVelocity = new AbstractVector2d<>(
                new DoubleCoordinate(h),
                new Vector2d(xLoc,
                             yLoc)
                );

        velocity = new AbstractVector2d<>(
                new DoubleCoordinate(h),
                localVelocity.getY().rotate(robotAngle)
        );

        localVelocityObserver.notifyListeners(localVelocity);
        velocityObserver.notifyListeners(velocity);

    }

    @Override
    public void init() {
        EventBus.getListenersRegistration().invoke(
                new RegisterNewLocalizeDeviceListener(this::setDeviceData));
        EventBus.getListenersRegistration().invoke(
                new RegisterNewPositionListener(this::setRobotAngle));
    }
}
