package org.woen.Hardware.DevicePool.Devices.VoltageSensor;

import com.qualcomm.robotcore.hardware.VoltageSensor;
public class RevVoltageSensorImpl implements RevVoltageSensor{
    private final VoltageSensor sensor;

    public RevVoltageSensorImpl(VoltageSensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public double getVoltage() {
        return sensor.getVoltage();
    }
}
