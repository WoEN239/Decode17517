package org.woen.Hardware.Devices.ColorSensor.Impl;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Hardware.Devices.ColorSensor.ColorSensorFix;
import org.woen.Hardware.Devices.ColorSensor.Interface.ColorSensor;

public class ColorSensorImpl implements ColorSensor {

    protected AdafruitI2cColorSensor sensor;

    public ColorSensorImpl(AdafruitI2cColorSensor sensor){
        this.sensor = ColorSensorFix.fix(sensor);
    }

    private int red = 0;
    private final ElapsedTime redTime = new ElapsedTime();
    @Override
    public int getRed(){
        if(redTime.seconds()>0.1){
           redTime.reset();
           red = sensor.red();
        }
        return red;
    }
    
    private int blue = 0;
    private final ElapsedTime blueTime = new ElapsedTime();
    @Override
    public int getBlue(){
        if(blueTime.seconds()>0.1){
            blueTime.reset();
            blue = sensor.blue();
        }
        return blue;
    }  
    
    private int green = 0;
    private final ElapsedTime greenTime = new ElapsedTime();
    @Override
    public int getGreen(){
        if(greenTime.seconds()>0.1){
            greenTime.reset();
            green = sensor.green();
        }
        return green;
    }
    
}
