package org.woen.Hardware.Devices.Odometers.Inter;

import org.woen.Util.Vectors.Pose;

public interface PinPoint {
    Pose getPose();
    Pose getVel();
    default void init(){}
}
