package org.woen.Hardware.Devices.Servo.Inter;

public interface Servo {
    boolean isItTarget();

    void setPos(double pos, double startPos);
}
