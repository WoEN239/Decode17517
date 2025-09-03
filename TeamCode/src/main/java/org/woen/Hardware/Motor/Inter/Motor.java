package org.woen.Hardware.Motor.Inter;

import com.qualcomm.robotcore.hardware.DcMotor;

public interface Motor {
    double getCurrent();

    double getVel();

    void setPower(double voltage);

    void reset();
    void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior sZPB);

    double getPosition();
}
