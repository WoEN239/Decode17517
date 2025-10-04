package org.woen.Hardware.Devices.Odometers.Impl;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.woen.Hardware.Devices.Odometers.Inter.Odometer;
import org.woen.Util.filter.Filter;
import org.woen.Util.filter.FilterStatus;

public class OdometerImpl implements Odometer {
    private int dir = 1;
    @Override
    public void setDir(int dir) {
        this.dir = dir;
    }

    private static final double SM_PER_TIK = 4.8*Math.PI/8192.0;
    protected DcMotorEx dcMotorEx;

    public OdometerImpl(DcMotorEx dcMotorEx){
        filter.init(new FilterStatus(6, 150, 30, 0.5, 0.1, 0.3));
        this.dcMotorEx = dcMotorEx;
    }

    private final Filter filter = new Filter();

    @Override
    public double getPos(){
        return dir*dcMotorEx.getCurrentPosition()*SM_PER_TIK;
    }

    @Override
    public double getVel(){
        filter.setPos(dcMotorEx.getCurrentPosition());
        filter.update();
        return filter.getVelocity()*dir*SM_PER_TIK;
    }

    @Override
    public void reset(){
        dcMotorEx.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        dcMotorEx.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

}
