package org.woen.RobotModule.Modules.Localizer.DeviceListener;

import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Gyro.Impl.Gyro;
import org.woen.Hardware.Odometers.Inter.Odometer;
import org.woen.RobotModule.Interface.IRobotModule;
import org.woen.RobotModule.Modules.Localizer.DeviceListener.Architecture.LocalizeDeviceData;
import org.woen.RobotModule.Modules.Localizer.DeviceListener.Architecture.LocalizerDeviceObserver;

public class DeviceListener implements IRobotModule {

    private final LocalizerDeviceObserver observer = new LocalizerDeviceObserver();

    DevicePool devicePool = new DevicePool();

    private Odometer rightOd;
    private Odometer leftOd;
    private Odometer sideOd;
    private Gyro gyro;




    @Override
    public void deviceReadUpdate() {

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
