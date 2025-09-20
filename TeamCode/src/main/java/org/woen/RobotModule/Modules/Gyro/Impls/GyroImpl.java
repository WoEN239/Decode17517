package org.woen.RobotModule.Modules.Gyro.Impls;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.RobotModule.Modules.Gyro.Arcitecture.AngleObserver;
import org.woen.RobotModule.Modules.Gyro.Arcitecture.AngularVelocityObserver;
import org.woen.RobotModule.Modules.Gyro.Interface.Gyro;

public class GyroImpl implements Gyro {
    private final AngleObserver angleObserver = new AngleObserver();
    private final AngularVelocityObserver velObserver = new AngularVelocityObserver();
    private IMU imu;
    private final ElapsedTime timer = new ElapsedTime();

    public void init() {
        imu = DevicePool.getInstance().gyro;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot
                (RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.RIGHT);
        imu.initialize(new IMU.Parameters(orientationOnRobot));
        reset();
    }


    public  void reset() {imu.resetYaw();}

    @Override
    public  void update() {
        if(timer.seconds()>0.1) {
            double angle = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            double velocity = imu.getRobotAngularVelocity(AngleUnit.RADIANS).zRotationRate;
            angleObserver.notifyListeners(angle);
            velObserver.notifyListeners(velocity);
            timer.reset();
        }
    }

}
