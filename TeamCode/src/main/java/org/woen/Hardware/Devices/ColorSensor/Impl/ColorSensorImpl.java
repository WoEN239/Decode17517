package org.woen.Hardware.Devices.ColorSensor.Impl;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Hardware.Devices.ColorSensor.ColorSensorFix;
import org.woen.Hardware.Devices.ColorSensor.Interface.ColorSensor;
import org.woen.Util.Arrays.ArrayExtra;
import org.woen.Util.Color.RgbColorVector;

public class ColorSensorImpl implements ColorSensor {

    protected AdafruitI2cColorSensor sensor;

    public ColorSensorImpl(AdafruitI2cColorSensor sensor){
        this.sensor = ColorSensorFix.fix(sensor);
        sensor.setGain(0.1f);
    }

    private int[] redReads = new int[21];
    private int red = 0;
    private final ElapsedTime redTime = new ElapsedTime();
    @Override
    public int getRed(){
        if(redTime.seconds()>0.1){
           redTime.reset();
           ArrayExtra.updateLikeBuffer(sensor.red(),redReads);
        }
        return ArrayExtra.findMedian(redReads);
    }


    private int[] blueReads = new int[21];
    private int blue = 0;
    private final ElapsedTime blueTime = new ElapsedTime();
    @Override
    public int getBlue(){
        if(blueTime.seconds()>0.1){
            blueTime.reset();
            ArrayExtra.updateLikeBuffer(sensor.blue(),blueReads);
        }
        return ArrayExtra.findMedian(blueReads);
    }


    private int[] greenReads = new int[21];
    private int green = 0;
    private final ElapsedTime greenTime = new ElapsedTime();
    @Override
    public int getGreen(){
        if(greenTime.seconds()>0.1){
            greenTime.reset();
            ArrayExtra.updateLikeBuffer(sensor.green(),greenReads);
        }
        return ArrayExtra.findMedian(greenReads);
    }

    @Override
    public RgbColorVector getVector(){
        return new RgbColorVector(getRed(),getGreen(),getBlue());
    }

}
