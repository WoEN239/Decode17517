package org.woen.Hardware.Odometers.Inter;

public interface Odometer {
    double getPos();//meter
    double getVel();//meter per second

    void reset();
}
