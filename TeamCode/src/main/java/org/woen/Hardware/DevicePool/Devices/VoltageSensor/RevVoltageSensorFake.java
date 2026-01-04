package org.woen.Hardware.DevicePool.Devices.VoltageSensor;

public class RevVoltageSensorFake implements RevVoltageSensor{
    @Override
    public double getVoltage() {
        return 12;
    }
}
