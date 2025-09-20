package org.woen.RobotModule.Modules.Localizer.DeviceListener.Impls;

import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Devices.Odometers.Inter.Odometer;
import org.woen.RobotModule.Modules.Localizer.DeviceListener.Architecture.LocalizeDeviceData;
import org.woen.RobotModule.Modules.Localizer.DeviceListener.Architecture.LocalizerDeviceObserver;
import org.woen.RobotModule.Modules.Localizer.DeviceListener.Interface.LocalizerDeviceListener;

public class LocalizerDeviceListenerImpl implements LocalizerDeviceListener {

    private final LocalizerDeviceObserver observer = new LocalizerDeviceObserver();

    private Odometer rightOd;
    private Odometer leftOd;
    private Odometer sideOd;

    @Override
    public void deviceReadUpdate() {
        LocalizeDeviceData data = new LocalizeDeviceData(
                rightOd.getPos(),
                leftOd.getPos(),
                sideOd.getPos(),

                rightOd.getVel(),
                leftOd.getVel(),
                sideOd.getVel()
        );

        observer.notifyListeners(data);
    }

    @Override
    public void init() {
        sideOd = DevicePool.getInstance().sideOd;
        leftOd = DevicePool.getInstance().leftOd;
        rightOd = DevicePool.getInstance().rightOd;
    }

}
