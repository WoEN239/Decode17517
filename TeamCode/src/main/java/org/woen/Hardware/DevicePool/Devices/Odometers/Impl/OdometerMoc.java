package org.woen.Hardware.DevicePool.Devices.Odometers.Impl;

import org.woen.Hardware.DevicePool.Devices.Odometers.Inter.Odometer;
import org.woen.Telemetry.ConfigurableVariables.SimpleProvider;

public class OdometerMoc implements Odometer {

    private SimpleProvider<Double> cord;
    private SimpleProvider<Double> vel;

    public OdometerMoc(SimpleProvider<Double> cord, SimpleProvider<Double> vel){
        this.cord = cord;
        this.vel =  vel;
    }

    @Override
    public double getPos(){
        return  cord.get();
    }

    @Override
    public double getVel(){
        return  vel.get();
    }

    @Override
    public void reset(){}

}
