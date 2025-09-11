package org.woen.Hardware.ServiseActivationConfig;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.config.ValueProvider;

@Config
public class ServiceActivation {



    public class SimpleProvider<T> implements ValueProvider<T> {
        private T data;

        public SimpleProvider(T defaultValue) {
            this.data = defaultValue;
        }

        @Override
        public T get() {
            return data;
        }

        @Override
        public void set(T value) {
            if (value != null) {
                data = value;
            }
        }
    }

    SimpleProvider<Boolean> simpleProviderOdometers = new SimpleProvider(false);

    SimpleProvider<Boolean> simpleProviderMotors = new SimpleProvider(false);

    SimpleProvider<Boolean> simpleProviderGyro = new SimpleProvider(false);



    public void updateService(){
        FtcDashboard.getInstance().addConfigVariable("Odometers","TurnOff/On", simpleProviderOdometers);
        FtcDashboard.getInstance().addConfigVariable("Motors","TurnOff/On", simpleProviderMotors);
        FtcDashboard.getInstance().addConfigVariable("Gyro","TurnOff/On", simpleProviderGyro);
    }

    public boolean getOdometersConf(){
        return simpleProviderOdometers.get();
    }

    public boolean getMotorsConf(){
        return simpleProviderMotors.get();
    }

    public boolean getGyroConf(){
        return simpleProviderGyro.get();
    }



    public static ServiceActivation getDef(){
        return new ServiceActivation();
    }


}
