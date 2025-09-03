package org.woen.Hardware.Odometers.Impl;

public interface Odometer {
    double getPos();//meter
    double getVel();//meter per second

    void reset();
}
