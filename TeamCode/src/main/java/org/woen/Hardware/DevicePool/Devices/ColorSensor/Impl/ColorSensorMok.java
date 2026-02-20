package org.woen.Hardware.DevicePool.Devices.ColorSensor.Impl;

import org.woen.Hardware.DevicePool.Devices.ColorSensor.Interface.ColorSensor;
import org.woen.Telemetry.ConfigurableVariables.Provider;

public class ColorSensorMok implements ColorSensor {

    protected int red = 0;

    protected int blue = 0;
    protected int green = 0;

    public ColorSensorMok(Provider<Integer> red, Provider<Integer> green, Provider<Integer> blue){

        this.green = green.get();
        this.blue = blue.get();
        this.red = red.get();

     }
    @Override
    public int getRed() {
        return red;
    }

    @Override
    public int getBlue() {
        return blue;
    }

    @Override
    public int getGreen() {
        return green;
    }
}
