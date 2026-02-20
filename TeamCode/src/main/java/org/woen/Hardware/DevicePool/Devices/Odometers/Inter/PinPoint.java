package org.woen.Hardware.DevicePool.Devices.Odometers.Inter;

import org.woen.Util.Vectors.Pose;

public interface PinPoint {
    Pose getPose();
    Pose getVel();
    default void init(){}
    default void update(){}

    default void setPose(double x, double y, double h){}
}
