package org.woen.Hardware.ServiseActivationConfig;

import com.acmerobotics.dashboard.config.Config;

@Config

public class ServiceMotorActive {

    public boolean isMotorActive = false;

    public boolean isOdometersActive = false;

    public static ServiceMotorActive getDef(){
        return new ServiceMotorActive();
    }

}
