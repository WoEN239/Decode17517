package org.woen.Hardware.Devices.Odometers.Impl;

import org.woen.Hardware.Devices.Odometers.Inter.Odometer;
import org.woen.Telemetry.Configs.Provider;

public class OdometerMoc implements Odometer {

    private Provider<Double> cord;
    private Provider<Double> vel;

    public OdometerMoc(Provider<Double> cord, Provider<Double> vel){
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
