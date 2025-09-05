package org.woen.Hardware.Gyro.Inter;

import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.woen.Hardware.Gyro.Impl.Gyro;

public class GyroImpl implements Gyro {

    IMU imu;

    public GyroImpl(IMU imu){
        this.imu = imu;
    }


    @Override
    public double getPos() {
        return imu.getRobotYawPitchRollAngles().getYaw();
    }

    @Override
    public AngularVelocity getVel() {
        return  imu.getRobotAngularVelocity(AngleUnit.DEGREES);
    }

    @Override
    public double getTelemetry() {
        return 0;
    }
}
