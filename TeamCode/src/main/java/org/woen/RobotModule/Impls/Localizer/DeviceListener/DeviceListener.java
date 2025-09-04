package org.woen.RobotModule.Impls.Localizer.DeviceListener;

import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Interfaces.Gyro;
import org.woen.Hardware.Motor.Inter.Motor;
import org.woen.Hardware.Odometers.Impl.Odometer;
import org.woen.RobotModule.Interface.IRobotModule;
import org.woen.RobotModule.Impls.Localizer.DeviceListener.Architecture.LocalizeDeviceData;
import org.woen.RobotModule.Impls.Localizer.DeviceListener.Architecture.LocalizerDeviceObserver;

public class DeviceListener implements IRobotModule {

    private final LocalizerDeviceObserver observer = new LocalizerDeviceObserver();

    DevicePool devicePool = new DevicePool();

    private Odometer rightOd;
    private Odometer leftOd;
    private Odometer sideOd;
    private Gyro gyro;




    @Override
    public void deviceUpdate() {

        LocalizeDeviceData data = new LocalizeDeviceData(
                rightOd.getPos(),
                leftOd.getPos(),
                sideOd.getPos(),
                gyro.getPos(),

                rightOd.getVel(),
                leftOd.getVel(),
                sideOd.getVel(),
                gyro.getVel()
        );

        observer.notifyListeners(data);

    }

    @Override
    public void init() {


    }

}
