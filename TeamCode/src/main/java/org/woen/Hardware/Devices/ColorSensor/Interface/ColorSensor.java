package org.woen.Hardware.Devices.ColorSensor.Interface;

import org.woen.Util.Color.RgbColorVector;

public interface ColorSensor {
     default RgbColorVector getVector(){ return new RgbColorVector(0,0,0); }
     int getRed();
     int getBlue();
     int getGreen();
}
