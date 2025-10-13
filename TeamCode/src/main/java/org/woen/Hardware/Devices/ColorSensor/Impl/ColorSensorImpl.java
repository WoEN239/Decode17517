package org.woen.Hardware.Devices.ColorSensor.Impl;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;

import org.woen.Hardware.Devices.ColorSensor.ColorSensorFix;
import org.woen.Hardware.Devices.ColorSensor.Interface.ColorSensor;

public class ColorSensorImpl implements ColorSensor {

    protected AdafruitI2cColorSensor sensor;

    public ColorSensorImpl(AdafruitI2cColorSensor sensor){
        this.sensor = ColorSensorFix.fix(sensor);
    }
@Override
    public int getRed(){
        return sensor.red();
    }
    @Override
    public int getBlue(){
        return sensor.blue();
    }
    @Override
    public int getGreen(){
        return sensor.green();
    }

}
