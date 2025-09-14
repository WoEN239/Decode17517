package org.woen.Hardware.Gyro.Inter;

import org.woen.Hardware.Gyro.Impl.Gyro;

public class GyroMoc implements Gyro {


    private Double vel = 0d;
    private Double yaw = 0d;


    public GyroMoc(Double vel, Double yaw){
        this.vel = vel;
        this.yaw = yaw;
    }


    @Override
    public double getPos() {
        return yaw;
    }

    @Override
    public double getVel() {
        return vel;
    }

}
