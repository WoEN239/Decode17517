package org.woen.RobotModule.Modules.Localizer.DeviceListener.Architecture;

import org.woen.Architecture.EventBus.IEvent;
import org.woen.Architecture.Observers.IListener;

public class RegisterNewLocalizeDeviceListener implements IEvent<IListener<LocalizeDeviceData>> {

    private final IListener<LocalizeDeviceData> data;

    public RegisterNewLocalizeDeviceListener(IListener<LocalizeDeviceData> data) {
        this.data = data;
    }

    @Override
    public IListener<LocalizeDeviceData> getData() {
        return data;
    }
}
