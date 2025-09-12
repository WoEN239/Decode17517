package org.woen.Hardware.Gyro.Inter;

import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.woen.Hardware.Gyro.Impl.Gyro;

public class GyroMoc implements Gyro {


    private double vel = 0;
    private double yaw = 0d;


    public GyroMoc(double vel, double yaw){
        this.vel = vel;
        this.yaw = yaw;
    }


    @Override
    public double getPos() {
        return yaw;
    }

    @Override
    public double getVel() {
        return vel;
    }

    @Override
    public double getTelemetry() {
        return 0;
    }
}
