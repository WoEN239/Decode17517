package org.woen.Hardware.Devices.Odometers.Impl;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Hardware.Devices.Odometers.Inter.Odometer;

public class OdometerImpl implements Odometer {
    private int dir = 1;
    @Override
    public void setDir(int dir) {
        this.dir = dir;
    }

    private static final double TIK_TO_SM = 4.8*Math.PI/8192.0;
    protected DcMotorEx dcMotorEx;

    protected ElapsedTime time = new ElapsedTime();


    public OdometerImpl(DcMotorEx dcMotorEx){
        this.dcMotorEx = dcMotorEx;
    }


    private double oldVelo = 0d;

    private double oldTimer = (double) System.nanoTime()/1E9;

    public double getRawVelocity(){
        double velo = dcMotorEx.getVelocity();
        if(velo != oldVelo)
            oldVelo = velo;
        return velo;
    }

    @Override
    public double getPos(){
        return dir*dcMotorEx.getCurrentPosition()*TIK_TO_SM;
    }

    private final static int CPS_STEP = 0x10000;

    @Override
    public double getVel(){
        double real = getRawVelocity();
        double pos = dcMotorEx.getCurrentPosition();
        while (Math.abs(pos - real) > CPS_STEP / 2.0) {
            real += Math.signum(pos - real) * CPS_STEP;
        }
        return real*TIK_TO_SM*dir;
    }

    @Override
    public void reset(){
        dcMotorEx.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        dcMotorEx.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }


}
