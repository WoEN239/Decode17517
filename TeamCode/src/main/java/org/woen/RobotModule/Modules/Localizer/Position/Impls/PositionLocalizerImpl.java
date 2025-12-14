package org.woen.RobotModule.Modules.Localizer.Position.Impls;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.woen.Config.ControlSystemConstant.*;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.MatchData;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Devices.Odometers.Inter.PinPoint;
import org.woen.RobotModule.Modules.Gyro.Arcitecture.RegisterNewAngleListener;
import org.woen.RobotModule.Modules.Localizer.DeviceListener.Architecture.LocalizeDeviceData;
import org.woen.RobotModule.Modules.Localizer.DeviceListener.Architecture.RegisterNewLocalizeDeviceListener;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.LocalPositionObserver;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.PositionObserver;
import org.woen.RobotModule.Modules.Localizer.Position.Interface.PositionLocalizer;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.Angel.AngleUtil;
import org.woen.Util.ExponentialFilter.ExponentialFilter;
import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.Vector2d;

public class PositionLocalizerImpl implements PositionLocalizer {
    private PinPoint odo;

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

    private double gyroAngle = 0;
    public void setGyroAngle(double gyroAngle) {
        this.gyroAngle = gyroAngle;
    }

    @Override
    public void update() {
        Pose pRaw = odo.getPose();

        positionObserver.notifyListeners(pRaw);
        localPositionObserver.notifyListeners(position);
    }

    @Override
    public void init() {
        EventBus.getListenersRegistration().invoke(
                new RegisterNewLocalizeDeviceListener(this::setDeviceData));
        EventBus.getListenersRegistration().invoke(
                new RegisterNewAngleListener(this::setGyroAngle));
        position = MatchData.startPosition;
        odo = DevicePool.getInstance().pinPoint;
        odo.init();
    }
}
