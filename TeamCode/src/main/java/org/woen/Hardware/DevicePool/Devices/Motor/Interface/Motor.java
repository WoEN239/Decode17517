package org.woen.Hardware.DevicePool.Devices.Motor.Interface;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public interface Motor {
    double getCurrent();

    double getVel();

    void setPower(double power);

    void setDir(int dir);
    default void setDirection(DcMotorSimple.Direction direction){}

    void reset();
    void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior sZPB);

    double getPosition();
}
