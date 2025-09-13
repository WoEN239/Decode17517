package org.woen.Hardware.Motor.Inter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public interface Motor {
    double getCurrent();

    double getVel();

    void setPower(double voltage);
    void setDirection(DcMotorSimple.Direction direction);

    void reset();
    void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior sZPB);

    double getPosition();
}
