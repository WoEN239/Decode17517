package org.woen.Hardware.Odometers.Inter;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Hardware.Odometers.Impl.Odometer;

public class OdometerImpl implements Odometer {

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
        return  dcMotorEx.getCurrentPosition();
    }

    private final static int CPS_STEP = 0x10000;

    @Override
    public double getVel(){
        double real = getRawVelocity();
        while (Math.abs(getPos() - real) > CPS_STEP / 2.0) {
            real += Math.signum(getPos() - real) * CPS_STEP;
        }
        return real;
    }

    @Override
    public void reset(){
        dcMotorEx.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        dcMotorEx.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }


}
