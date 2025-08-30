package org.woen.RobotModule.Impls.Localizer.Position;

import static org.woen.Config.OdometerConstant.METER_PER_ANGLE;
import static org.woen.Config.OdometerConstant.Y_ODOMETER_RADIUS;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import org.woen.Architecture.EventBus.Bus.EventBus;
import org.woen.Config.MatchData;
import org.woen.RobotModule.Interface.IRobotModule;
import org.woen.RobotModule.Impls.Localizer.DeviceListener.Architecture.LocalizeDeviceData;
import org.woen.RobotModule.Impls.Localizer.DeviceListener.Architecture.RegisterNewLocalizeDeviceListener;
import org.woen.RobotModule.Impls.Localizer.Position.Architecture.LocalPositionObserver;
import org.woen.RobotModule.Impls.Localizer.Position.Architecture.PositionObserver;
import org.woen.Util.Angel.AngelUtil;
import org.woen.Util.ExponentialFilter.ExponentialFilter;
import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.DoubleCoordinate;
import org.woen.Util.Vectors.Vector2d;

public class PositionLocalizer implements IRobotModule {

    private Pose position = MatchData.startPosition;
    private final PositionObserver positionObserver = new PositionObserver();

    private Pose localPosition = new Pose(
            MatchData.startPosition.h,
            new Vector2d()
    );
    private final LocalPositionObserver localPositionObserver = new LocalPositionObserver();


    private LocalizeDeviceData deviceData = new LocalizeDeviceData();

    public void setDeviceData(LocalizeDeviceData data){
        deviceData = data;
    }


    private double s1Old = 0;
    private double xH2Old = 0;
    private final ExponentialFilter filter = new ExponentialFilter();

    @Override
    public void update() {

        double xLoc = deviceData.leftOdPos + deviceData.rightOdPos;
        xLoc *= 0.5;

        double hOd = -deviceData.leftOdPos + deviceData.rightOdPos;
        hOd *= 0.5;
        hOd *= METER_PER_ANGLE;

        double yLoc = deviceData.sideOdPos;
        yLoc += hOd*Y_ODOMETER_RADIUS;

        hOd = AngelUtil.normolize(hOd);

        double d1 = hOd                - s1Old;
        double d2 = deviceData.gyroPos - xH2Old;

        d1 = AngelUtil.normolize(d1);
        d2 = AngelUtil.normolize(d2);

        filter.update(d1,d2);

        s1Old = hOd;
        xH2Old = filter.getX();

        double h = filter.getX() + MatchData.startPosition.h;

        h = AngelUtil.normolize(h);

        Pose deltaLocalPosition = localPosition.minus(
                new Pose(
                        h,
                        new Vector2d(xLoc,
                                     yLoc)
                )
        );

        double dx = deltaLocalPosition.vector.x;
        double dy = deltaLocalPosition.vector.y;
        double dh = deltaLocalPosition.h;

        Vector2d dpCorrected = new Vector2d(
                dx*sin(dh)/dh + dy*(cos(dh)-1)/dh,
                dx*(cos(dh)-1)/dh + dy*sin(dh)/dh
        );

        if(abs(dh)<0.001){
            dpCorrected = new Vector2d(dx,
                                       dh
            );
        }

        position = new Pose(
                h,
                position.vector.plus(dpCorrected.rotate(localPosition.h))
        );

        localPosition  = new Pose(
                h,
                new Vector2d(xLoc,
                             yLoc)
        );

        positionObserver.notifyListeners(position);
        localPositionObserver.notifyListeners(localPosition);

    }

    @Override
    public void init() {
        EventBus.getListenersRegistration().invoke(
                new RegisterNewLocalizeDeviceListener(this::setDeviceData));
    }
}
