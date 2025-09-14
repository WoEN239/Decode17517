package org.woen.Hardware.Devices.Odometers.Inter;

public interface Odometer {
    double getPos();//meter
    double getVel();//meter per second
    default void setDir(int dir){};
    void reset();
}
