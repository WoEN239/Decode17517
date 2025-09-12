package org.woen.RobotModule.Modules.Localizer.DeviceListener.Architecture;

import org.woen.Architecture.EventBus.Bus.EventBus;
import org.woen.Architecture.Observers.Observer;

public class LocalizerDeviceObserver extends Observer<LocalizeDeviceData,RegisterNewLocalizeDeviceListener> {

    @Override
    public void onEvent(RegisterNewLocalizeDeviceListener registration) {
            listeners.add(registration.getData());
    }

    public LocalizerDeviceObserver() {
        EventBus.getListenersRegistration().subscribe(RegisterNewLocalizeDeviceListener.class,this::onEvent);
    }

}
