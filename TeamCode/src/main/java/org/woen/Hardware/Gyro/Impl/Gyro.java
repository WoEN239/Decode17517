package org.woen.Hardware.Gyro.Impl;

import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;

public interface Gyro{
    double getPos();//rad
    AngularVelocity getVel();//rad per sec
    double getTelemetry();//
}
