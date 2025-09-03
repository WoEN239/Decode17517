package org.woen.Hardware.ServiseActivationConfig;

import com.acmerobotics.dashboard.config.Config;

@Config

public class ServiceActivationConfig {

    public boolean isMotorActive = false;

    public boolean isOdometersActive = false;

    public static ServiceActivationConfig getDef(){
        return new ServiceActivationConfig();
    }

}
