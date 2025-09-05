package org.woen.Hardware.Odometers.Impl;

import org.woen.Hardware.Odometers.Inter.Odometer;

public class OdometerMoc implements Odometer {

    private Double cord;

    private Double vel;

    public OdometerMoc(Double cord, Double vel){
        this.cord = cord;
    }

    @Override
    public double getPos(){
        return  cord;
    }

    @Override
    public double getVel(){
        return  vel;
    }

    @Override
    public void reset(){

    }

}
