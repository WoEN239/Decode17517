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
import org.woen.Util.Vectors.AbstractVector2d;
import org.woen.Util.Vectors.DoubleCoordinate;
import org.woen.Util.Vectors.Vector2d;

public class PositionLocalizer implements IRobotModule {

    private AbstractVector2d<DoubleCoordinate, Vector2d> position = MatchData.startPosition;
    private final PositionObserver positionObserver = new PositionObserver();

    private AbstractVector2d<DoubleCoordinate, Vector2d> localPosition = new AbstractVector2d<>(
            new DoubleCoordinate(MatchData.startPosition.getX().getData()),
            new Vector2d( new DoubleCoordinate(0),
                          new DoubleCoordinate(0))
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

        double h = filter.getX() + MatchData.startPosition.getX().getData();

        h = AngelUtil.normolize(h);

        AbstractVector2d<DoubleCoordinate, Vector2d> deltaLocalPos = localPosition.minus(
                new AbstractVector2d<>(
                        new DoubleCoordinate(h),
                        new Vector2d(new DoubleCoordinate(xLoc),
                                     new DoubleCoordinate(yLoc))
                )
        );

        double dx = localPosition.getY().getX().getData();
        double dy = localPosition.getY().getY().getData();
        double dh = localPosition.getX().getData();

        Vector2d dpCorrected = new Vector2d(
                new DoubleCoordinate(dx*sin(dh)/dh + dy*(cos(dh)-1)/dh),
                new DoubleCoordinate(dx*(cos(dh)-1)/dh + dy*sin(dh)/dh)
        );

        if(abs(dh)<0.001){
            dpCorrected = new Vector2d(
                    new DoubleCoordinate(dx),
                    new DoubleCoordinate(dh)
            );
        }

        dpCorrected.rotate(localPosition.getX().getData());

        localPosition  = new AbstractVector2d<>(
                new DoubleCoordinate(h),
                new Vector2d( new DoubleCoordinate(xLoc),
                        new DoubleCoordinate(yLoc))
        );

        position = new AbstractVector2d<>(
                new DoubleCoordinate(h),
                position.getY().plus(dpCorrected)
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
