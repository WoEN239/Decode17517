package org.woen.Hardware.ServiseActivationConfig;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.config.ValueProvider;

@Config

public class ServiceActivationOdometers {


    public boolean isOdometersActive = false;


    public static ServiceActivationOdometers getDef(){
        return new ServiceActivationOdometers();
    }
}
